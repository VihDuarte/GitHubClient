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
        url = urls[0]

        java.net.URL(url).openStream().use {
            return BitmapFactory.decodeStream(it)
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        result?.let {
            imageView.setImageBitmap(result)
            ImageLoader.addCache(url, result)
        }
    }
}