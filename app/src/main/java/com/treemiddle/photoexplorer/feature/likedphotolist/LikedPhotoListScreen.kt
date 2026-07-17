package com.treemiddle.photoexplorer.feature.likedphotolist

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.FullScreenError
import com.treemiddle.photoexplorer.common.designsystem.FullScreenLoading
import com.treemiddle.photoexplorer.common.designsystem.PhotoCard
import com.treemiddle.photoexplorer.common.designsystem.TopBar
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
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
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        effectFlow?.collect { effect ->
            when (effect) {
                is LikedPhotoListContract.Effect.ShowMessage -> {
                    Toast.makeText(
                        context,
                        effect.message.value,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Content(
        isLoading = state.isLoading,
        photoList = state.photoList,
        isLoadingMore = state.isLoadingMore,
        onBackButtonClick = onNavigateBack,
        onLoadMore = {
            onEventSent(LikedPhotoListContract.Event.LoadMore)
        },
        onPhotoClick = onNavigateToDetail,
        onUnlikeClick = { photoId ->
            onEventSent(LikedPhotoListContract.Event.UnLikeClick(photoId = photoId))
        }
    )
}

@Composable
private fun Content(
    isLoading: Boolean,
    photoList: List<LikedPhotoCard>,
    isLoadingMore: Boolean,
    onBackButtonClick: () -> Unit,
    onLoadMore: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onUnlikeClick: (String) -> Unit
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
                        onPhotoClick = onPhotoClick,
                        onUnlikeClick = onUnlikeClick
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
    onUnlikeClick: (String) -> Unit,
    modifier: Modifier = Modifier
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
    ) { item ->
        PhotoCard(
            image = File(item.localImagePath),
            description = item.description,
            authorName = item.authorName,
            authorProfileImageUrl = item.authorProfileImageUrl,
            isLiked = item.isLiked,
            onClick = {
                onPhotoClick(item.id)
            },
            onLikeClick = {
                onUnlikeClick(item.id)
            }
        )
    }
}

@Preview(
    showBackground = true,
    name = "전체 로딩 화면"
)
@Composable
private fun P1() {
    Content(
        isLoading = true,
        photoList = emptyList(),
        isLoadingMore = false,
        onBackButtonClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onUnlikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "좋아요된 사진이 없는 화면"
)
@Composable
private fun P2() {
    Content(
        isLoading = false,
        photoList = emptyList(),
        isLoadingMore = false,
        onBackButtonClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onUnlikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "좋아요한 사진 목록 화면"
)
@Composable
private fun P3() {
    Content(
        isLoading = false,
        photoList = listOf(
            LikedPhotoCard(
                id = "1",
                description = "설명",
                authorName = "작가 이름"
            ),
            LikedPhotoCard(
                id = "2",
                description = "설명",
                authorName = "작가 이름 작가 이름"
            ),
        ),
        isLoadingMore = false,
        onBackButtonClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onUnlikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "좋아요한 사진 목록을 더 불러오는 화면"
)
@Composable
private fun P4() {
    Content(
        isLoading = false,
        photoList = listOf(
            LikedPhotoCard(
                id = "1",
                description = "설명",
                authorName = "작가 이름"
            ),
            LikedPhotoCard(
                id = "2",
                description = "설명",
                authorName = "작가 이름 작가 이름"
            ),
            LikedPhotoCard(
                id = "3",
                description = "설명",
                authorName = "작가 이름 작가 이름"
            ),
        ),
        isLoadingMore = true,
        onBackButtonClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onUnlikeClick = {}
    )
}