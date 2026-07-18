package com.treemiddle.photoexplorer.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.compose.rememberSingleClick

@Composable
fun FooterLoading(
    isLoading: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoading -> {
                Loading()
            }

            isError -> {
                Error(onRetry = onRetry)
            }
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier.size(size = 28.dp))
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    val onRetryClick = rememberSingleClick(onClick = onRetry)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.footer_load_error),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRetryClick) {
            Text(stringResource(id = R.string.retry_text))
        }
    }
}

@Preview(
    showBackground = true,
    name = "로딩"
)
@Composable
private fun P1() {
    FooterLoading(
        isLoading = true,
        isError = false,
    )
}

@Preview(
    showBackground = true,
    name = "에러"
)
@Composable
private fun P2() {
    FooterLoading(
        isLoading = false,
        isError = true,
    )
}