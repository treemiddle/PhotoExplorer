package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import java.io.File

@Composable
fun LocalImage(
    path: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    RemoteImage(
        model = File(path),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}

@Preview(name = "로컬 이미지")
@Composable
private fun P1() {
    LocalImage(
        path = "",
        contentDescription = ""
    )
}
