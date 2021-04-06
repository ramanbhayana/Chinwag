package com.app.chinwag.api.network

interface TaskFinished<T> {
    fun onTaskFinished(data: T)
}