package com.treemiddle.photoexplorer.feature.photolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
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
    private val likedRepository: LikeRepository,
    private val layoutRepository: LayoutRepository
) : BaseViewModelV4<PhotoListContract.Event, PhotoListContract.State, PhotoListContract.Effect>() {
    private var hasNextPage = false
    private var page = 1

    private val inFlightLikeRequestCounts = mutableMapOf<String, Int>()
    private var likedIds: Set<String> = emptySet()

    override fun setInitialState(): PhotoListContract.State {
        return PhotoListContract.State()
    }

    override fun handleEvents(event: PhotoListContract.Event) {
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
                        photoList = it.list.withLikeState()
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
                        photoList = (photoList + it.list)
                            .distinctBy { photo ->
                                photo.id
                            }
                            .withLikeState()
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

        inFlightLikeRequestCounts[photoId] = (inFlightLikeRequestCounts[photoId] ?: 0) + 1
        updateLiked(
            photoId = photoCard.id,
            isLiked = photoCard.isLiked.not()
        )
        viewModelScope.launch {
            val result = runCatching {
                if (photoCard.isLiked) {
                    likedRepository.unlike(photoId = photoCard.id)
                } else {
                    likedRepository.like(photo = photoCard.toLikedPhotoRequest())
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
            finishLikeWork(
                photoId = photoId,
                isFailed = result.isFailure
            )
        }
    }

    private fun finishLikeWork(
        photoId: String,
        isFailed: Boolean
    ) {
        val remaining = (inFlightLikeRequestCounts[photoId] ?: 1) - 1
        if (remaining > 0) {
            inFlightLikeRequestCounts[photoId] = remaining
            return
        }
        inFlightLikeRequestCounts.remove(photoId)
        if (isFailed) {
            updateLiked(
                photoId = photoId,
                isLiked = photoId in likedIds
            )
        }
    }

    private fun updateLiked(
        photoId: String,
        isLiked: Boolean
    ) {
        setState {
            copy(
                photoList = photoList.map {
                    if (it.id == photoId) {
                        it.copy(isLiked = isLiked)
                    } else {
                        it
                    }
                }
            )
        }
    }

    private fun observeLikedIds() {
        viewModelScope.launch {
            likedRepository.likedIds.collect { ids ->
                likedIds = ids
                setState {
                    copy(
                        photoList = photoList.map { photo ->
                            if (photo.id in inFlightLikeRequestCounts) {
                                photo
                            } else {
                                photo.copy(isLiked = photo.id in ids)
                            }
                        }
                    )
                }
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
            if (it.id in inFlightLikeRequestCounts) {
                it
            } else {
                it.copy(isLiked = it.id in likedIds)
            }
        }
    }
}