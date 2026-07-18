package com.treemiddle.photoexplorer.core.ui

import com.treemiddle.photoexplorer.core.model.ExifInfo
import com.treemiddle.photoexplorer.core.model.PhotoLayout
import com.treemiddle.photoexplorer.domain.model.Exif
import com.treemiddle.photoexplorer.domain.model.Layout

fun Layout.toPhotoLayout(): PhotoLayout {
    return when (this) {
        Layout.ONE_GRID -> {
            PhotoLayout.ONE_GRID
        }

        Layout.TWO_GRID -> {
            PhotoLayout.TWO_GRID
        }
    }
}

fun Exif.toExifInfo(): ExifInfo {
    return ExifInfo(
        camera = camera,
        aperture = displayAperture,
        exposureTime = displayExposureTime,
        focalLength = displayFocalLength,
        iso = displayIso
    )
}
