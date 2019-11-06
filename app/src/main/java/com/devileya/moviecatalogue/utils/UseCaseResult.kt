package com.devileya.moviecatalogue.utils

/**
 * Created by Arif Fadly Siregar 2019-11-01.
 */
sealed class UseCaseResult<out T : Any?> {
    class Success<out T : Any>(val data: T) : UseCaseResult<T>()
    class Error(val exception: Throwable) : UseCaseResult<Nothing>()
}