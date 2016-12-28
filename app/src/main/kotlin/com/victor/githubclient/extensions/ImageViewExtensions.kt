package com.victor.githubclient.extensions

import android.widget.ImageView
import com.victor.githubclient.interactor.DownloadImageTask
import com.victor.githubclient.interactor.ImageLoader

fun ImageView.loadImage(url: String?, placeHolder: Int) {

    if (!ImageLoader.downloads.isEmpty()) {
        ImageLoader.downloads.filter { it.imageView.equals(this) }.forEach {
            it.cancel(true)
            ImageLoader.downloads.remove(it)
        }
    }

    if (ImageLoader.cache[url] != null) {
        this.setImageBitmap(ImageLoader.cache[url])
    } else {
        this.setImageResource(placeHolder)

        val item = DownloadImageTask(this)
        item.execute(url)

        ImageLoader.downloads.add(item)
    }

}

