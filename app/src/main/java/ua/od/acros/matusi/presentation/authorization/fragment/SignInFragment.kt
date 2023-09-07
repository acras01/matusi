package ua.od.acros.matusi.presentation.authorization.fragment

import android.app.Activity.RESULT_OK
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jakewharton.rxbinding4.view.clicks
import org.koin.core.context.unloadKoinModules
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentSignInBinding
import ua.od.acros.matusi.di.authorizationModule
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.authorization.vm.AuthorizationViewModel

class SignInFragment (private val sharedViewModel: AuthorizationViewModel):
    BaseFragment<FragmentSignInBinding, AuthorizationViewModel>(
        R.layout.fragment_sign_in,
        FragmentSignInBinding::inflate,
        sharedViewModel
    ) {

    private val googleAuthorizationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showSnackbar(binding.root, R.string.login_failed)
            }
        } else
            showSnackbar(binding.root, R.string.login_failed)
    }

    override fun setUi() {
        with(binding) {
            btnEmailSignIn.clicks().subscribe {
                sharedViewModel.applyIntent(ViewIntent.EmailSignInIntent)
            }

            btnGoogleSignIn.clicks().subscribe {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                sharedViewModel.applyIntent(ViewIntent.GoogleAuthIntent(googleSignInClient))
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        sharedViewModel.applyIntent(ViewIntent.FirebaseGoogleAuthIntent(credential))
    }

    private fun signInWithGoogle(client: GoogleSignInClient) {
        val signInIntent = client.signInIntent
        googleAuthorizationLauncher.launch(signInIntent)
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.FirebaseAuthState -> {
                if (state.result != null) {
                    val user = state.result
                    showSnackbar(
                        binding.root,
                        getString(R.string.welcome, user.displayName)
                    )
                } else
                    showSnackbar(binding.root, R.string.login_failed)
            }
            else -> {}
        }
    }

    override fun performAction(action: ViewAction) {
        when (action) {
            is ViewAction.GoogleAuthAction -> {
                signInWithGoogle(action.client)
            }
            is ViewAction.FirebaseAuthAction -> {
                if (action.result != null) {
                    findNavController().navigate(R.id.action_global_userSettings)
                    unloadKoinModules(authorizationModule)
                } else
                    findNavController().navigate(R.id.action_global_authorizationFragment)
            }
            is ViewAction.NextFragmentAction -> {
                findNavController().navigate(R.id.action_signInFragment_to_emailSignInFragment)
            }
            else -> {}
        }
    }
}