package ua.od.acros.matusi.presentation.misc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    private val _userIntent = Channel<ViewIntent>(Channel.UNLIMITED)
    val userIntent = _userIntent.receiveAsFlow()

    protected val _state = MutableStateFlow<ViewState>(ViewState.IdleState)
    val state: StateFlow<ViewState> = _state

    protected val _action = MutableStateFlow<ViewAction>(ViewAction.IdleAction)
    val action: StateFlow<ViewAction> = _action

    protected abstract suspend fun processIntent(intent: ViewIntent)

    val _loading = SingleLiveEvent<Boolean>()

    val error = SingleLiveEvent<CustomError>()

    init {
        viewModelScope.launch {
            _userIntent.consumeEach { intent ->
                processIntent(intent)
            }
        }
    }

    fun applyIntent(intent: ViewIntent) = viewModelScope.launch {
        _userIntent.send(intent)
    }
}