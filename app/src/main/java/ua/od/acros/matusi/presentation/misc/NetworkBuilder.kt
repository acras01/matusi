package ua.od.acros.matusi.presentation.misc

import kotlinx.coroutines.CoroutineScope

class NetworkBuilder<T> {
    internal var builderLoadingLiveData : SingleLiveEvent<Boolean>? = null
    internal var builderApiCall: (suspend CoroutineScope.() -> T)? = null
    internal var builderStartLambda: (() -> Unit)? = null
    internal var builderFinishLambda: (() -> Unit)? = null
    internal var builderSuccess: ((result: T) -> Unit) = {}
    internal var builderErrorLambda: (suspend CoroutineScope.(CustomError) -> Unit)? = null
}

fun <T> NetworkBuilder<T>.execute(execution: suspend CoroutineScope.() -> T) {
    this.builderApiCall = execution
}

fun <T> NetworkBuilder<T>.error(error: suspend CoroutineScope.(CustomError) -> Unit) {
    this.builderErrorLambda = error
}

fun <T> NetworkBuilder<T>.onFinish(onFinish: () -> Unit) {
    this.builderFinishLambda = onFinish
}

fun <T> NetworkBuilder<T>.onStart(onStart: () -> Unit) {
    this.builderStartLambda = onStart
}

fun <T> NetworkBuilder<T>.success(success: (T) -> Unit) {
    this.builderSuccess = success
}

fun <T> NetworkBuilder<T>.loading(loading: SingleLiveEvent<Boolean>){
    this.builderLoadingLiveData = loading
}
