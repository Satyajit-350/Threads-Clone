package com.satyajit.threads.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionManager {

    fun getPermissionRequest(): List<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissions.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permissions
    }

    fun checkPermissionGranted(context: Context, permissions: List<String>): Boolean{
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(
        permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        permissions: List<String>
    ){
        permissionLauncher.launch(permissions.toTypedArray())
    }

}