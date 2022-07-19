package com.androiddevs.mvvmnewsapp.ui.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    //Getting data if Success
    class Success<T>(data: T) : Resource<T>(data)
    //Will give error message if Error
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    //Returned when our request was fired off, waiting for Success or Error
    class Loading<T> : Resource<T>()
}