package com.treemiddle.photoexplorer.domain.model

import com.treemiddle.photoexplorer.core.extension.formatCount

data class PhotoDetail(
    val id: String = "",
    val description: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val fullUrl: String = "",
    val regularUrl: String = "",
    val authorName: String = "",
    val authorProfileImageUrl: String = "",
    val views: Long = 0L,
    val downloads: Long = 0L,
    val likes: Long = 0L,
    val exif: Exif = Exif(),
    val location: Location = Location(),
    val tags: List<String> = emptyList()
) {
    val ratio: Float = if (width > 0 && height > 0) {
        width.toFloat() / height.toFloat()
    } else {
        1f
    }
    val displayViews = views.formatCount()
    val displayDownloads = downloads.formatCount()
    val displayLikes = likes.formatCount()
}

data class Exif(
    val make: String = "",
    val model: String = "",
    val exposureTime: String = "",
    val aperture: String = "",
    val focalLength: String = "",
    val iso: Int = -1,
) {
    val camera = listOfNotNull(
        make,
        model
    ).joinToString(separator = " ")
    val displayAperture = if (aperture.isBlank()) {
        ""
    } else {
        "f/$aperture"
    }
    val displayExposureTime = if (exposureTime.isBlank()) {
        ""
    } else {
        "$exposureTime s"
    }
    val displayFocalLength = if (focalLength.isBlank()) {
        ""
    } else {
        "$focalLength mm"
    }
    val displayIso = if (iso == -1) {
        ""
    } else {
        "$iso"
    }
    val isNotEmpty = make.isNotBlank() || model.isNotBlank() ||
            exposureTime.isNotBlank() || aperture.isNotBlank() ||
            focalLength.isNotBlank() || iso != -1
}

data class Location(
    val name: String = "",
    val city: String = "",
    val country: String = "",
) {
    val displayName = name.takeIf {
        it.isNotBlank()
    } ?: listOf(
        city,
        country
    ).filter {
        it.isNotBlank()
    }.joinToString(separator = ", ").ifEmpty {
        ""
    }
}
