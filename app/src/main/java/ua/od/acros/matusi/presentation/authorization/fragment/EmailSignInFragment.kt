package ua.od.acros.matusi.presentation.authorization.fragment

import android.text.TextUtils
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import org.koin.core.context.GlobalContext.unloadKoinModules
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentEmailSignInBinding
import ua.od.acros.matusi.di.authorizationModule
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.misc.showSnackbar
import ua.od.acros.matusi.presentation.authorization.vm.AuthorizationViewModel

class EmailSignInFragment (private val sharedViewModel: AuthorizationViewModel):
    BaseFragment<FragmentEmailSignInBinding, AuthorizationViewModel>(
        R.layout.fragment_email_sign_in,
        FragmentEmailSignInBinding::inflate,
        sharedViewModel
) {
    override fun setUi() {
        with(binding) {
            btnSignIn.clicks().subscribe {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
                    sharedViewModel.applyIntent(ViewIntent.FirebaseEmailAuthIntent(email, password))
                else
                    showSnackbar(binding.root, R.string.fill_all_fields)
            }
        }
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
            is ViewAction.FirebaseAuthAction -> {
                if (action.result != null) {
                    findNavController().navigate(R.id.action_global_userSettings)
                    unloadKoinModules(authorizationModule)
                } else
                    findNavController().navigate(R.id.action_global_authorizationFragment)
            }
            else -> {}
        }
    }
}