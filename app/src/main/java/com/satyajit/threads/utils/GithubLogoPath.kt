package com.satyajit.threads.utils

import android.graphics.Matrix
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.PathParser


object GithubLogoPath {

    val originalPath = PathParser().parsePathString(Constant.GITHUB_LOGO_PATH).toPath().asAndroidPath()

    val scaledPath = android.graphics.Path(originalPath)

    init {
        val scaleMatrix = Matrix()

        // Calculate the required scale factor to fit the height within 80dp
        val desiredHeight = 60f
        val bounds = android.graphics.RectF()
        originalPath.computeBounds(bounds, true)
        val scaleFactor = desiredHeight / bounds.height()

        // Apply scaling to the matrix
        scaleMatrix.setScale(scaleFactor, scaleFactor, bounds.centerX(), bounds.centerY())
        scaledPath.transform(scaleMatrix)
    }
}