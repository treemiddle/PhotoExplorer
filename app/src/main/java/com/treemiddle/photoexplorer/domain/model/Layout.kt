package com.treemiddle.photoexplorer.domain.model

enum class Layout(val columns: Int) {
    ONE_GRID(1),
    TWO_GRID(2);

    fun toggle(): Layout {
        return if (this == ONE_GRID) {
            TWO_GRID
        } else {
            ONE_GRID
        }
    }

    companion object {
        fun toLayout(columns: Int): Layout {
            return entries.firstOrNull {
                it.columns == columns
            } ?: TWO_GRID
        }
    }
}