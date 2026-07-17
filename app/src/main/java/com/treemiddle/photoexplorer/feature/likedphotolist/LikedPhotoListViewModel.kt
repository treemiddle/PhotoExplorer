package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.lifecycle.viewModelScope
import com.treemiddle.photoexplorer.base.BaseViewModelV4
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPhotoListViewModel @Inject constructor(
    private val likeRepository: LikeRepository
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

    init {
        getLikedPhotoList()
    }

    private fun getLikedPhotoList() {
        setState {
            copy(isLoading = true)
        }
        viewModelScope.launch {
            runCatching {
                likeRepository.getLikedPhotoList(offset = 0)
            }.onSuccess {
                setState {
                    copy(
                        isLoading = false,
                        photoList = it
                    )
                }
            }.onFailure {
                setState {
                    copy(isLoading = false)
                }
            }
        }
    }
}