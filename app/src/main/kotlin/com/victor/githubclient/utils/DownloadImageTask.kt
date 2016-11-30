package com.victor.githubclient.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView

/**
 * Created by victor on 30/11/16.
 */

class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap {
        val url = urls[0]

        val openStream = java.net.URL(url).openStream()
        val icon = BitmapFactory.decodeStream(openStream)

        return icon
    }

    override fun onPostExecute(result: Bitmap) {
        bmImage.setImageBitmap(result)
    }
}
