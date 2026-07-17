package com.treemiddle.photoexplorer.feature.photolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.FullScreenError
import com.treemiddle.photoexplorer.common.designsystem.FullScreenLoading
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
    Content(
        isLoading = state.isLoading,
        isError = state.isError,
        photoList = state.photoList,
        onRetryClick = {
            onEventSent(PhotoListContract.Event.OnRetryClick)
        }
    )
}

@Composable
private fun Content(
    isLoading: Boolean,
    isError: Boolean,
    photoList: List<PhotoInfo>,
    onRetryClick: () -> Unit,
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

                else -> {
                    List(list = photoList)
                }
            }
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
        onRetryClick = {}
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
        onRetryClick = {}
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
        onRetryClick = {}
    )
}