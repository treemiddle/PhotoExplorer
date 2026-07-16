package com.treemiddle.photoexplorer.core.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.treemiddle.photoexplorer.core.DEFAULT_CLICK_INTERVAL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

@Composable
fun rememberSingleClick(
    onClick: () -> Unit,
    delay: Duration = DEFAULT_CLICK_INTERVAL,
): () -> Unit {
    val currentOnClick by rememberUpdatedState(newValue = onClick)
    val scope = rememberCoroutineScope()
    var clickable by remember {
        mutableStateOf(value = true)
    }

    return remember(
        key1 = scope,
        key2 = delay
    ) {
        {
            if (clickable.not()) {
                return@remember
            }

            clickable = false
            currentOnClick()

            scope.launch {
                delay(duration = delay)
                clickable = true
            }
        }
    }
}
