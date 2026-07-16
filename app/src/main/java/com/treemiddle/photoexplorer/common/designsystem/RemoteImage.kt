package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.treemiddle.photoexplorer.R

@Composable
fun RemoteImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    loading: (@Composable BoxScope.() -> Unit)? = {
        DefaultImageLoading()
    },
    error: (@Composable BoxScope.() -> Unit)? = {
        DefaultImageError()
    }
) {
    if (LocalInspectionMode.current) {
        Image(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
        return
    }

    val context = LocalPlatformContext.current
    val request = remember(
        key1 = model,
        key2 = context
    ) {
        ImageRequest.Builder(context = context)
            .data(data = model)
            .crossfade(enable = true)
            .build()
    }
    var state by remember(key1 = model) {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
    }

    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        AsyncImage(
            model = request,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale,
            onState = {
                state = it
            }
        )
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                loading?.invoke(this)
            }

            is AsyncImagePainter.State.Error -> {
                error?.invoke(this)
            }

            else -> Unit
        }
    }
}

@Composable
private fun BoxScope.DefaultImageLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .align(alignment = Alignment.Center)
            .size(size = 24.dp),
        strokeWidth = 2.dp
    )
}

@Composable
private fun BoxScope.DefaultImageError(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Filled.BrokenImage,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .align(alignment = Alignment.Center)
            .size(size = 32.dp)
    )
}

@Composable
@Preview(name = "리모트 이미지")
private fun P1() {
    RemoteImage(
        model = "",
        contentDescription = ""
    )
}
