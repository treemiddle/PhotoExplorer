package com.treemiddle.photoexplorer.data.model

data class PhotoDetailData(
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
)

data class Exif(
    val make: String = "",
    val model: String = "",
    val exposureTime: String = "",
    val aperture: String = "",
    val focalLength: String = "",
    val iso: Int = -1,
)

data class Location(
    val name: String = "",
    val city: String = "",
    val country: String = "",
)