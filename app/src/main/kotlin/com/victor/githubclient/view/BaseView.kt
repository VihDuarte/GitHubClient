package com.victor.githubclient.view


interface BaseView {

    fun showProgress()

    fun hideProgress()

    fun showError()

    fun hideError()

    fun cleanData()
}
