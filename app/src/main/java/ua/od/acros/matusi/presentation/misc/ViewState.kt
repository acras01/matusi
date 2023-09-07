package ua.od.acros.matusi.presentation.misc

import com.google.firebase.auth.FirebaseUser
import ua.od.acros.matusi.domain.model.Parent

sealed class ViewState {
    object IdleState : ViewState()
    data class CurrentParentState(val parent: Parent?) : ViewState()
    data class FirebaseAuthState(val result: FirebaseUser?) : ViewState()
    data class ParentLocationState(val address: String?) : ViewState()
    data class ErrorState(val error: CustomError) : ViewState()
}