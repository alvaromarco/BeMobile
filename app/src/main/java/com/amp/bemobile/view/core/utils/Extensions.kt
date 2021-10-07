package com.amp.bemobile.view.core.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }

inline fun <T : ViewBinding> ViewGroup.viewBinding(viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> T) =
    viewBindingFactory.invoke(LayoutInflater.from(this.context), this, false)
