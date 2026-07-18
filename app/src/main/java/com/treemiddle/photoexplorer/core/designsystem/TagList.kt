package com.treemiddle.photoexplorer.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.R

@Composable
fun TagList(
    list: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.component_tag_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
            list.forEach { tag ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(text = tag)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun P1() {
    TagList(
        list = listOf(
            "tag1",
            "tag2",
            "tag3",
            "tag4",
            "tag5",
            "tag6"
        )
    )
}