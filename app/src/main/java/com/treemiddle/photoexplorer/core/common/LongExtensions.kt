package com.treemiddle.photoexplorer.core.common

fun Long.formatCount(): String = when {
    this >= 1_000_000 -> {
        val value = this / 1_000_000.0
        if (value % 1.0 == 0.0) {
            "${value.toInt()}M"
        } else {
            "%.1fM".format(value)
        }
    }

    this >= 1_000 -> {
        val value = this / 1_000.0
        if (value % 1.0 == 0.0) {
            "${value.toInt()}K"
        } else {
            "%.1fK".format(value)
        }
    }

    else -> toString()
}