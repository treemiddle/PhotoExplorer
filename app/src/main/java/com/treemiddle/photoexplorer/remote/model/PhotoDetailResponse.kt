package com.treemiddle.photoexplorer.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetailResponse(
    val id: String = "",
    val description: String = "",
    @SerialName("alt_description")
    val altDescription: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val urls: Urls = Urls(),
    val user: User = User(),
    val views: Long = 0L,
    val downloads: Long = 0L,
    val likes: Long = 0L,
    val exif: Exif = Exif(),
    val location: Location = Location(),
    val tags: List<Tag> = emptyList()
)

@Serializable
data class Exif(
    val make: String = "",
    val model: String = "",
    val aperture: String = "",
    @SerialName("exposure_time")
    val exposureTime: String = "",
    @SerialName("focal_length")
    val focalLength: String = "",
    val iso: Int = -1
)

@Serializable
data class Location(
    val name: String = "",
    val city: String = "",
    val country: String = ""
)

@Serializable
data class Tag(val title: String = "")