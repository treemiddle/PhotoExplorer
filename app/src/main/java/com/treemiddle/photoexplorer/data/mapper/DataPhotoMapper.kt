package com.treemiddle.photoexplorer.data.mapper

import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.data.model.PhotoDetailData
import com.treemiddle.photoexplorer.domain.model.Exif
import com.treemiddle.photoexplorer.domain.model.Location
import com.treemiddle.photoexplorer.domain.model.PhotoDetail
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
        },
        hasNext = hasNext
    )
}

fun PhotoDetailData.toDomain(): PhotoDetail {
    return PhotoDetail(
        id = id,
        description = description,
        width = width,
        height = height,
        fullUrl = fullUrl,
        regularUrl = regularUrl,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
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
        tags = tags
    )
}