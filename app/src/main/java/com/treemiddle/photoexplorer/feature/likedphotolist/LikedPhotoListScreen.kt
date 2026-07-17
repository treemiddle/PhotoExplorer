package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun LikedPhotoListScreen(
    viewModel: LikedPhotoListViewModel = hiltViewModel()
) {
    Screen(
        state = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents
    )
}

@Composable
private fun Screen(
    state: LikedPhotoListContract.State,
    effectFlow: Flow<LikedPhotoListContract.Effect>?,
    onEventSent: (event: LikedPhotoListContract.Event) -> Unit,
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            Text(text = "좋아요한 사진 리스트 화면")
        }
    }
}

@Composable
private fun Content() {

}