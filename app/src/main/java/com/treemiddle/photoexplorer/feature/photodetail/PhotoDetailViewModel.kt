package com.treemiddle.photoexplorer.feature.photodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import com.treemiddle.photoexplorer.feature.photolist.model.UserMessage
import com.treemiddle.photoexplorer.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
    private val likedRepository: LikeRepository
) : BaseViewModelV4<PhotoDetailContract.Event, PhotoDetailContract.State, PhotoDetailContract.Effect>() {
    private val photoId = savedStateHandle[Route.PHOTO_ID] ?: ""

    override fun setInitialState(): PhotoDetailContract.State {
        return PhotoDetailContract.State()
    }

    override fun handleEvents(event: PhotoDetailContract.Event) {
        when (event) {
            is PhotoDetailContract.Event.OnRetryClick -> {
                getPhotoDetail()
            }
        }
    }

    init {
        getPhotoDetail()
    }

    private fun getPhotoDetail() {
        setState {
            copy(
                isLoading = true,
                isError = false
            )
        }
        viewModelScope.launch {
            runCatching {
                delay(2000)
                photoRepository.getPhotoDetail(id = photoId)
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        photoDetail = it,
                        localPhoto = null
                    )
                }
            }.onFailure {
                getLocalPhoto()
            }
        }
    }

    private fun getLocalPhoto() {
        viewModelScope.launch {
            val localPhoto = runCatching {
                likedRepository.getLikedPhoto(photoId)
            }.getOrNull()
            if (localPhoto == null) {
                setState {
                    copy(
                        isLoading = false,
                        isError = true,
                        localPhoto = null,
                        photoDetail = null
                    )
                }
            } else {
                setState {
                    copy(
                        isLoading = false,
                        isError = false,
                        localPhoto = localPhoto,
                        photoDetail = null
                    )
                }
                setEffect {
                    PhotoDetailContract.Effect.ShowMessage(message = UserMessage.DETAIL_INFO_ERROR)
                }
            }
        }
    }
}