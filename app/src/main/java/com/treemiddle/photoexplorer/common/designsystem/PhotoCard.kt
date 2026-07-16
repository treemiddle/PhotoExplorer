package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.core.extension.singleClickable

@Composable
fun PhotoCard(
    photo: Any?,
    description: String,
    authorName: String,
    authorProfileImageUrl: String,
    isLiked: Boolean,
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .singleClickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1f)
                .clip(shape = RoundedCornerShape(size = 12.dp))
        ) {
            RemoteImage(
                model = photo,
                contentDescription = description,
                modifier = Modifier.fillMaxSize()
            )
            LikeButton(
                isLiked = isLiked,
                onClick = onLikeClick,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        AuthorInfo(
            name = authorName,
            profileImageUrl = authorProfileImageUrl,
            modifier = Modifier.padding(
                horizontal = 2.dp,
                vertical = 6.dp
            )
        )
    }
}

@Preview(
    showBackground = true,
    name = "PhotoCard"
)
@Composable
private fun P1() {
    PhotoCard(
        photo = null,
        description = "설명",
        authorName = "작가 이름",
        authorProfileImageUrl = "",
        isLiked = true,
        onClick = {},
        onLikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "PhotoCard - 작가 이름 말줄임 표시"
)
@Composable
private fun P2() {
    PhotoCard(
        photo = null,
        description = "설명",
        authorName = "작가 이름 작가 이름 작가 이름 작가 이름 작가 이름 작가 이름 작가 이름 작가 이름",
        authorProfileImageUrl = "",
        isLiked = false,
        onClick = {},
        onLikeClick = {}
    )
}