package com.treemiddle.photoexplorer.common.designsystem

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
import com.treemiddle.photoexplorer.domain.model.Exif

@Composable
fun Exif(
    data: Exif,
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
    exif: Exif,
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
            text = exif.displayAperture
        )
        Info(
            label = R.string.component_exif_label_exposure_time,
            text = exif.displayExposureTime
        )
        Info(
            label = R.string.component_exif_label_focal_length,
            text = exif.displayFocalLength
        )
        Info(
            label = R.string.component_exif_label_iso,
            text = exif.displayIso
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
            data = Exif(
                make = "make",
                model = "model"
            )
        )
        Exif(
            data = Exif(
                make = "make",
                model = "model",
                aperture = "aperture",
                exposureTime = "exposureTime"
            )
        )
        Exif(
            data = Exif(
                make = "make",
                model = "model",
                aperture = "aperture",
                exposureTime = "exposureTime",
                focalLength = "focalLength"
            )
        )
        Exif(
            data = Exif(
                make = "make",
                model = "model",
                aperture = "aperture",
                exposureTime = "exposureTime",
                focalLength = "focalLength",
                iso = 100
            )
        )
    }
}