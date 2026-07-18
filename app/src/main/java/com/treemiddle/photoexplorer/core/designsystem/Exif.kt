package com.treemiddle.photoexplorer.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.model.ExifInfo

@Composable
fun Exif(
    data: ExifInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.component_exif_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        ExifRowList(exif = data)
    }
}

@Composable
private fun ExifRowList(
    exif: ExifInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        Info(
            label = R.string.component_exif_label_camera,
            text = exif.camera
        )
        Info(
            label = R.string.component_exif_label_aperture,
            text = exif.aperture
        )
        Info(
            label = R.string.component_exif_label_exposure_time,
            text = exif.exposureTime
        )
        Info(
            label = R.string.component_exif_label_focal_length,
            text = exif.focalLength
        )
        Info(
            label = R.string.component_exif_label_iso,
            text = exif.iso
        )
    }
}

@Composable
private fun Info(
    @StringRes label: Int,
    text: String?,
    modifier: Modifier = Modifier
) {
    if (text.isNullOrBlank()) {
        return
    }

    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = label),
            modifier = Modifier.width(width = 100.dp)
        )
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun P1() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 20.dp)
    ) {
        Exif(
            data = ExifInfo(camera = "make model")
        )
        Exif(
            data = ExifInfo(
                camera = "make model",
                aperture = "f/1.8",
                exposureTime = "1/200 s"
            )
        )
        Exif(
            data = ExifInfo(
                camera = "make model",
                aperture = "f/1.8",
                exposureTime = "1/200 s",
                focalLength = "50 mm"
            )
        )
        Exif(
            data = ExifInfo(
                camera = "make model",
                aperture = "f/1.8",
                exposureTime = "1/200 s",
                focalLength = "50 mm",
                iso = "100"
            )
        )
    }
}
