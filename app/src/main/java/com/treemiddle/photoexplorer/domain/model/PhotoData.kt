package com.treemiddle.photoexplorer.domain.model

data class PhotoData(val list: List<PhotoInfo> = emptyList())

data class PhotoInfo(
    val id: String = "",
    val description: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val thumbUrl: String = "",
    val regularUrl: String = "",
    val authorName: String = "",
    val authorProfileImageUrl: String = ""
)
