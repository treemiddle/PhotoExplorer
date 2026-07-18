package com.treemiddle.photoexplorer.feature.photodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.core.exception.StorageException
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import com.treemiddle.photoexplorer.feature.common.UserMessage
import com.treemiddle.photoexplorer.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
    private val likedRepository: LikeRepository
) : BaseViewModelV4<PhotoDetailContract.Event, PhotoDetailContract.State, PhotoDetailContract.Effect>() {
    private val photoId = savedStateHandle[Route.PHOTO_ID] ?: ""

    private var likeProcessing = false
    private var isLikedInDatabase = false

    override fun setInitialState(): PhotoDetailContract.State {
        return PhotoDetailContract.State()
    }

    override fun handleEvents(event: PhotoDetailContract.Event) {
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
                likedRepository.getLikedPhoto(photoId = photoId)
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
                            isError = true
                        )
                    } else {
                        copy(isDetailError = true)
                    }
                }
            }
        }
    }

    private fun onPhotoLikeClick() {
        if (likeProcessing) {
            return
        }
        val isLiked = viewState.value.isLiked
        val request = viewState.value.request
        if (isLiked.not() && request == null) {
            setEffect {
                PhotoDetailContract.Effect.ShowMessage(message = UserMessage.LIKE_FAILED)
            }
            return
        }

        likeProcessing = true
        setState {
            copy(isLiked = isLiked.not())
        }
        viewModelScope.launch {
            runCatching {
                if (isLiked) {
                    likedRepository.unlike(photoId = photoId)
                } else if (request != null) {
                    likedRepository.like(photo = request)
                }
            }.onFailure {
                setState {
                    copy(isLiked = isLikedInDatabase)
                }
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
            likeProcessing = false
        }
    }

    private fun observeIsLiked() {
        viewModelScope.launch {
            likedRepository.observeIsLiked(id = photoId).collect { isLiked ->
                isLikedInDatabase = isLiked
                if (likeProcessing.not()) {
                    setState {
                        copy(isLiked = isLiked)
                    }
                }
            }
        }
    }
}