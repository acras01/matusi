package ua.od.acros.matusi.presentation.settings.fragment

import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentUserAccountBinding
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel
import java.lang.StringBuilder

class UserAccountFragment (private val sharedViewModel: UserSettingsViewModel):
    BaseSettingsFragment<FragmentUserAccountBinding, UserSettingsViewModel>(
        R.layout.fragment_user_account,
        FragmentUserAccountBinding::inflate,
        sharedViewModel
    ) {

    private var currentParent: Parent? = null

    override fun setUi() {

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            })

        with(binding) {
            btnEditLocation.setOnClickListener {
                sharedViewModel.applyIntent(
                    ViewIntent.OpenLocationIntent(
                        R.id.action_userAccountFragment_to_userSettingsUserLocationFragment, true
                    )
                )
            }
            btnEditRadius.setOnClickListener {
                sharedViewModel.applyIntent(
                    ViewIntent.OpenLocationIntent(
                        R.id.action_userAccountFragment_to_userSettingsUserLocationRadiusFragment,
                        true
                    )
                )
            }
            btnEditChildren.setOnClickListener {
                sharedViewModel.applyIntent(
                    ViewIntent.OpenLocationIntent(
                        R.id.action_userAccountFragment_to_userSettingsAddChildrenFragment, true
                    )
                )
            }
            btnEditSchedule.setOnClickListener {
                sharedViewModel.applyIntent(
                    ViewIntent.OpenLocationIntent(
                        R.id.action_userAccountFragment_to_userSettingsUserScheduleFragment, true
                    )
                )
            }
        }
    }

    override fun performAction(action: ViewAction) {
        when(action) {
            is ViewAction.OpenLocationAction -> {
                findNavController().navigate(action.action, bundleOf("from" to action.from))
            }
            else -> {}
        }
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.CurrentParentState -> {
                currentParent = state.parent
                currentParent?.let {
                    val parent = it
                    sharedViewModel.applyIntent(ViewIntent.GetParentAddressIntent(parent.location))
                    with(binding) {
                        tvRadius.text = getString(R.string.radius, parent.radius?.div(1000) ?: 0)
                        val kids = parent.kids
                        var text = StringBuilder(getString(R.string.children))
                        kids.forEach {
                            text.append(it.toStringCustom())
                            if (kids.indexOf(it) != kids.size - 1)
                                text.append("\n")
                        }
                        tvChildren.text = text.toString()
                        val schedule = parent.schedule
                        text = StringBuilder(getString(R.string.schedule))
                        schedule.forEach {
                            text.append(it.toStringCustom())
                            if (it.checked && (schedule.indexOf(it) != schedule.size - 1))
                                text.append("\n")
                        }
                        tvSchedule.text = text.toString()
                    }
                } ?: run {
                        val parent = sharedViewModel.currentUser?.uid?.let {
                            Parent(
                                it,
                                "",
                                "",
                                "",
                                "",
                                "",
                                null,
                                0,
                                arrayListOf(),
                                0,
                                false,
                                arrayListOf()
                            )
                        }
                    sharedViewModel.applyIntent(ViewIntent.SetCurrentParentIntent(parent))
                    findNavController().navigate(R.id.action_userAccountFragment_to_userSettingsNameAndSocialFragment)
                }
            }
            is ViewState.ParentLocationState -> {
                binding.tvLocation.text = getString(R.string.location, state.address)
            }
            else -> {}
        }
    }
}