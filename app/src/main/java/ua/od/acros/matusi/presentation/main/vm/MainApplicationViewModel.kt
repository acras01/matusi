package ua.od.acros.matusi.presentation.main.vm

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ua.od.acros.matusi.presentation.misc.BaseViewModel
import ua.od.acros.matusi.presentation.misc.ViewIntent

class MainApplicationViewModel(private val firebaseAuth: FirebaseAuth): BaseViewModel() {

    private var _locationPerm: Boolean = false
    val locationPerm: Boolean get() = _locationPerm
    fun setLocationPermission(permission: Boolean) {
        _locationPerm = permission
    }

    private val _currentUser: Lazy<FirebaseUser?> = lazy {
        firebaseAuth.currentUser
    }
    val currentUser: FirebaseUser? get() = _currentUser.value

    private var _isSetupFinished: Boolean = false
    val isSetupFinished: Boolean get() = _isSetupFinished
    fun setSetupFinished(finished: Boolean) {
        _isSetupFinished = finished
    }

    override suspend fun processIntent(intent: ViewIntent) {
        TODO("Not yet implemented")
    }
}