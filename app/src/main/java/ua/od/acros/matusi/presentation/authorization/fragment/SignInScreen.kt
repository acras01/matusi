package ua.od.acros.matusi.presentation.authorization.fragment

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.core.context.unloadKoinModules
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentSignInBinding
import ua.od.acros.matusi.di.authorizationModule
import ua.od.acros.matusi.presentation.misc.ViewAction
import ua.od.acros.matusi.presentation.misc.ViewIntent
import ua.od.acros.matusi.presentation.misc.ViewState

@Composable
fun SignInScreen(
    navHostController: NavHostController,
    currentState: State<ViewState>,
    currentAction: State<ViewAction>,
    navigateToSettings: () -> Unit,
    applyIntent: (ViewIntent) -> Unit,
    showSnackbar: (String) -> Unit
) {

    val context = LocalContext.current

    val googleAuthorizationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken.orEmpty(), null)
                applyIntent(ViewIntent.FirebaseGoogleAuthIntent(credential))
            } catch (e: ApiException) {
                showSnackbar(context.getString(R.string.login_failed))
            }
        } else
            showSnackbar(context.getString(R.string.login_failed))
    }

    AndroidViewBinding(FragmentSignInBinding::inflate) {

        val signInWithGoogle: (client: GoogleSignInClient) -> Unit = { client ->
            val signInIntent = client.signInIntent
            googleAuthorizationLauncher.launch(signInIntent)
        }

        btnEmailSignIn.setOnClickListener {
            applyIntent(ViewIntent.EmailSignInIntent)
        }

        btnGoogleSignIn.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            applyIntent(ViewIntent.GoogleAuthIntent(googleSignInClient))
        }

        when(val state = currentState.value) {
            is ViewState.FirebaseAuthState -> {
                if (state.result != null) {
                    val user = state.result
                    showSnackbar(
                        context.getString(R.string.welcome, user.displayName)
                    )
                } else
                    showSnackbar(
                        context.getString(R.string.login_failed)
                    )
            }
            else -> {}
        }

        when (val action = currentAction.value) {
            is ViewAction.GoogleAuthAction -> {
                signInWithGoogle(action.client)
            }
            is ViewAction.FirebaseAuthAction -> {
                if (action.result != null) {
                    navigateToSettings()
                    unloadKoinModules(authorizationModule)
                } else
                    navHostController.navigate("authorize")
            }
            is ViewAction.NextFragmentAction -> {
                navHostController.navigate("email_sign_in")
            }
            else -> {}
        }
    }
}