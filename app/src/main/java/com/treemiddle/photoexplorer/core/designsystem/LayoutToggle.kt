package com.treemiddle.photoexplorer.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.compose.rememberSingleClick
import com.treemiddle.photoexplorer.core.model.PhotoLayout

@Composable
fun LayoutToggle(
    layout: PhotoLayout,
    onToggle: () -> Unit
) {
    val onClick = rememberSingleClick(onClick = onToggle)

    IconButton(onClick = onClick) {
        Icon(
            imageVector = when (layout) {
                PhotoLayout.ONE_GRID -> {
                    Icons.AutoMirrored.Filled.ViewList
                }

                PhotoLayout.TWO_GRID -> {
                    Icons.Filled.GridView
                }
            },
            contentDescription = when (layout) {
                PhotoLayout.ONE_GRID -> {
                    stringResource(id = R.string.layout_one_grid_description)
                }

                PhotoLayout.TWO_GRID -> {
                    stringResource(id = R.string.layout_two_grid_description)
                }
            },
        )
    }
}

@Preview(
    name = "2-Gird",
    showBackground = true
)
@Composable
private fun P1() {
    LayoutToggle(
        PhotoLayout.TWO_GRID,
        onToggle = {}
    )
}

@Preview(
    name = "1-Gird",
    showBackground = true
)
@Composable
private fun P2() {
    LayoutToggle(
        PhotoLayout.ONE_GRID,
        onToggle = {}
    )
}
