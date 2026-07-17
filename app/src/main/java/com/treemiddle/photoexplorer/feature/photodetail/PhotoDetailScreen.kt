package com.treemiddle.photoexplorer.feature.photodetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.LikeButton
import com.treemiddle.photoexplorer.common.designsystem.TopBar
import kotlinx.coroutines.flow.Flow

@Composable
fun PhotoDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: PhotoDetailViewModel = hiltViewModel()
) {
    Screen(
        state = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun Screen(
    state: PhotoDetailContract.State,
    effectFlow: Flow<PhotoDetailContract.Effect>?,
    onEventSent: (event: PhotoDetailContract.Event) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        effectFlow?.collect { effect ->
            when (effect) {
                else -> {

                }
            }
        }
    }
    Content(
        isLiked = state.isLiked,
        onBackClick = onNavigateBack,
        onLikeClick = {

        }
    )
}

@Composable
private fun Content(
    isLiked: Boolean,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.photo_detail_topbar_title),
                onBack = onBackClick,
                actions = {
                    LikeButton(
                        isLiked = isLiked,
                        onClick = onLikeClick,
                        withScrim = false
                    )
                }
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            Text(text = "사진 상세")
        }
    }
}