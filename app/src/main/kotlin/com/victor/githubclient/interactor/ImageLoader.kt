package com.victor.githubclient.interactor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView

object ImageLoader {
    var cache: MutableMap<String, Bitmap> = mutableMapOf()
    var downloads: MutableList<DownloadImageTask> = arrayListOf()

    fun addCache(url: String, image: Bitmap) {
        cache.put(url, image)
    }
}

class DownloadImageTask(var imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {
    private var url = ""

    override fun doInBackground(vararg urls: String): Bitmap? {
        try {
            url = urls[0]

            val openStream = java.net.URL(url).openStream()
            val icon = BitmapFactory.decodeStream(openStream)

            return icon
        } catch (ex: Exception) {
            return null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null) {
            imageView.setImageBitmap(result)
            ImageLoader.addCache(url, result)
        }
    }
}