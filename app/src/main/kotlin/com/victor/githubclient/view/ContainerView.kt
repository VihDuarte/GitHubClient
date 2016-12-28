package com.victor.githubclient.view

import android.support.v4.app.Fragment


interface ContainerView {
    fun showDetail(fragment: Fragment, title: String)
}