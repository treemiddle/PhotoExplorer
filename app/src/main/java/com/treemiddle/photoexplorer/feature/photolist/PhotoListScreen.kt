package com.treemiddle.photoexplorer.feature.photolist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.FooterLoading
import com.treemiddle.photoexplorer.common.designsystem.FullScreenError
import com.treemiddle.photoexplorer.common.designsystem.FullScreenLoading
import com.treemiddle.photoexplorer.common.designsystem.PhotoCard
import com.treemiddle.photoexplorer.common.designsystem.TopBar
import com.treemiddle.photoexplorer.core.extension.LoadMoreEffect
import com.treemiddle.photoexplorer.core.extension.rememberSingleClick
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import kotlinx.coroutines.flow.Flow

@Composable
fun PhotoListScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToLiked: () -> Unit,
    viewModel: PhotoListViewModel = hiltViewModel()
) {
    Screen(
        state = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToLiked = onNavigateToLiked
    )
}

@Composable
private fun Screen(
    state: PhotoListContract.State,
    effectFlow: Flow<PhotoListContract.Effect>?,
    onEventSent: (event: PhotoListContract.Event) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToLiked: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        effectFlow?.collect { effect ->
            when (effect) {
                is PhotoListContract.Effect.ShowMessage -> {
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
        isError = state.isError,
        photoList = state.photoList,
        onRetryClick = {
            onEventSent(PhotoListContract.Event.OnRetryClick)
        },
        onPhotoClick = { photoId ->
            onNavigateToDetail(photoId)
        },
        onPhotoLikeClick = { photoId ->
            onEventSent(PhotoListContract.Event.OnPhotoLikeClick(photoId = photoId))
        },
        onLikeClick = onNavigateToLiked,
        onLoadMore = {
            onEventSent(PhotoListContract.Event.LoadMore)
        },
        onRetryLoadMore = {
            onEventSent(PhotoListContract.Event.RetryLoadMore)
        },
        isLoadingMore = state.isLoadingMore,
        isLoadingMoreError = state.isLoadingMoreError
    )
}

@Composable
private fun Content(
    isLoading: Boolean,
    isError: Boolean,
    photoList: List<PhotoInfo>,
    onRetryClick: () -> Unit,
    onLoadMore: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onPhotoLikeClick: (String) -> Unit,
    onLikeClick: () -> Unit,
    isLoadingMore: Boolean = false,
    isLoadingMoreError: Boolean = false,
    onRetryLoadMore: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = rememberSingleClick(onClick = onLikeClick)) {
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
            when {
                isLoading -> {
                    FullScreenLoading()
                }

                isError -> {
                    FullScreenError(
                        message = stringResource(id = R.string.full_screen_error_text),
                        onRetryButtonClick = onRetryClick
                    )
                }

                photoList.isEmpty() -> {
                    FullScreenError(message = stringResource(id = R.string.full_screen_photo_list_empty_text))
                }

                else -> {
                    List(
                        list = photoList,
                        onLoadMore = onLoadMore,
                        onRetryLoadMore = onRetryLoadMore,
                        onPhotoClick = onPhotoClick,
                        onPhotoLikeClick = onPhotoLikeClick,
                        isLoadingMore = isLoadingMore,
                        isLoadingMoreError = isLoadingMoreError
                    )
                }
            }
        }
    }
}

@Composable
private fun List(
    list: List<PhotoInfo>,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onPhotoLikeClick: (String) -> Unit,
    isLoadingMore: Boolean = false,
    isLoadingMoreError: Boolean = false,
    onRetryLoadMore: () -> Unit = {},
) {
    val lazyGirdState = rememberLazyGridState()

    lazyGirdState.LoadMoreEffect(
        itemCount = list.size,
        onLoadMore = onLoadMore
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = modifier,
        state = lazyGirdState,
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
                isLiked = it.isLiked,
                onClick = {
                    onPhotoClick(it.id)
                },
                onLikeClick = {
                    onPhotoLikeClick(it.id)
                }
            )
        }
        if (isLoadingMore || isLoadingMoreError) {
            item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {
                FooterLoading(
                    isLoading = isLoadingMore,
                    isError = isLoadingMoreError,
                    onRetry = onRetryLoadMore
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "로딩화면"
)
@Composable
private fun P1() {
    Content(
        isLoading = true,
        isError = false,
        photoList = emptyList(),
        onRetryClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onLikeClick = {},
        onPhotoLikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "사진 목록"
)
@Composable
private fun P2() {
    Content(
        isLoading = false,
        isError = false,
        photoList = listOf(
            PhotoInfo(
                id = "1",
                thumbUrl = "",
                description = "설명",
                authorName = "작가 이름",
                authorProfileImageUrl = ""
            ),
            PhotoInfo(
                id = "2",
                thumbUrl = "",
                description = "설명",
                authorName = "작가 이름 작가 이름",
                authorProfileImageUrl = ""
            ),
        ),
        onRetryClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onLikeClick = {},
        onPhotoLikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "에러화면"
)
@Composable
private fun P3() {
    Content(
        isLoading = false,
        isError = true,
        photoList = emptyList(),
        onRetryClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onLikeClick = {},
        onPhotoLikeClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "사진 리스트가 없는 화면"
)
@Composable
private fun P4() {
    Content(
        isLoading = false,
        isError = false,
        photoList = emptyList(),
        onRetryClick = {},
        onLoadMore = {},
        onPhotoClick = {},
        onLikeClick = {},
        onPhotoLikeClick = {}
    )
}