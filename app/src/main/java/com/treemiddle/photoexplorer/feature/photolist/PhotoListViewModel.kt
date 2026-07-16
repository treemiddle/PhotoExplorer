package com.treemiddle.photoexplorer.feature.photolist

import com.treemiddle.photoexplorer.base.BaseViewModelV4
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor() :
    BaseViewModelV4<PhotoListContract.Event, PhotoListContract.State, PhotoListContract.Effect>() {
    override fun setInitialState(): PhotoListContract.State {
        return PhotoListContract.State()
    }

    override fun handleEvents(event: PhotoListContract.Event) {
        when (event) {
            else -> {}
        }
    }
}