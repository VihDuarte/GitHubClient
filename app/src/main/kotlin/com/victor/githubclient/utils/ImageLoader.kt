package com.victor.githubclient.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView

object ImageLoader {
    private var cache: MutableMap<String, Bitmap> = mutableMapOf()
    private var downloads: MutableList<DownloadImageTask> = arrayListOf()

    fun loadImage(url: String?, into: ImageView, placeHolder: Int) {
        if (cache[url] != null) {
            into.setImageBitmap(cache[url])
        } else {
            into.setImageResource(placeHolder)

            val item = DownloadImageTask(into)
            item.execute(url)

            if (!downloads.isEmpty()) {
                downloads.filter { it.bmImage.equals(into) }.forEach {
                    it.cancel(true)
                    downloads.remove(it)
                }
            }

            downloads.add(item)
        }
    }

    fun addCache(url: String, image: Bitmap) {
        cache.put(url, image)
    }
}

class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {
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
            bmImage.setImageBitmap(result)
            ImageLoader.addCache(url, result)
        }
    }
}
