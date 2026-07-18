package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.core.extension.singleClickable
import com.treemiddle.photoexplorer.domain.model.Layout

@Composable
fun PhotoCard(
    image: Any?,
    description: String,
    authorName: String,
    authorProfileImageUrl: String,
    isLiked: Boolean,
    layout: Layout,
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (layout) {
        Layout.ONE_GRID -> {
            OneGrid(
                imageModel = image,
                description = description,
                authorName = authorName,
                authorProfileImageUrl = authorProfileImageUrl,
                isLiked = isLiked,
                onClick = onClick,
                onLikeClick = onLikeClick,
                modifier = modifier
            )
        }

        Layout.TWO_GRID -> {
            TwoGrid(
                image = image,
                description = description,
                authorName = authorName,
                authorProfileImageUrl = authorProfileImageUrl,
                isLiked = isLiked,
                onClick = onClick,
                onLikeClick = onLikeClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun OneGrid(
    imageModel: Any?,
    description: String,
    authorName: String,
    authorProfileImageUrl: String,
    isLiked: Boolean,
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .singleClickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        RemoteImage(
            model = imageModel,
            contentDescription = description,
            modifier = Modifier
                .size(size = 72.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp))
        )
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        ) {
            AuthorInfo(
                name = authorName,
                profileImageUrl = authorProfileImageUrl
            )
            Text(
                text = description.takeIf {
                    it.isNotBlank()
                } ?: authorName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        LikeButton(
            isLiked = isLiked,
            onClick = onLikeClick
        )
    }
}

@Composable
private fun TwoGrid(
    image: Any?,
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
                model = image,
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
    name = "1-Grid isLiked",
    showBackground = true
)
@Composable
private fun P1() {
    PhotoCard(
        image = "",
        description = "description",
        authorName = "Gilbert Kane",
        authorProfileImageUrl = "",
        layout = Layout.ONE_GRID,
        isLiked = true,
        onClick = {},
        onLikeClick = {},
    )
}

@Preview(
    name = "1-Grid isLiked.not()",
    showBackground = true
)
@Composable
private fun P2() {
    PhotoCard(
        image = "",
        description = "description, description description, description description, description\n" +
                "description, description description, description description, description\n" +
                "description, description",
        authorName = "Gilbert Kane, Gilbert Kane, Gilbert Kane, Gilbert Kane, Gilbert Kane" +
                " Gilbert Kane, Gilbert Kane, Gilbert Kane",
        authorProfileImageUrl = "",
        layout = Layout.ONE_GRID,
        isLiked = false,
        onClick = {},
        onLikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "2-Grid PhotoCard isLiked"
)
@Composable
private fun P3() {
    PhotoCard(
        image = null,
        description = "설명",
        authorName = "작가 이름",
        authorProfileImageUrl = "",
        layout = Layout.TWO_GRID,
        isLiked = true,
        onClick = {},
        onLikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "2-Grid PhotoCard isLiked.not()- 작가 이름 말줄임 표시"
)
@Composable
private fun P4() {
    PhotoCard(
        image = null,
        description = "설명",
        authorName = "작가 이름 작가 이름 작가 이름 작가 이름 작가 이름 작가 이름 작가 이름 작가 이름",
        authorProfileImageUrl = "",
        layout = Layout.TWO_GRID,
        isLiked = false,
        onClick = {},
        onLikeClick = {}
    )
}