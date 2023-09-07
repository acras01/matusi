package ua.od.acros.matusi.presentation.authorization.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import kotlinx.coroutines.launch
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentRegisterBinding
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.misc.showSnackbar
import ua.od.acros.matusi.presentation.authorization.vm.AuthorizationViewModel

class RegisterFragment (private val sharedViewModel: AuthorizationViewModel):
    BaseFragment<FragmentRegisterBinding, AuthorizationViewModel>(
        R.layout.fragment_register,
        FragmentRegisterBinding::inflate,
        sharedViewModel
    ) {

    override fun setUi() {
        with(binding) {
            val constraintSet = ConstraintSet()
            tvTerms?.movementMethod = ScrollingMovementMethod()
            btnRegister.isEnabled = false
            cbTerms.setOnCheckedChangeListener { _, p1 ->
                btnRegister.isEnabled = p1
            }
            btnExpand?.clicks()?.subscribe {
                btnCollapse?.visibility = View.VISIBLE
                btnExpand.visibility = View.GONE
                tvTerms?.visibility = View.VISIBLE
                constraintSet.clone(binding.cl1)
                btnCollapse?.id?.let { collapse ->
                    cbTerms.id.let { terms ->
                        constraintSet.connect(
                            terms,
                            ConstraintSet.END,
                            collapse,
                            ConstraintSet.START,
                            8
                        )
                    }
                }
                constraintSet.applyTo(cl1)
            }
            btnCollapse?.clicks()?.subscribe {
                btnCollapse.visibility = View.GONE
                btnExpand?.visibility = View.VISIBLE
                tvTerms?.visibility = View.GONE
                constraintSet.clone(binding.cl1)
                btnExpand?.id?.let { expand ->
                    cbTerms.id.let { terms ->
                        constraintSet.connect(
                            terms,
                            ConstraintSet.END,
                            expand,
                            ConstraintSet.START,
                            8
                        )
                    }
                }
                constraintSet.applyTo(cl1)
            }

            tvTermsLand?.clicks()?.subscribe {
                val dialog = AlertDialog.Builder(activity)
                    .setTitle(R.string.terms_of_use_title)
                    .setMessage(R.string.terms_of_use)
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .create()
                dialog.show()
            }
            btnRegister.clicks().subscribe {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val rePassword = etRepassword.text.toString()
                val equal = etPassword.text.toString() == etRepassword.text.toString()
                if (TextUtils.isEmpty(email))
                    etEmail.error = getString(R.string.field_empty)
                else if (TextUtils.isEmpty(password))
                    etPassword.error = getString(R.string.field_empty)
                else if (TextUtils.isEmpty(rePassword))
                    etRepassword.error = getString(R.string.field_empty)
                else if (!equal) {
                    etPassword.error = getString(R.string.password_not_equal)
                    etRepassword.error = getString(R.string.password_not_equal)
                } else
                    sharedViewModel.applyIntent(ViewIntent.CreateAccountIntent(email, password))
            }

            etPassword.doOnTextChanged { text, _, _, _ ->
                validatePassword(text.toString(), etRepassword.text.toString())
            }
            etRepassword.doOnTextChanged { text, _, _, _ ->
                validatePasswordConfirm(etPassword.text.toString(), text.toString())
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
                    showSnackbar(binding.root, R.string.authorization_failed)
            }
            else -> {}
        }
    }

    override fun performAction(action: ViewAction) {
        when (action) {
            is ViewAction.FirebaseAuthAction -> {
                if (action.result != null)
                    findNavController().navigate(R.id.action_global_userSettings)
                else
                    findNavController().navigate(R.id.action_global_authorizationFragment)
            }
            else -> {}
        }
    }

    private fun validatePassword(passwordInput: String, confirmPass: String) {
        val passwordsEquals = passwordInput == confirmPass
        val passwordIsEmpty = passwordInput == ""

        with(binding) {
            if (passwordIsEmpty) {
                ilPassword.boxStrokeColor = ContextCompat
                    .getColor(requireActivity(), R.color.brand_color_grey)
                ilPassword.hintTextColor = ContextCompat
                    .getColorStateList(requireActivity(), R.color.brand_color_grey)
            } else {
                ilPassword.boxStrokeColor = Color.GREEN
                ilPassword.hintTextColor = ContextCompat
                    .getColorStateList(requireActivity(), android.R.color.holo_green_dark)
            }

            btnRegister.isEnabled = passwordsEquals
        }
    }

    private fun validatePasswordConfirm(passwordInput: String, confirmPass: String) {
        val passwordsEquals = passwordInput == confirmPass
        val passwordIsEmpty = confirmPass == ""

        with(binding) {
            if (passwordIsEmpty) {
                ilRepassword.boxStrokeColor = ContextCompat
                    .getColor(requireActivity(), R.color.brand_color_grey)
                ilRepassword.hintTextColor = ContextCompat
                    .getColorStateList(requireActivity(), R.color.brand_color_grey)
            } else {
                if (!passwordsEquals) {
                    ilRepassword.boxStrokeColor = Color.RED
                    ilRepassword.hintTextColor = ContextCompat
                        .getColorStateList(
                            requireActivity(), android.R.color.holo_red_dark)
                    ilRepassword.hint = getString(R.string.passwords_not_match)
                } else {
                    ilRepassword.boxStrokeColor = Color.GREEN
                    ilRepassword.hintTextColor = ContextCompat
                        .getColorStateList(
                            requireActivity(), android.R.color.holo_green_dark)
                    ilRepassword.hint = getString(R.string.passwords_match)
                }
            }
            btnRegister.isEnabled = passwordsEquals
        }
    }
}