package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.core.ui.BaseViewModel
import com.treemiddle.photoexplorer.domain.repository.LayoutRepository
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.core.ui.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPhotoListViewModel @Inject constructor(
    private val likeRepository: LikeRepository,
    private val layoutRepository: LayoutRepository
) : BaseViewModel<LikedPhotoListContract.Event, LikedPhotoListContract.State, LikedPhotoListContract.Effect>() {
    private val loadedLimit = MutableStateFlow(value = PAGE_SIZE)
    private var hasNextPage = false

    override fun setInitialState(): LikedPhotoListContract.State {
        return LikedPhotoListContract.State()
    }

    override fun handleEvent(event: LikedPhotoListContract.Event) {
        when (event) {
            is LikedPhotoListContract.Event.LoadMore -> {
                loadMore()
            }

            is LikedPhotoListContract.Event.UnLikeClick -> {
                unLikeClick(photoId = event.photoId)
            }

            LikedPhotoListContract.Event.OnLayoutClick -> {
                onLayoutClick()
            }
        }
    }

    init {
        observeLikedPhotoList()
        observeLayout()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeLikedPhotoList() {
        viewModelScope.launch {
            loadedLimit
                .flatMapLatest { limit ->
                    likeRepository.observeLikedPhotoList(limit = limit).map { photos ->
                        limit to photos
                    }
                }
                .catch {
                    setState {
                        copy(
                            isLoading = false,
                            isLoadingMore = false
                        )
                    }
                    setEffect {
                        LikedPhotoListContract.Effect.ShowMessage(message = UserMessage.LIKED_LIST_LOAD_FAILED)
                    }
                }
                .collect { (limit, photos) ->
                    hasNextPage = photos.size == limit
                    setState {
                        copy(
                            isLoading = false,
                            isLoadingMore = false,
                            photoList = photos
                        )
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
        loadedLimit.value += PAGE_SIZE
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
