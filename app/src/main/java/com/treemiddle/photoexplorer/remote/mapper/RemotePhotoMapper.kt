package com.treemiddle.photoexplorer.remote.mapper

import com.treemiddle.photoexplorer.data.model.Exif
import com.treemiddle.photoexplorer.data.model.Location
import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.data.model.PhotoDetailData
import com.treemiddle.photoexplorer.data.model.PhotoInfo
import com.treemiddle.photoexplorer.remote.model.PhotoDetailResponse
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
        },
        hasNext = hasNext
    )
}

fun PhotoDetailResponse.toData(): PhotoDetailData {
    return PhotoDetailData(
        id = id,
        description = description.ifBlank {
            altDescription
        },
        width = width,
        height = height,
        fullUrl = urls.full,
        regularUrl = urls.regular,
        authorName = user.name.ifBlank {
            user.username
        },
        authorProfileImageUrl = user.profileImage.medium.ifBlank {
            user.profileImage.small
        },
        views = views,
        downloads = downloads,
        likes = likes,
        exif = Exif(
            make = exif.make,
            model = exif.model,
            exposureTime = exif.exposureTime,
            aperture = exif.aperture,
            focalLength = exif.focalLength,
            iso = exif.iso
        ),
        location = Location(
            name = location.name,
            city = location.city,
            country = location.country
        ),
        tags = tags.map {
            it.title
        }
    )
}