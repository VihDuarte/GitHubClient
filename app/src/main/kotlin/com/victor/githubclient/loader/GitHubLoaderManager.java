package com.victor.githubclient.loader;

import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.os.Bundle;

public class GitHubLoaderManager {

    public static <D> void init(final LoaderManager manager, final int loaderId,
                                   final GitHubLoader<D> loader, final Callback<D> callback) {
        manager.initLoader(loaderId, Bundle.EMPTY, new LoaderCallbacks<Response<D>>() {
            @Override
            public android.support.v4.content.Loader<Response<D>> onCreateLoader(int id, Bundle args) {
                return loader;
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<Response<D>> loader, Response<D> data) {
                if (data.hasError()) {
                    callback.onFailure(data.getException());
                } else {
                    callback.onSuccess(data.getResult());
                }

                manager.destroyLoader(loaderId);
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<Response<D>> loader) {
                //Nothing to do here
            }
        });
    }
}
