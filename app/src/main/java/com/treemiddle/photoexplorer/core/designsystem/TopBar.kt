package com.treemiddle.photoexplorer.core.designsystem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.compose.rememberSingleClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = rememberSingleClick(onClick = onBack)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        },
        actions = actions
    )
}

@Preview(
    showBackground = true,
    name = "TopBar"
)
@Composable
private fun P1() {
    TopBar(title = "TopBar")
}

@Preview(
    showBackground = true,
    name = "TopBar - 뒤로가기 버튼"
)
@Composable
private fun P2() {
    TopBar(
        title = "TopBar",
        onBack = {}
    )
}

@Preview(
    showBackground = true,
    name = "TopBar - 우측 버튼"
)
@Composable
private fun P3() {
    TopBar(
        title = "TopBar",
        actions = {
            LikeButton(
                isLiked = true,
                onClick = {}
            )
        }
    )
}

@Preview(
    showBackground = true,
    name = "TopBar - 뒤로가기 + 우측 버튼"
)
@Composable
private fun P4() {
    TopBar(
        title = "TopBar",
        onBack = {},
        actions = {
            LikeButton(
                isLiked = true,
                onClick = {}
            )
        }
    )
}