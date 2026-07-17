package com.treemiddle.photoexplorer.remote.mapper

import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.data.model.PhotoInfo
import com.treemiddle.photoexplorer.remote.model.PhotoResponse

fun PhotoResponse.toData(): PhotoData {
    return PhotoData(
        list = list.map {
            PhotoInfo(
                id = it.id,
                description = it.description.ifBlank {
                    it.altDescription
                },
                width = it.width,
                height = it.height,
                thumbUrl = it.urls.small,
                regularUrl = it.urls.regular,
                authorName = it.user.name.ifBlank {
                    it.user.username
                },
                authorProfileImageUrl = it.user.profileImage.medium.ifBlank {
                    it.user.profileImage.small
                },
            )
        }
    )
}