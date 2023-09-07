package ua.od.acros.matusi.presentation.misc

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T> BaseViewModel.networkExecutor(networkBuilder: NetworkBuilder<T>.() -> Unit) {
    viewModelScope.networkOperation(networkBuilder)
}

fun <T> CoroutineScope.networkOperation(networkBuilder: NetworkBuilder<T>.() -> Unit) {
    this.launch {
        val builder = NetworkBuilder<T>().apply { networkBuilder() }
        try {
            builder.builderStartLambda?.invoke()

            builder.builderLoadingLiveData?.value = true

            val result = builder.builderApiCall?.invoke(this)!!

            builder.builderSuccess.invoke(result)


        } catch (e: CustomError) {

            //TODO can add here handler to resolve issues with network etc
            builder.builderErrorLambda?.invoke(this, e)
        } finally {
            builder.builderFinishLambda?.invoke()
            builder.builderLoadingLiveData?.value = false
        }
    }
}