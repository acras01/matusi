package ua.od.acros.matusi.presentation.authorization.vm

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ua.od.acros.matusi.presentation.misc.BaseViewModel
import ua.od.acros.matusi.presentation.misc.ViewAction
import ua.od.acros.matusi.presentation.misc.ViewIntent
import ua.od.acros.matusi.presentation.misc.ViewState
import kotlin.coroutines.suspendCoroutine

class AuthorizationViewModel(private val firebaseAuth: FirebaseAuth): BaseViewModel() {

    private val _currentUser: Lazy<FirebaseUser?> = lazy {
        firebaseAuth.currentUser
    }
    val currentUser: FirebaseUser? get() = _currentUser.value

    override suspend fun processIntent(intent: ViewIntent) {
        when (intent) {
            is ViewIntent.IdleIntent -> {
                _state.value = ViewState.IdleState
                _action.value = ViewAction.IdleAction
            }
            is ViewIntent.GoogleAuthIntent -> {
                _state.value = ViewState.IdleState
                _action.value = ViewAction.GoogleAuthAction(intent.client)
            }
            is ViewIntent.FirebaseGoogleAuthIntent -> {
                val result = getResult(
                    firebaseAuth.signInWithCredential(intent.credential)
                )
                _state.value = ViewState.FirebaseAuthState(result)
                _action.value = ViewAction.FirebaseAuthAction(result)
            }
            is ViewIntent.EmailSignInIntent -> {
                _state.value = ViewState.IdleState
                _action.value = ViewAction.NextFragmentAction
            }
            is ViewIntent.FirebaseEmailAuthIntent -> {
                val result = getResult(
                    firebaseAuth.signInWithEmailAndPassword(intent.email, intent.password)
                )
                _state.value = ViewState.FirebaseAuthState(result)
                _action.value = ViewAction.FirebaseAuthAction(result)
            }
            is ViewIntent.CreateAccountIntent -> {
                val result = getResult(
                    firebaseAuth.createUserWithEmailAndPassword(intent.email, intent.password)
                )
                _state.value = ViewState.FirebaseAuthState(result)
                _action.value = ViewAction.FirebaseAuthAction(result)
            }
            else -> {}
        }
    }

    private suspend fun getResult(firebaseTask: Task<AuthResult>): FirebaseUser? =
        suspendCoroutine<FirebaseUser?> { cont ->
            firebaseTask.addOnCompleteListener { task ->
                    val user = firebaseAuth.currentUser
                    if (task.isSuccessful && user !== null) {
                        cont.resumeWith(Result.success((user)))
                    } else {
                        cont.resumeWith(Result.success((null)))
                    }
                }
        }
}