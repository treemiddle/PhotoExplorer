package com.treemiddle.photoexplorer.feature.likedphotolist

import com.treemiddle.photoexplorer.base.BaseViewModelV4
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikedPhotoListViewModel @Inject constructor(

) : BaseViewModelV4<LikedPhotoListContract.Event, LikedPhotoListContract.State, LikedPhotoListContract.Effect>(){
    override fun setInitialState(): LikedPhotoListContract.State {
        return LikedPhotoListContract.State()
    }

    override fun handleEvents(event: LikedPhotoListContract.Event) {
        when (event) {
            else -> {

            }
        }
    }
}