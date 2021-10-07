package com.amp.bemobile.view.core.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), Scope by Scope.Impl() {

    init {
        initScope()
    }

    @CallSuper
    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}