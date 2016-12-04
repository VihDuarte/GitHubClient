package com.victor.githubclient.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView

class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap? {
        try {
            val url = urls[0]

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
        }
    }
}
