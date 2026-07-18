package com.treemiddle.photoexplorer.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AuthorInfo(
    name: String,
    profileImageUrl: String,
    modifier: Modifier = Modifier,
    profileImageSize: Dp = 24.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RemoteImage(
            model = profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(size = profileImageSize)
                .clip(shape = CircleShape),
            error = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .size(size = profileImageSize)
                )
            },
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(
    showBackground = true,
    name = "AuthorInfo"
)
@Composable
private fun P1() {
    AuthorInfo(
        name = "작가 이름",
        profileImageUrl = ""
    )
}

@Preview(
    showBackground = true,
    widthDp = 200,
    name = "AuthorInfo"
)
@Composable
private fun P2() {
    AuthorInfo(
        name = "작가 이름이 들어갑니다, 작가 이름이 들어갑니다.",
        profileImageUrl = ""
    )
}

@Preview(
    showBackground = true,
    name = "AuthorInfo - profileImageSize 40"
)
@Composable
private fun P3() {
    AuthorInfo(
        name = "작가 이름",
        profileImageUrl = "",
        profileImageSize = 40.dp
    )
}

@Preview(
    showBackground = true,
    name = "AuthorInfo - 에러 이미지"
)
@Composable
private fun P4() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(size = 24.dp)
        )
        Text(
            text = "작가이름",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}