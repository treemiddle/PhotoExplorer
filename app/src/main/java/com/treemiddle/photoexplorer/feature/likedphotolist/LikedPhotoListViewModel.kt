package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPhotoListViewModel @Inject constructor(
    private val likeRepository: LikeRepository
) : BaseViewModelV4<LikedPhotoListContract.Event, LikedPhotoListContract.State, LikedPhotoListContract.Effect>(){
    private var hasNextPage = false

    override fun setInitialState(): LikedPhotoListContract.State {
        return LikedPhotoListContract.State()
    }

    override fun handleEvents(event: LikedPhotoListContract.Event) {
        when (event) {
            is LikedPhotoListContract.Event.LoadMore -> {
                loadMore()
            }
        }
    }

    init {
        getLikedPhotoList()
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

    companion object {
        private const val PAGE_SIZE = 20
    }
}