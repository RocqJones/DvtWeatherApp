package com.rocqjones.dvt.weatherapp.logic.models.sealed

/**
 * Explicitly handles Success & Failure of our API call.
 * This helps prevent potential runtime exceptions or unexpected behavior.
 */
sealed class IResult<out R> {
    data class Success<out T>(val data: T) : IResult<T>()
    data class Error(val throwable: Throwable) : IResult<Nothing>()
}