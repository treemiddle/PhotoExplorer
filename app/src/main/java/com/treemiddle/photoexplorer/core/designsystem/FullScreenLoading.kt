package com.treemiddle.photoexplorer.core.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FullScreenLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FooterLoading(
            isLoading = true,
            isError = false
        )
    }
}

@Preview(
    showBackground = true,
    name = "전체 화면 로딩"
)
@Composable
private fun P1() {
    FullScreenLoading()
}