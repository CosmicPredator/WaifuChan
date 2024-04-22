package com.cosmic.waifuchan.viewModels

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel

class EnlargedImageViewModel : ViewModel() {

    fun downloadImage(imageUrl: String, context: Context) {
        val imageName = imageUrl.split("/").last()

        val downloadManager = context.getSystemService(DownloadManager::class.java)

        val request = DownloadManager.Request(imageUrl.toUri())
            .setMimeType("image/${imageName.split(".").last()}")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(imageName)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "WaifuChan/${imageName}")

        downloadManager.enqueue(request)
    }

}