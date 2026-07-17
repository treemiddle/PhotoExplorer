package com.treemiddle.photoexplorer.domain.usecase

import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import javax.inject.Inject

class SelectPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val likeRepository: LikeRepository
) {
    suspend operator fun invoke(
        photoCard: PhotoInfo
    ) {
        photoRepository.trackDownloadApi(id = photoCard.id)
        likeRepository.addPhoto(photoInfo = photoCard)
    }
}