package com.victor.githubclient.loader

class Response<T> {

    var exception: Exception? = null
        private set

    var result: T? = null
        private set

    fun hasError(): Boolean {

        return exception != null
    }

    companion object {

        fun <T> ok(data: T): Response<T> {

            val response = Response<T>()
            response.result = data

            return response
        }

        fun <T> error(ex: Exception): Response<T> {

            val response = Response<T>()
            response.exception = ex

            return response
        }
    }
}
