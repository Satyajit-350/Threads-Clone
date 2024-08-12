package com.satyajit.threads.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

object Constant {


    const val GITHUB_LOGO_PATH =
        "M111.61,42.72c-1.06,-4.25 -8.92,-34.69 -46.4,-34.69c-37.56,0 -49.73,28.13 -48.76,59.87c0.87,28.59 15.34,52.06 48.3,52.06c23.92,0 41.43,-13.48 41.43,-31.37c0,-14.32 -7.31,-21.64 -19.78,-25.71C66.11,56.28 48.24,61.55 46.52,74.05c-1.37,9.91 7.75,17.81 19.94,16.86c15.87,-1.24 20.28,-15.46 20.28,-27.87c0,-20.28 -11.05,-26.01 -19.82,-26.47c-14.9,-0.77 -20.2,9.23 -20.2,9.23"

    const val THREADS_DATABASE = "threads_database"

    fun getFileExtension(uri: Uri, context: Context): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}