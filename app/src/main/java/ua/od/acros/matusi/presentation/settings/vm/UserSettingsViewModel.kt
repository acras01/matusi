package ua.od.acros.matusi.presentation.settings.vm

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ua.od.acros.matusi.domain.model.CareSchedule
import ua.od.acros.matusi.domain.model.Child
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.domain.usecase.*
import ua.od.acros.matusi.presentation.misc.*

class UserSettingsViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getLocationFromAddressUseCase: GetLocationFromAddressUseCase,
    private val getAddressFromLocationUseCase: GetAddressFromLocationUseCase,
    private val getParentBuIdUseCase: GetParentByIdUseCase,
    private val insertParentUseCase: InsertParentUseCase,
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    private var _locationPerm: Boolean = false
    val locationPerm: Boolean get() = _locationPerm
    fun setLocationPermission(permission: Boolean) {
        _locationPerm = permission
    }

    private val _currentUser: Lazy<FirebaseUser?> = lazy {
        firebaseAuth.currentUser
    }
    val currentUser: FirebaseUser? get() = _currentUser.value

    private var _currentParent: Parent? = null

   override fun onCleared() {
        super.onCleared()
    }

    private fun requestCurrentLocation() {
        networkExecutor<LatLng?> {
            execute {
                getCurrentLocationUseCase.invoke()
            }
            success {
                _currentParent = _currentParent?.copy(location = it)
                _state.value = ViewState.CurrentParentState(_currentParent)
            }
            error { _state.value = ViewState.ErrorState(it) }
            loading(_loading)
        }
    }

    private fun getLocationFromAddress(address: String) {
        networkExecutor<LatLng?> {
            execute {
                getLocationFromAddressUseCase.invoke(address)
            }
            success {
                _currentParent = _currentParent?.copy(location = it)
                _state.value = ViewState.CurrentParentState(_currentParent)
            }
            error { _state.value = ViewState.ErrorState(it) }
            loading(_loading)
        }
    }

    private fun getAddressFromLocation(latLng: LatLng?) {
        networkExecutor<String?> {
            execute {
                latLng?.let { getAddressFromLocationUseCase.invoke(it) }
            }
            success {
                _state.value = ViewState.ParentLocationState(it)
            }
            error { _state.value = ViewState.ErrorState(it) }
            loading(_loading)
        }
    }

    private fun addChild(child: Child) {
        var childList = _currentParent?.kids
        if (childList != null) {
            val oldChild = childList.find { it.id == child.id }
            if (oldChild == null) {
                childList.add(child)
            } else {
                val ind = childList.indexOf(oldChild)
                removeChild(child.id)
                childList.add(ind, child)
            }
        } else {
            childList = ArrayList(listOf(child))
        }
        _currentParent = _currentParent?.copy(kids = childList)
        _state.value = ViewState.CurrentParentState(_currentParent)
    }

    private fun removeChild(id: String) {
        val childList = _currentParent?.kids
        if (childList != null) {
            val child = childList.first { it.id == id }
            childList.remove(child)
            _currentParent = _currentParent?.copy(kids = childList)
            _state.value = ViewState.CurrentParentState(_currentParent)
        }
    }

    private fun replaceSchedule(currentDay: CareSchedule) {
        var temp = _currentParent?.schedule
        if (temp != null) {
            if (temp.isNotEmpty()) {
                val index = currentDay.day - 1
                temp.removeAt(index)
                temp.add(index, currentDay)
            } else
                temp.add(currentDay)
        } else {
            temp = arrayListOf<CareSchedule>()
            temp.add(currentDay)
        }
        _currentParent = _currentParent?.copy(schedule = temp)
        _state.value = ViewState.CurrentParentState(_currentParent)
    }

    private fun getCurrentParent() {
        networkExecutor<Parent?> {
            execute {
                _currentUser.value?.uid?.let { uid ->
                    getParentBuIdUseCase.invoke("parents", uid)
                }
            }
            success {
                _state.value = ViewState.CurrentParentState(_currentParent)
            }
            error { _state.value = ViewState.ErrorState(it) }
            loading(_loading)
        }
    }

    private fun saveParent(parent: Parent) {
        networkExecutor<Unit> {
            execute {
                _currentUser.value?.uid?.let {
                    insertParentUseCase.invoke("parents", it, parent)
                }
            }
            success {
                _state.value = ViewState.IdleState
            }
            error { _state.value = ViewState.ErrorState(it) }
            loading(_loading)
        }
    }

    override suspend fun processIntent(intent: ViewIntent) {
        when (intent) {
            is ViewIntent.GetCurrentParentIntent -> {
                getCurrentParent()
            }
            is ViewIntent.SetCurrentParentIntent -> {
                _currentParent = intent.parent
                _action.value = ViewAction.NextFragmentAction
            }
            is ViewIntent.SaveCurrentParentIntent -> {
                _currentParent = intent.parent
                intent.parent?.let { parent -> saveParent(parent) }
                _action.value = ViewAction.NextFragmentAction
            }
            is ViewIntent.GetCurrentLocationIntent -> {
                _state.value = ViewState.IdleState
                requestCurrentLocation()
            }
            is ViewIntent.ShowTimePickerIntent -> {
                _state.value = ViewState.IdleState
                _action.value = ViewAction.ShowTimePickerAction(intent.schedule, intent.period)
            }
            is ViewIntent.SetCurrentLocationIntent -> {
                _currentParent = _currentParent?.copy(location = intent.latLng)
                _state.value = ViewState.CurrentParentState(_currentParent)
            }
            is ViewIntent.SetLocationFromAddressIntent -> {
                getLocationFromAddress(intent.address)
            }
            is ViewIntent.AddChildIntent -> {
                addChild(intent.child)
            }
            is ViewIntent.RemoveChildIntent -> {
                removeChild(intent.childId)
            }
            is ViewIntent.UpdateScheduleIntent -> {
                replaceSchedule(intent.schedule)
            }
            is ViewIntent.GetParentAddressIntent -> {
                getAddressFromLocation(intent.latLng)
            }
            is ViewIntent.OpenLocationIntent -> {
                _state.value = ViewState.IdleState
                _action.value = ViewAction.OpenLocationAction(intent.action, intent.from)
            }
            is ViewIntent.IdleIntent -> {
                _state.value = ViewState.IdleState
                _action.value = ViewAction.IdleAction
            }
            else -> {}
        }
    }
}
