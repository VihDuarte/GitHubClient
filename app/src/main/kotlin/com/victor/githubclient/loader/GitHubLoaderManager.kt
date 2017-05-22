package com.victor.githubclient.loader

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.app.LoaderManager.LoaderCallbacks

object GitHubLoaderManager {
    fun <T> init(manager: LoaderManager, loaderId: Int,
                 loader: GitHubLoader<T>, callback: Callback<T>) {
        manager.initLoader(loaderId, Bundle.EMPTY, object : LoaderCallbacks<Response<T>> {
            override fun onCreateLoader(id: Int, args: Bundle): android.support.v4.content.Loader<Response<T>> {
                return loader
            }

            override fun onLoadFinished(loader: android.support.v4.content.Loader<Response<T>>, data: Response<T>) {
                data.result?.let { callback.onSuccess(it) }
                data.exception?.let { callback.onFailure(it) }

                manager.destroyLoader(loaderId)
            }

            override fun onLoaderReset(loader: android.support.v4.content.Loader<Response<T>>) {
                //Nothing to do here
            }
        })
    }
}
