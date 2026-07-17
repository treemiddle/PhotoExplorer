package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.domain.model.toLikedPhotoRequest
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.feature.photolist.model.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPhotoListViewModel @Inject constructor(
    private val likeRepository: LikeRepository
) : BaseViewModelV4<LikedPhotoListContract.Event, LikedPhotoListContract.State, LikedPhotoListContract.Effect>() {
    private var hasNextPage = false

    private val unlikeProcessingIds = mutableSetOf<String>()

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
        }
    }

    init {
        getLikedPhotoList()
        observeLikedIds()
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
                        photoList = photoList + it
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
        if (photoId in unlikeProcessingIds) {
            return
        }
        val photo = viewState.value.photoList.find {
            it.id == photoId
        } ?: return

        unlikeProcessingIds += photoId
        viewModelScope.launch {
            runCatching {
                likeRepository.updatePhoto(photo = photo.toLikedPhotoRequest())
            }.onFailure {
                setEffect {
                    LikedPhotoListContract.Effect.ShowMessage(message = UserMessage.UNLIKE_FAILED)
                }
            }
            unlikeProcessingIds -= photoId
        }
    }

    private fun observeLikedIds() {
        viewModelScope.launch {
            likeRepository.likedIds.drop(1).collect { ids ->
                val current = viewState.value.photoList
                val currentIds = current.map { it.id }.toSet()
                val newestLikedAt = current.firstOrNull()?.likedAt ?: 0L

                val hasAdded = (ids - currentIds).isNotEmpty()
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

    companion object {
        private const val PAGE_SIZE = 20
    }
}