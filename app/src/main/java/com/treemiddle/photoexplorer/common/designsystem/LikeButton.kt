package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.extension.rememberSingleClick

@Composable
fun LikeButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onLikeClick = rememberSingleClick(onClick = onClick)

    IconButton(
        onClick = onLikeClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isLiked) {
                Icons.Filled.Favorite
            } else {
                Icons.Filled.FavoriteBorder
            },
            contentDescription = if (isLiked) {
                stringResource(id = R.string.content_description_like)
            } else {
                stringResource(id = R.string.content_description_unlike)
            },
            tint = if (isLiked) {
                Color.Red
            } else {
                Color.White
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    name = "좋아요가 선택된 버튼"
)
private fun P1() {
    LikeButton(
        isLiked = true,
        onClick = {},
    )
}

@Composable
@Preview(
    showBackground = false,
    name = "좋아요가 해제된 버튼"
)
private fun P2() {
    LikeButton(
        isLiked = false,
        onClick = {},
    )
}