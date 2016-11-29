package com.victor.githubclient.loader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader


abstract class GitHubLoader<D>(context: Context) : AsyncTaskLoader<Response<D>>(context) {

    private var mCachedResponse: Response<D>? = null

    override fun loadInBackground(): Response<D>? {
        try {

            val data = call()
            mCachedResponse = Response.ok(data)

        } catch (ex: Exception) {

            mCachedResponse = Response.error<D>(ex)
        }

        return mCachedResponse
    }

    override fun onStartLoading() {

        super.onStartLoading()

        if (mCachedResponse != null) {

            deliverResult(mCachedResponse)
        }

        if (takeContentChanged() || mCachedResponse == null) {

            forceLoad()
        }
    }

    override fun onReset() {

        super.onReset()

        mCachedResponse = null
    }

    abstract fun call(): D
}
