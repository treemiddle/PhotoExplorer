package com.treemiddle.photoexplorer.feature.photodetail

import com.treemiddle.photoexplorer.base.BaseViewModelV4
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
) : BaseViewModelV4<PhotoDetailContract.Event, PhotoDetailContract.State, PhotoDetailContract.Effect>() {
    override fun setInitialState(): PhotoDetailContract.State {
        return PhotoDetailContract.State()
    }

    override fun handleEvents(event: PhotoDetailContract.Event) {
        when (event) {
            else -> {
            }
        }
    }
}