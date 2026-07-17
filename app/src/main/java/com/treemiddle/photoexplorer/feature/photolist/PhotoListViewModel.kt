package com.treemiddle.photoexplorer.feature.photolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
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
}