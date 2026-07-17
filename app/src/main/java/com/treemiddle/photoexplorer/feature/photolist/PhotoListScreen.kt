package com.treemiddle.photoexplorer.feature.photolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.PhotoCard
import com.treemiddle.photoexplorer.common.designsystem.TopBar
import com.treemiddle.photoexplorer.core.extension.rememberSingleClick
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import kotlinx.coroutines.flow.Flow

@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel = hiltViewModel()
) {
    Screen(
        state = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents
    )
}

@Composable
private fun Screen(
    state: PhotoListContract.State,
    effectFlow: Flow<PhotoListContract.Effect>?,
    onEventSent: (event: PhotoListContract.Event) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        effectFlow?.collect { effect ->
            when (effect) {
                else -> {

                }
            }
        }
    }
    Content(photoList = state.photoList)
}

@Composable
private fun Content(
    photoList: List<PhotoInfo>
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = rememberSingleClick(onClick = {})) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.content_description_photo_list_topbar_like),
                            tint = Color.Red,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            List(list = photoList)
        }
    }
}

@Composable
private fun List(
    list: List<PhotoInfo>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = modifier,
        contentPadding = PaddingValues(all = 10.dp),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        items(
            items = list,
            key = {
                it.id
            }
        ) {
            PhotoCard(
                image = it.thumbUrl,
                description = it.description,
                authorName = it.authorName,
                authorProfileImageUrl = it.authorProfileImageUrl,
                isLiked = false,
                onClick = {

                },
                onLikeClick = {

                }
            )
        }
    }
}