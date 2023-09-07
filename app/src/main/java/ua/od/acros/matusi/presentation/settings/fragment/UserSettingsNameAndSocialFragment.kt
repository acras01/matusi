package ua.od.acros.matusi.presentation.settings.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.databinding.FragmentUserSettingsNameAndSocialBinding
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel

class UserSettingsNameAndSocialFragment (private val sharedViewModel: UserSettingsViewModel):
    BaseSettingsFragment<FragmentUserSettingsNameAndSocialBinding, UserSettingsViewModel>(
        R.layout.fragment_user_settings_name_and_social,
        FragmentUserSettingsNameAndSocialBinding::inflate,
        sharedViewModel
    ) {

    private var currentParent: Parent? = null

    private var from = false

    override fun setUi() {
        from = arguments?.getBoolean("from") ?: false

        with(binding) {
            btnNext.clicks().subscribe {
                if (TextUtils.isEmpty(etName.text))
                    etName.error = getString(R.string.field_empty)
                else if (!TextUtils.isEmpty(etFB.text) ||
                    !TextUtils.isEmpty(etSky.text) ||
                    !TextUtils.isEmpty(etVib.text) ||
                    !TextUtils.isEmpty(etTwi.text)) {
                    saveParent()
                }
                else
                    showSnackbar(root, R.string.fill_social)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveParent()
    }

    private fun saveParent() {
        with(binding) {
            if (currentParent != null) {
                currentParent?.nickname = etName.text.toString()
                currentParent?.facebook = etFB.text.toString()
                currentParent?.skype = etSky.text.toString()
                currentParent?.viber = etVib.text.toString()
                currentParent?.twitter = etTwi.text.toString()
            } else {
                currentParent = Parent(
                    sharedViewModel.currentUser?.uid!!,
                    etName.text.toString(),
                    etFB.text.toString(),
                    etSky.text.toString(),
                    etVib.text.toString(),
                    etTwi.text.toString(),
                    null,
                    0,
                    arrayListOf(),
                    0,
                    false,
                    arrayListOf()
                )
            }
        }
        sharedViewModel.applyIntent(ViewIntent.SetCurrentParentIntent(currentParent))
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.CurrentParentState -> {
                currentParent = state.parent
                if (currentParent != null) {
                    binding.parent = currentParent
                    binding.executePendingBindings()
                }
            }
            else -> {}
        }
    }

    override fun performAction(action: ViewAction) {
        when (action) {
            is ViewAction.NextFragmentAction -> {
                val act = if (from)
                    R.id.action_global_userAccountFragment
                else
                    R.id.action_userSettingsLocationFragment_to_userSettingsUserLocationRadiusFragment
                findNavController()
                    .navigate(act)
            }
            else -> {}
        }
    }
}