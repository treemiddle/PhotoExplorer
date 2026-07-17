package com.treemiddle.photoexplorer.feature.likedphotolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.FullScreenError
import com.treemiddle.photoexplorer.common.designsystem.FullScreenLoading
import com.treemiddle.photoexplorer.common.designsystem.PhotoCard
import com.treemiddle.photoexplorer.common.designsystem.TopBar
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.feature.common.PhotoList
import kotlinx.coroutines.flow.Flow
import java.io.File

@Composable
fun LikedPhotoListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: LikedPhotoListViewModel = hiltViewModel()
) {
    Screen(
        state = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigateBack = onNavigateBack,
        onNavigateToDetail = onNavigateToDetail
    )
}

@Composable
private fun Screen(
    state: LikedPhotoListContract.State,
    effectFlow: Flow<LikedPhotoListContract.Effect>?,
    onEventSent: (event: LikedPhotoListContract.Event) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
   Content(
       isLoading = state.isLoading,
       photoList = state.photoList,
       isLoadingMore = state.isLoadingMore,
       onBackButtonClick = onNavigateBack,
       onLoadMore = {
           onEventSent(LikedPhotoListContract.Event.LoadMore)
       },
       onPhotoClick = onNavigateToDetail
   )
}

@Composable
private fun Content(
    isLoading: Boolean,
    photoList: List<LikedPhotoCard>,
    isLoadingMore: Boolean,
    onBackButtonClick: () -> Unit,
    onLoadMore: () -> Unit,
    onPhotoClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.liked_photo_list_topbar_title),
                onBack = onBackButtonClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            when {
                isLoading -> {
                    FullScreenLoading()
                }

                photoList.isEmpty() -> {
                    FullScreenError(message = stringResource(id = R.string.liked_photo_list_empty_text))
                }

                else -> {
                    List(
                        list = photoList,
                        isLoadingMore = isLoadingMore,
                        onLoadMore = onLoadMore,
                        onPhotoClick = onPhotoClick
                    )
                }
            }
        }
    }
}

@Composable
private fun List(
    list: List<LikedPhotoCard>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onPhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    PhotoList(
        list = list,
        key = {
            it.id
        },
        onLoadMore = onLoadMore,
        modifier = modifier,
        isLoadingMore = isLoadingMore,
        isLoadingMoreError = false
    ) {
        PhotoCard(
            image = File(it.localImagePath),
            description = it.description,
            authorName = it.authorName,
            authorProfileImageUrl = it.authorProfileImageUrl,
            isLiked = it.isLiked,
            onClick = {
                onPhotoClick(it.id)
            },
            onLikeClick = {

            }
        )
    }
}