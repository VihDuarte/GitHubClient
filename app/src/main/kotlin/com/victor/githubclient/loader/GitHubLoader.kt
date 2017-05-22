package com.victor.githubclient.loader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

abstract class GitHubLoader<T>(context: Context) : AsyncTaskLoader<Response<T>>(context) {

    private var cachedResponse: Response<T>? = null

    override fun loadInBackground(): Response<T>? {
        try {
            val data = call()
            cachedResponse = Response.ok(data)
        } catch (ex: Exception) {
            cachedResponse = Response.error<T>(ex)
        }

        return cachedResponse
    }

    override fun onStartLoading() {
        super.onStartLoading()

        cachedResponse?.let { deliverResult(cachedResponse) }

        if (takeContentChanged() || cachedResponse == null) {
            forceLoad()
        }
    }

    override fun onReset() {
        super.onReset()
        cachedResponse = null
    }

    abstract fun call(): T
}
