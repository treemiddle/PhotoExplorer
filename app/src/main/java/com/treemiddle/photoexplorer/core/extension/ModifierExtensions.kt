package com.treemiddle.photoexplorer.core.extension

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.treemiddle.photoexplorer.core.DEFAULT_CLICK_INTERVAL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

fun Modifier.singleClickable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    delay: Duration = DEFAULT_CLICK_INTERVAL,
): Modifier = composed {
    val scope = rememberCoroutineScope()
    var isClickable by remember {
        mutableStateOf(value = true)
    }

    clickable(
        enabled = enabled && isClickable,
        onClick = {
            if (isClickable) {
                isClickable = false
                onClick()
            }

            if (isClickable.not()) {
                scope.launch {
                    delay(duration = delay)
                    isClickable = true
                }
            }
        }
    )
}
