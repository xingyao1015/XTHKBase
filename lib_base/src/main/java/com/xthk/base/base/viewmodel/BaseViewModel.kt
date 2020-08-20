package com.xthk.base.base.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xthk.base.base.entity.BaseEntity
import kotlinx.coroutines.*

class BaseViewModel : ViewModel(), LifecycleObserver {

    val isLoading by lazy { MutableLiveData<Boolean>() }

    val isEmpty by lazy { MutableLiveData<Boolean>() }

    val error by lazy { MutableLiveData<Throwable>() }


    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    fun launch(showLoading: Boolean = true, tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            if (showLoading) {
                isLoading.postValue(true)
            }
            tryCatch(tryBlock)
            if (showLoading) {
                isLoading.postValue(false)
            }
        }
    }


    suspend fun <T : Any?> apiCall(call: suspend () -> BaseEntity<T>): BaseEntity<T> {
        return call.invoke()
    }


    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                isLoading.value = false
                if (e !is CancellationException) {
                    e.printStackTrace()
                    error.value = e
                }
            }
        }
    }
}