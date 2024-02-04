package com.rocqjones.dvt.weatherapp.logic.executors

import retrofit2.Response

// An interface for asynchronous executions
interface IExecute<T> {
    fun run(result: Response<T?>?, t: Throwable?)
}