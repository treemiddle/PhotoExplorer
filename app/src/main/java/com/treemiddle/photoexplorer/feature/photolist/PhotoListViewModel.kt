package com.treemiddle.photoexplorer.feature.photolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModel
import com.treemiddle.photoexplorer.core.exception.StorageException
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.domain.model.toLikedPhotoRequest
import com.treemiddle.photoexplorer.domain.repository.LayoutRepository
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import com.treemiddle.photoexplorer.feature.common.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val likeRepository: LikeRepository,
    private val layoutRepository: LayoutRepository
) : BaseViewModel<PhotoListContract.Event, PhotoListContract.State, PhotoListContract.Effect>() {
    private var hasNextPage = false
    private var page = 1

    private val pendingLikes = mutableMapOf<String, PendingLike>()
    private var databaseLikedIds: Set<String> = emptySet()

    override fun setInitialState(): PhotoListContract.State {
        return PhotoListContract.State()
    }

    override fun handleEvent(event: PhotoListContract.Event) {
        when (event) {
            PhotoListContract.Event.OnRetryClick -> {
                getPhotoList()
            }

            PhotoListContract.Event.LoadMore -> {
                loadMore()
            }

            PhotoListContract.Event.RetryLoadMore -> {
                retryLoadMore()
            }

            is PhotoListContract.Event.OnPhotoLikeClick -> {
                onPhotoLikeClick(event.photoId)
            }

            PhotoListContract.Event.OnLayoutClick -> {
                onLayoutClick()
            }
        }
    }

    init {
        getPhotoList()
        observeLikedIds()
        observeLayout()
    }

    private fun getPhotoList() {
        setState {
            copy(
                isLoading = true,
                isError = false
            )
        }
        viewModelScope.launch {
            runCatching {
                photoRepository.getPhotoList(page = page)
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        isError = false,
                        photoList = it.list.distinctBy { photo ->
                            photo.id
                        }.withLikeState()
                    )
                }
                updatePaging(hasNext = it.hasNext)
            }.onFailure {
                setState {
                    copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }

    private fun loadMore() {
        if (
            hasNextPage.not() ||
            viewState.value.isLoadingMore ||
            viewState.value.isLoadingMoreError
        ) {
            return
        }
        setState {
            copy(isLoadingMore = true)
        }
        viewModelScope.launch {
            runCatching {
                photoRepository.getPhotoList(page = page)
            }.onSuccess {
                setState {
                    copy(
                        isLoadingMore = false,
                        isLoadingMoreError = false,
                        photoList = (photoList + it.list).distinctBy { photo ->
                            photo.id
                        }.withLikeState()
                    )
                }
                updatePaging(hasNext = it.hasNext)
            }.onFailure {
                setState {
                    copy(
                        isLoadingMore = false,
                        isLoadingMoreError = true
                    )
                }
            }
        }
    }

    private fun retryLoadMore() {
        setState {
            copy(isLoadingMoreError = false)
        }
        loadMore()
    }

    private fun updatePaging(hasNext: Boolean) {
        page++
        hasNextPage = hasNext
    }

    private fun onPhotoLikeClick(photoId: String) {
        val photoCard = viewState.value.photoList.find {
            it.id == photoId
        } ?: return

        pendingLikes[photoId] = PendingLike(
            count = (pendingLikes[photoId]?.count ?: 0) + 1,
            isLiked = photoCard.isLiked.not()
        )
        updateLiked()
        viewModelScope.launch {
            val result = runCatching {
                if (photoCard.isLiked) {
                    likeRepository.unlike(photoId = photoCard.id)
                } else {
                    likeRepository.like(photo = photoCard.toLikedPhotoRequest())
                }
            }.onFailure {
                val message = when {
                    it is StorageException -> {
                        UserMessage.STORAGE_FULL
                    }

                    photoCard.isLiked -> {
                        UserMessage.UNLIKE_FAILED
                    }

                    else -> {
                        UserMessage.LIKE_FAILED
                    }
                }
                setEffect {
                    PhotoListContract.Effect.ShowMessage(message = message)
                }
            }
            completeLikeRequest(
                photoId = photoId,
                isLiked = photoCard.isLiked.not(),
                isFailed = result.isFailure
            )
        }
    }

    private fun completeLikeRequest(
        photoId: String,
        isLiked: Boolean,
        isFailed: Boolean
    ) {
        val pending = pendingLikes[photoId] ?: return
        if (pending.count > 1) {
            pendingLikes[photoId] = pending.copy(count = pending.count - 1)
            return
        }
        pendingLikes.remove(key = photoId)
        if (isFailed.not()) {
            databaseLikedIds = if (isLiked) {
                databaseLikedIds + photoId
            } else {
                databaseLikedIds - photoId
            }
        }
        updateLiked()
    }

    private fun updateLiked() {
        setState {
            copy(photoList = photoList.withLikeState())
        }
    }

    private fun observeLikedIds() {
        viewModelScope.launch {
            likeRepository.likedIds.collect { ids ->
                databaseLikedIds = ids
                updateLiked()
            }
        }
    }

    private fun observeLayout() {
        viewModelScope.launch {
            layoutRepository.layout.collect { layout ->
                setState {
                    copy(layout = layout)
                }
            }
        }
    }

    private fun onLayoutClick() {
        viewModelScope.launch {
            layoutRepository.update(layout = viewState.value.layout.toggle())
        }
    }

    private fun List<PhotoInfo>.withLikeState(): List<PhotoInfo> {
        return map {
            it.copy(isLiked = pendingLikes[it.id]?.isLiked ?: (it.id in databaseLikedIds))
        }
    }

    private data class PendingLike(
        val count: Int,
        val isLiked: Boolean
    )
}