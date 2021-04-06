package com.app.chinwag.commonUtils.permissions

interface ResponsePermissionCallback {
    fun onResult(permissionResult: List<String>)
}