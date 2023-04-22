package com.arda.mainapp.auth

import java.lang.Exception

sealed class Resource<out R> {
    data class Sucess<out R>(val result:R): Resource<R>()
    data class Failure<out R>(val exception: Exception): Resource<Nothing>()
    object Loading : Resource<Nothing>()
}