package com.treemiddle.photoexplorer.data.model

data class PhotoData(
    val list: List<PhotoInfo> = emptyList(),
    val hasNext: Boolean = false
)

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