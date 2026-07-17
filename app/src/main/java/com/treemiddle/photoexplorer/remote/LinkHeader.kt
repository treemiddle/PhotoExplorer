package com.treemiddle.photoexplorer.remote

object LinkHeader {
    fun hasNext(header: String?): Boolean {
        if (header.isNullOrBlank()) {
            return false
        }
        return header.split(",").any {
            it.contains("rel=\"next\"")
        }
    }
}