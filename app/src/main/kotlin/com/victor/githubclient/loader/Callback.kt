package com.victor.githubclient.loader

interface Callback<in T> {

    fun onFailure(ex: Exception)

    fun onSuccess(result: T)
}
