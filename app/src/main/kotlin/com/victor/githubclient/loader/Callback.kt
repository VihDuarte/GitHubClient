package com.victor.githubclient.loader

interface Callback<T> {

    fun onFailure(ex: Exception)

    fun onSuccess(result: T)
}
