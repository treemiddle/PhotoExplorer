package com.treemiddle.photoexplorer.feature.photodetail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.designsystem.AuthorInfo
import com.treemiddle.photoexplorer.core.common.formatCount
import com.treemiddle.photoexplorer.core.designsystem.Exif
import com.treemiddle.photoexplorer.core.ui.UserMessage
import com.treemiddle.photoexplorer.core.ui.toExifInfo
import com.treemiddle.photoexplorer.core.designsystem.FullScreenLoading
import com.treemiddle.photoexplorer.core.designsystem.FullScreenView
import com.treemiddle.photoexplorer.core.designsystem.LikeButton
import com.treemiddle.photoexplorer.core.designsystem.LocalImage
import com.treemiddle.photoexplorer.core.designsystem.Location
import com.treemiddle.photoexplorer.core.designsystem.RemoteImage
import com.treemiddle.photoexplorer.core.designsystem.Stats
import com.treemiddle.photoexplorer.core.designsystem.TagList
import com.treemiddle.photoexplorer.core.designsystem.TopBar
import com.treemiddle.photoexplorer.core.compose.rememberSingleClick
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.PhotoDetail
import kotlinx.coroutines.flow.Flow

@Composable
fun PhotoDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: PhotoDetailViewModel = hiltViewModel()
) {
    Screen(
        state = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun Screen(
    state: PhotoDetailContract.State,
    effectFlow: Flow<PhotoDetailContract.Effect>,
    onEventSent: (event: PhotoDetailContract.Event) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        effectFlow.collect { effect ->
            when (effect) {
                is PhotoDetailContract.Effect.ShowMessage -> {
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
        isDetailError = state.isDetailError,
        errorMessage = state.errorMessage,
        isLiked = state.isLiked,
        photoDetail = state.photoDetail,
        localPhoto = state.localPhoto,
        onBackClick = onNavigateBack,
        onLikeClick = {
            onEventSent(PhotoDetailContract.Event.OnPhotoLikeClick)
        },
        onRetryClick = {
            onEventSent(PhotoDetailContract.Event.OnRetryClick)
        }
    )
}

@Composable
private fun Content(
    isLoading: Boolean,
    isError: Boolean,
    isDetailError: Boolean,
    errorMessage: UserMessage,
    isLiked: Boolean,
    photoDetail: PhotoDetail?,
    localPhoto: LikedPhotoCard?,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.photo_detail_topbar_title),
                onBack = onBackClick,
                actions = {
                    LikeButton(
                        isLiked = isLiked,
                        onClick = onLikeClick
                    )
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
                    FullScreenView(
                        message = stringResource(id = errorMessage.value),
                        onRetryButtonClick = onRetryClick
                    )
                }

                else -> {
                    List(
                        authorName = (photoDetail?.authorName ?: localPhoto?.authorName).orEmpty(),
                        profileImageUrl = (photoDetail?.authorProfileImageUrl
                            ?: localPhoto?.authorProfileImageUrl).orEmpty(),
                        description = (photoDetail?.description
                            ?: localPhoto?.description).orEmpty(),
                        photoDetail = photoDetail,
                        isDetailError = isDetailError,
                        errorMessage = errorMessage,
                        onRetryClick = onRetryClick,
                        image = {
                            when {
                                photoDetail != null -> {
                                    RemoteImage(
                                        model = photoDetail.fullUrl,
                                        contentDescription = photoDetail.description,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(ratio = photoDetail.ratio),
                                        placeholder = localPhoto?.let { local ->
                                            {
                                                LocalImage(
                                                    path = local.localImagePath,
                                                    contentDescription = local.description,
                                                    modifier = Modifier.matchParentSize()
                                                )
                                            }
                                        }
                                    )
                                }

                                localPhoto != null -> {
                                    LocalImage(
                                        path = localPhoto.localImagePath,
                                        contentDescription = localPhoto.description,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(ratio = localPhoto.ratio)
                                    )
                                }

                                else -> Unit
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun List(
    authorName: String,
    profileImageUrl: String,
    description: String,
    photoDetail: PhotoDetail?,
    isDetailError: Boolean,
    errorMessage: UserMessage,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        makeHeader(
            authorName = authorName,
            profileImageUrl = profileImageUrl,
            description = description,
            image = image
        )
        when {
            photoDetail != null -> {
                makeBody(photoDetail = photoDetail)
            }

            isDetailError -> {
                makeDetailError(
                    errorMessage = errorMessage,
                    onRetryClick = onRetryClick
                )
            }

            else -> {
                makeDetailLoading()
            }
        }
    }
}

private fun LazyListScope.makeHeader(
    authorName: String,
    profileImageUrl: String,
    description: String,
    image: @Composable () -> Unit,
) {
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)

    item {
        image()
        AuthorInfo(
            name = authorName,
            profileImageUrl = profileImageUrl,
            profileImageSize = 36.dp,
            modifier = modifier
        )
        if (description.isNotBlank()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
            )
        }
    }
}

private fun LazyListScope.makeDetailError(
    errorMessage: UserMessage,
    onRetryClick: () -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = errorMessage.value),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Button(onClick = rememberSingleClick(onClick = onRetryClick)) {
                Text(text = stringResource(id = R.string.retry_text))
            }
        }
    }
}

private fun LazyListScope.makeDetailLoading() {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size = 24.dp),
                strokeWidth = 2.dp
            )
        }
    }
}

private fun LazyListScope.makeBody(photoDetail: PhotoDetail) {
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
    item {
        HorizontalDivider(modifier = modifier)
        Stats(
            views = photoDetail.views.formatCount(),
            downloads = photoDetail.downloads.formatCount(),
            likes = photoDetail.likes.formatCount(),
            modifier = modifier
        )
        if (photoDetail.exif.isNotEmpty) {
            HorizontalDivider(modifier = modifier)
            Exif(
                data = photoDetail.exif.toExifInfo(),
                modifier = modifier
            )
        }
        if (photoDetail.location.displayName.isNotBlank()) {
            HorizontalDivider(modifier = modifier)
            Location(
                text = photoDetail.location.displayName,
                modifier = modifier
            )
        }
        if (photoDetail.tags.isNotEmpty()) {
            HorizontalDivider(modifier = modifier)
            TagList(
                list = photoDetail.tags,
                modifier = modifier
            )
        }
    }
}