package com.treemiddle.photoexplorer.common.designsystem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.extension.formatCount

@Composable
fun Stats(
    views: String,
    downloads: String,
    likes: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 20.dp)
    ) {
        Item(
            label = R.string.component_stats_views,
            value = views
        )
        Item(
            label = R.string.component_stats_downloads,
            value = downloads
        )
        Item(
            label = R.string.component_stats_likes,
            value = likes
        )
    }
}

@Composable
private fun Item(
    @StringRes label: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = label),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun P1() {
    Stats(
        views = 19872838734L.formatCount(),
        downloads = 4343L.formatCount(),
        likes = 0L.formatCount()
    )
}
