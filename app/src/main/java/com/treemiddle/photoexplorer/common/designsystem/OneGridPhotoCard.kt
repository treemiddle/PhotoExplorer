package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@Composable
fun OneGridPhotoCard(
    imageModel: Any?,
    description: String,
    authorName: String,
    authorProfileImageUrl: String,
    isLiked: Boolean,
    onClick: () -> Unit,
    onToggleLike: () -> Unit,
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
            onClick = onToggleLike
        )
    }
}

@Preview(
    name = "1-Grid isLiked",
    showBackground = true
)
@Composable
private fun P1() {
    OneGridPhotoCard(
        imageModel = "",
        description = "description",
        authorName = "Gilbert Kane",
        authorProfileImageUrl = "",
        isLiked = true,
        onClick = {},
        onToggleLike = {},
    )
}

@Preview(
    name = "1-Grid isLiked.not()",
    showBackground = true
)
@Composable
private fun P2() {
    OneGridPhotoCard(
        imageModel = "",
        description = "description, description description, description description, description\n" +
                "description, description description, description description, description\n" +
                "description, description",
        authorName = "Gilbert Kane, Gilbert Kane, Gilbert Kane, Gilbert Kane, Gilbert Kane" +
                " Gilbert Kane, Gilbert Kane, Gilbert Kane",
        authorProfileImageUrl = "",
        isLiked = false,
        onClick = {},
        onToggleLike = {},
    )
}