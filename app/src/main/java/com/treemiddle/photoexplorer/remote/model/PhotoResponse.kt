package com.treemiddle.photoexplorer.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PhotoResponse(
    val list: List<PhotoInfo> = emptyList(),
    val hasNext: Boolean = false
)

@Serializable
data class PhotoInfo(
    val id: String = "",
    val description: String = "",
    @SerialName("alt_description")
    val altDescription: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val urls: Urls = Urls(),
    val user: User = User()
)

@Serializable
data class Urls(
    val full: String = "",
    val regular: String = "",
    val small: String = ""
)

@Serializable
data class User(
    val username: String = "",
    val name: String = "",
    @SerialName("profile_image")
    val profileImage: ProfileImage = ProfileImage(),
)

@Serializable
data class ProfileImage(
    val small: String = "",
    val medium: String = ""
)