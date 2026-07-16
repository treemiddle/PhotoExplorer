package com.treemiddle.photoexplorer.feature.photolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.TopBar
import com.treemiddle.photoexplorer.core.extension.rememberSingleClick
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
    Content()
}

@Composable
private fun Content() {
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
            // NOTE : 구현 필요
        }
    }
}