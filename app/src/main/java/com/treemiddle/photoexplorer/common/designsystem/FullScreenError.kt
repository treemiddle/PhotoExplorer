package com.treemiddle.photoexplorer.common.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.treemiddle.photoexplorer.core.extension.rememberSingleClick

@Composable
fun FullScreenError(
    message: String,
    modifier: Modifier = Modifier,
    onRetryButtonClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(all = 24.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            if (onRetryButtonClick != null) {
                Button(onClick = rememberSingleClick(onClick = onRetryButtonClick)) {
                    Text(stringResource(id = R.string.retry_text))
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    name = "텍스트만 노출된 전체 에러 화면"
)
private fun P1() {
    FullScreenError(message = "표시할 사진이 없어요.")
}

@Composable
@Preview(
    showBackground = true,
    name = "텍스트 + 버튼이 노출된 전체 에러 화면"
)
private fun P2() {
    FullScreenError(
        message = "표시할 사진이 없어요.",
        onRetryButtonClick = {}
    )
}