package com.treemiddle.photoexplorer.feature.photodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import com.treemiddle.photoexplorer.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository
) : BaseViewModelV4<PhotoDetailContract.Event, PhotoDetailContract.State, PhotoDetailContract.Effect>() {
    private val photoId = savedStateHandle[Route.PHOTO_ID] ?: ""

    override fun setInitialState(): PhotoDetailContract.State {
        return PhotoDetailContract.State()
    }

    override fun handleEvents(event: PhotoDetailContract.Event) {
        when (event) {
            else -> {
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
                photoRepository.getPhotoDetail(id = photoId)
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        isError = false,
                        photoDetail = it
                    )
                }
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
}