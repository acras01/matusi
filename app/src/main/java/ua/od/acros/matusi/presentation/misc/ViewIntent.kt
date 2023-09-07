package ua.od.acros.matusi.presentation.misc

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.AuthCredential
import ua.od.acros.matusi.domain.model.CareSchedule
import ua.od.acros.matusi.domain.model.Child
import ua.od.acros.matusi.domain.model.Parent

sealed class ViewIntent {
    data class SetCurrentParentIntent(val parent: Parent?) : ViewIntent()
    data class SaveCurrentParentIntent(val parent: Parent?) : ViewIntent()
    data class SetCurrentLocationIntent(val latLng: LatLng) : ViewIntent()
    data class SetLocationFromAddressIntent(val address: String) : ViewIntent()
    data class RemoveChildIntent(val childId: String) : ViewIntent()
    data class AddChildIntent(val child: Child) : ViewIntent()
    data class ShowTimePickerIntent(val schedule: CareSchedule, val period: String) : ViewIntent()
    data class UpdateScheduleIntent(val schedule: CareSchedule) : ViewIntent()
    data class GetParentAddressIntent(val latLng: LatLng?) : ViewIntent()
    object GetCurrentParentIntent : ViewIntent()
    object IdleIntent : ViewIntent()
    object GetCurrentLocationIntent : ViewIntent()
    //Authorization
    data class GoogleAuthIntent(val client: GoogleSignInClient) : ViewIntent()
    data class FirebaseGoogleAuthIntent(val credential: AuthCredential) : ViewIntent()
    data class FirebaseEmailAuthIntent(val email: String, val password: String) : ViewIntent()
    data class CreateAccountIntent(val email: String, val password: String) : ViewIntent()
    data class OpenLocationIntent(val action: Int, val from: Boolean = false) : ViewIntent()
    object EmailSignInIntent : ViewIntent()
}