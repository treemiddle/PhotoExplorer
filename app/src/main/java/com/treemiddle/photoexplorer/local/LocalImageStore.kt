package com.treemiddle.photoexplorer.local

import android.content.Context
import android.system.ErrnoException
import android.system.OsConstants
import com.treemiddle.photoexplorer.core.exception.StorageException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalImageStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val dir: File by lazy {
        File(
            context.filesDir,
            LIKED_DIR
        ).apply {
            if (!exists() && !mkdirs()) {
                throw IOException("Failed: $absolutePath")
            }
        }
    }

    suspend fun save(id: String, byteArray: ByteArray): String {
        return withContext(context = Dispatchers.IO) {
            val target = File(dir, "$id.jpg")
            val temp = File(dir, "$id.tmp")

            try {
                temp.writeBytes(byteArray)
                if (temp.renameTo(target).not()) {
                    throw IOException("Failed to rename temp file.")
                }
                target.absolutePath
            } catch (e: IOException) {
                if (e.isNoSpaceLeft()) {
                    throw StorageException(e)
                }
                throw e
            } finally {
                temp.delete()
            }
        }
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        File(dir, "$id.jpg").delete()
    }

    private fun IOException.isNoSpaceLeft(): Boolean {
        var current: Throwable? = this
        while (current != null) {
            if (current is ErrnoException && current.errno == OsConstants.ENOSPC) {
                return true
            }
            current = current.cause
        }
        val text = message.orEmpty()
        return text.contains("ENOSPC", ignoreCase = true) ||
                text.contains("No space left", ignoreCase = true)
    }

    companion object {
        private const val LIKED_DIR = "liked_dir"
    }
}