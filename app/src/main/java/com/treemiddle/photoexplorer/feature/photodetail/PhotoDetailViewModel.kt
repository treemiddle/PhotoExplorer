package com.treemiddle.photoexplorer.feature.photodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.core.ui.BaseViewModel
import com.treemiddle.photoexplorer.core.common.StorageException
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import com.treemiddle.photoexplorer.core.ui.UserMessage
import com.treemiddle.photoexplorer.core.ui.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
    private val likeRepository: LikeRepository
) : BaseViewModel<PhotoDetailContract.Event, PhotoDetailContract.State, PhotoDetailContract.Effect>() {
    private val photoId = savedStateHandle[PHOTO_ID_ARG] ?: ""

    private var pendingLikeRequestCount = 0
    private var isLikedInDatabase = false

    override fun setInitialState(): PhotoDetailContract.State {
        return PhotoDetailContract.State()
    }

    override fun handleEvent(event: PhotoDetailContract.Event) {
        when (event) {
            is PhotoDetailContract.Event.OnRetryClick -> {
                getPhotoDetail()
            }

            is PhotoDetailContract.Event.OnPhotoLikeClick -> {
                onPhotoLikeClick()
            }
        }
    }

    init {
        loadPhoto()
        observeIsLiked()
    }

    private fun loadPhoto() {
        setState {
            copy(isLoading = true)
        }
        viewModelScope.launch {
            val localPhoto = runCatching {
                likeRepository.getLikedPhoto(photoId = photoId)
            }.getOrNull()
            if (localPhoto != null) {
                setState {
                    copy(
                        isLoading = false,
                        localPhoto = localPhoto
                    )
                }
            }
            getPhotoDetail()
        }
    }

    private fun getPhotoDetail() {
        setState {
            copy(
                isLoading = localPhoto == null,
                isError = false,
                isDetailError = false
            )
        }
        viewModelScope.launch {
            runCatching {
                photoRepository.getPhotoDetail(id = photoId)
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        photoDetail = it
                    )
                }
            }.onFailure {
                setState {
                    if (localPhoto == null) {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = it.toUserMessage(message = UserMessage.LOAD_FAILED)
                        )
                    } else {
                        copy(
                            isDetailError = true,
                            errorMessage = it.toUserMessage(message = UserMessage.DETAIL_LOAD_FAILED)
                        )
                    }
                }
            }
        }
    }

    private fun onPhotoLikeClick() {
        if (viewState.value.isError) {
            return
        }
        val isLiked = viewState.value.isLiked
        val request = viewState.value.request
        if (isLiked.not() && request == null) {
            return
        }

        pendingLikeRequestCount++
        setState {
            copy(isLiked = isLiked.not())
        }
        viewModelScope.launch {
            val result = runCatching {
                if (isLiked) {
                    likeRepository.unlike(photoId = photoId)
                } else if (request != null) {
                    likeRepository.like(photo = request)
                }
            }.onFailure {
                val message = when {
                    it is StorageException -> {
                        UserMessage.STORAGE_FULL
                    }

                    isLiked -> {
                        UserMessage.UNLIKE_FAILED
                    }

                    else -> {
                        UserMessage.LIKE_FAILED
                    }
                }
                setEffect {
                    PhotoDetailContract.Effect.ShowMessage(message = message)
                }
            }
            completeLikeRequest(isFailed = result.isFailure)
        }
    }

    private fun completeLikeRequest(isFailed: Boolean) {
        pendingLikeRequestCount--
        if (pendingLikeRequestCount > 0) {
            return
        }
        if (isFailed) {
            setState {
                copy(isLiked = isLikedInDatabase)
            }
        }
    }

    private fun observeIsLiked() {
        viewModelScope.launch {
            likeRepository.observeIsLiked(id = photoId).collect { isLiked ->
                isLikedInDatabase = isLiked
                if (pendingLikeRequestCount == 0) {
                    setState {
                        copy(isLiked = isLiked)
                    }
                }
            }
        }
    }
}