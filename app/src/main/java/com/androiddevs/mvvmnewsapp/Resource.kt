package com.androiddevs.mvvmnewsapp

//recommended generic class to wrap around network responses
//useful to differentiate between successful and error requests
//sealed classes lets you allow which classes are allowed to inherit from that class

sealed class Resource<T> (
    val data : T ?= null,
    val message: String?= null)
{

    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String, data: T? = null) :Resource<T>(data,message)

    class Loading<T> : Resource<T>()
}