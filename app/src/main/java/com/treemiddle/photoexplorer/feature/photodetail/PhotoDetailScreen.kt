package com.treemiddle.photoexplorer.feature.photodetail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.common.designsystem.AuthorInfo
import com.treemiddle.photoexplorer.common.designsystem.Exif
import com.treemiddle.photoexplorer.common.designsystem.FullScreenError
import com.treemiddle.photoexplorer.common.designsystem.FullScreenLoading
import com.treemiddle.photoexplorer.common.designsystem.LikeButton
import com.treemiddle.photoexplorer.common.designsystem.LocalImage
import com.treemiddle.photoexplorer.common.designsystem.Location
import com.treemiddle.photoexplorer.common.designsystem.RemoteImage
import com.treemiddle.photoexplorer.common.designsystem.Stats
import com.treemiddle.photoexplorer.common.designsystem.TagList
import com.treemiddle.photoexplorer.common.designsystem.TopBar
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
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        effectFlow?.collect { effect ->
            when (effect) {
                is PhotoDetailContract.Effect.ShowMessage -> {
                    Toast.makeText(
                        context,
                        effect.message.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    Content(
        isLoading = state.isLoading,
        isError = state.isError,
        isLiked = state.isLiked,
        photoDetail = state.photoDetail,
        localPhoto = state.localPhoto,
        onBackClick = onNavigateBack,
        onLikeClick = {

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
                        onClick = onLikeClick,
                        withScrim = false
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
                    FullScreenError(
                        message = stringResource(R.string.full_screen_error_text),
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
                        image = {
                            when {
                                photoDetail != null -> {
                                    RemoteImage(
                                        model = photoDetail.fullUrl,
                                        contentDescription = photoDetail.description,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(ratio = photoDetail.ratio)
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
        if (photoDetail != null) {
            makeBody(photoDetail = photoDetail)
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

private fun LazyListScope.makeBody(photoDetail: PhotoDetail) {
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
    item {
        HorizontalDivider(modifier = modifier)
        Stats(
            views = photoDetail.displayViews,
            downloads = photoDetail.displayDownloads,
            likes = photoDetail.displayLikes,
            modifier = modifier
        )
        if (photoDetail.exif.isNotEmpty) {
            HorizontalDivider(modifier = modifier)
            Exif(
                data = photoDetail.exif,
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