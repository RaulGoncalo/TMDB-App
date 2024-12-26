package com.rgos_developer.tmdbapp.presentation

interface BaseView {
    fun showLoading(type: String = "")
    fun hideLoading(type: String = "")
    fun initViews()
}