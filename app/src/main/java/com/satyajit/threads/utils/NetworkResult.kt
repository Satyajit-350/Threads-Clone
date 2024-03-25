package com.satyajit.threads.utils

sealed class NetworkResult<T>(var data: T? = null, var message: String? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data,null)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()

}