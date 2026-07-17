package com.treemiddle.photoexplorer.data.mapper

import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.domain.model.PhotoData as DomainPhotoData
import com.treemiddle.photoexplorer.domain.model.PhotoInfo as DomainPhotoInfo

fun PhotoData.toDomain(): DomainPhotoData {
    return DomainPhotoData(
        list = list.map {
            DomainPhotoInfo(
                id = it.id,
                description = it.description,
                width = it.width,
                height = it.height,
                thumbUrl = it.thumbUrl,
                regularUrl = it.regularUrl,
                authorName = it.authorName,
                authorProfileImageUrl = it.authorProfileImageUrl
            )
        }
    )
}