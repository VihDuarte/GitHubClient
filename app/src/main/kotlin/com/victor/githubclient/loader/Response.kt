package com.victor.githubclient.loader

data class Response<out T>(val exception: Exception?, val result: T?) {
    fun hasError(): Boolean {
        return exception != null
    }

    companion object {
        inline fun <T> ok(data: T): Response<T> {
            return Response(null, data)
        }

        inline fun <T> error(ex: Exception): Response<T> {
            return Response<T>(ex, null)
        }
    }
}
