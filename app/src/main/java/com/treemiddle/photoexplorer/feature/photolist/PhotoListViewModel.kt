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
            else -> {}
        }
    }

    init {
        getPhotoList()
    }

    private fun getPhotoList() {
        viewModelScope.launch {
            runCatching {
                photoRepository.getPhotoList(1)
            }.onSuccess {
                setState {
                    copy(photoList = it.list)
                }
            }
        }
    }
}