package com.treemiddle.photoexplorer.feature.photolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.core.exception.StorageException
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import com.treemiddle.photoexplorer.domain.usecase.SelectPhotoUseCase
import com.treemiddle.photoexplorer.feature.photolist.model.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val selectPhotoUseCase: SelectPhotoUseCase,
    private val photoRepository: PhotoRepository
) : BaseViewModelV4<PhotoListContract.Event, PhotoListContract.State, PhotoListContract.Effect>() {
    private var hasNextPage = false
    private var page = 1

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
        }
    }

    init {
        getPhotoList()
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
                        photoList = it.list
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
                        photoList = photoList + it.list
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
        updateLiked(photoId = photoCard.id)
        viewModelScope.launch {
            runCatching {
                selectPhotoUseCase(photoCard)
            }.onFailure {
                val message = if (it is StorageException) {
                    UserMessage.STORAGE_FULL
                } else {
                    UserMessage.LIKE_FAILED
                }
                setEffect {
                    PhotoListContract.Effect.ShowMessage(message = message)
                }
            }
        }
    }

    private fun updateLiked(photoId: String) {
        setState {
            copy(
                photoList = photoList.map {
                    if (it.id == photoId) {
                        it.copy(isLiked = it.isLiked.not())
                    } else {
                        it
                    }
                }
            )
        }
    }
}