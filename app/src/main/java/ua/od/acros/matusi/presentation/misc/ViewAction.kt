package ua.od.acros.matusi.presentation.misc

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import ua.od.acros.matusi.domain.model.CareSchedule

sealed class ViewAction {
    object IdleAction: ViewAction()
    object NextFragmentAction: ViewAction()
    data class ShowTimePickerAction(val schedule: CareSchedule, val period: String) : ViewAction()
    data class GoogleAuthAction(val client: GoogleSignInClient) : ViewAction()
    data class FirebaseAuthAction(val result: FirebaseUser?) : ViewAction()
    data class OpenLocationAction(val action: Int, val from: Boolean = false) : ViewAction()
}