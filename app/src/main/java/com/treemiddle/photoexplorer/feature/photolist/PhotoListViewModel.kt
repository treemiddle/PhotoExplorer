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
    override fun setInitialState(): PhotoListContract.State {
        return PhotoListContract.State()
    }

    override fun handleEvents(event: PhotoListContract.Event) {
        when (event) {
            PhotoListContract.Event.OnRetryClick -> {
                getPhotoList()
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
                photoRepository.getPhotoList(1)
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        isError = false,
                        photoList = it.list
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