package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModel
import com.treemiddle.photoexplorer.domain.repository.LayoutRepository
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.feature.common.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPhotoListViewModel @Inject constructor(
    private val likeRepository: LikeRepository,
    private val layoutRepository: LayoutRepository
) : BaseViewModel<LikedPhotoListContract.Event, LikedPhotoListContract.State, LikedPhotoListContract.Effect>() {
    private var hasNextPage = false

    override fun setInitialState(): LikedPhotoListContract.State {
        return LikedPhotoListContract.State()
    }

    override fun handleEvents(event: LikedPhotoListContract.Event) {
        when (event) {
            is LikedPhotoListContract.Event.LoadMore -> {
                loadMore()
            }

            is LikedPhotoListContract.Event.UnLikeClick -> {
                unLikeClick(photoId = event.photoId)
            }

            LikedPhotoListContract.Event.OnClickLayout -> {
                onLayoutClick()
            }
        }
    }

    init {
        getLikedPhotoList()
        observeLikedIds()
        observeLayout()
    }

    private fun getLikedPhotoList() {
        setState {
            copy(isLoading = true)
        }
        viewModelScope.launch {
            runCatching {
                likeRepository.getLikedPhotoList(
                    limit = PAGE_SIZE,
                    offset = 0
                )
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        photoList = it
                    )
                }
                updateNextPage(size = it.size)
            }.onFailure {
                setState {
                    copy(isLoading = false)
                }
                setEffect {
                    LikedPhotoListContract.Effect.ShowMessage(message = UserMessage.LIKED_LIST_LOAD_FAILED)
                }
            }
        }
    }

    private fun loadMore() {
        if (hasNextPage.not() || viewState.value.isLoadingMore) {
            return
        }
        setState {
            copy(isLoadingMore = true)
        }
        viewModelScope.launch {
            runCatching {
                likeRepository.getLikedPhotoList(
                    limit = PAGE_SIZE,
                    offset = viewState.value.photoList.size
                )
            }.onSuccess {
                setState {
                    copy(
                        isLoadingMore = false,
                        photoList = (photoList + it).distinctBy { photo ->
                            photo.id
                        }
                    )
                }
                updateNextPage(size = it.size)
            }.onFailure {
                setState {
                    copy(isLoadingMore = false)
                }
            }
        }
    }

    private fun updateNextPage(size: Int) {
        hasNextPage = size == PAGE_SIZE
    }

    private fun unLikeClick(photoId: String) {
        viewModelScope.launch {
            runCatching {
                likeRepository.unlike(photoId = photoId)
            }.onFailure {
                setEffect {
                    LikedPhotoListContract.Effect.ShowMessage(message = UserMessage.UNLIKE_FAILED)
                }
            }
        }
    }

    private fun observeLikedIds() {
        viewModelScope.launch {
            var previousIds: Set<String>? = null
            likeRepository.likedIds.collect { ids ->
                val prev = previousIds
                previousIds = ids
                if (prev == null) {
                    return@collect
                }
                val current = viewState.value.photoList
                val currentIds = current.map { it.id }.toSet()
                val newestLikedAt = current.firstOrNull()?.likedAt ?: 0L

                val hasAdded = (ids - prev).isNotEmpty()
                val newPhotos = if (hasAdded) {
                    runCatching {
                        likeRepository.getLikedPhotoList(
                            limit = PAGE_SIZE,
                            offset = 0
                        )
                    }.getOrDefault(defaultValue = emptyList()).filter {
                        it.id !in currentIds && it.likedAt > newestLikedAt
                    }
                } else {
                    emptyList()
                }
                setState {
                    copy(
                        photoList = (newPhotos + photoList)
                            .distinctBy {
                                it.id
                            }
                            .filter {
                                it.id in ids
                            }
                            .sortedByDescending {
                                it.likedAt
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

    companion object {
        private const val PAGE_SIZE = 20
    }
}