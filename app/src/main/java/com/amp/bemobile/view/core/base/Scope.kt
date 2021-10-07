package com.amp.bemobile.view.core.base

import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

interface Scope : CoroutineScope {

    var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + getJobErrorHandler()

    fun initScope() {
        job = SupervisorJob()
    }

    fun destroyScope() {
        job.cancel()
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, exception ->
        Timber.e("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }

    class Impl : Scope {
        override lateinit var job: Job
    }
}