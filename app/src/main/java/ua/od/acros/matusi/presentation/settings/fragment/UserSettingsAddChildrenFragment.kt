package ua.od.acros.matusi.presentation.settings.fragment

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentUserSettingsAddChildrenBinding
import ua.od.acros.matusi.domain.model.Child
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.settings.adapter.ChildAdapter
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel

class UserSettingsAddChildrenFragment (private val sharedViewModel: UserSettingsViewModel):
    BaseSettingsFragment<FragmentUserSettingsAddChildrenBinding, UserSettingsViewModel>(
        R.layout.fragment_user_settings_user_schedule,
        FragmentUserSettingsAddChildrenBinding::inflate,
        sharedViewModel
    ) {

    private var childAdapter: ChildAdapter? = null

    private var currentParent: Parent? = null

    private var from = false

    override fun setUi() {

        from = arguments?.getBoolean("from") ?: false

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val act = if (from)
                        R.id.action_global_userAccountFragment
                    else
                        R.id.action_userSettingsAddChildrenFragment_to_userSettingsUserLocationRadiusFragment
                    findNavController().navigate(act)
                }
            })

        with(binding) {

            val onAddChild: (Child) -> Unit = {
                sharedViewModel.applyIntent(ViewIntent.AddChildIntent(child = it))
            }

            val onRemoveChild: (String) -> Unit = {
                sharedViewModel.applyIntent(ViewIntent.RemoveChildIntent(childId = it))
            }

            childAdapter = ChildAdapter(onAddChild, onRemoveChild)
            with(rvChildren) {
                val lManager = LinearLayoutManager(activity)
                layoutManager = lManager
                adapter = childAdapter
            }

            btnNext.clicks().subscribe {
                currentParent = childAdapter?.children?.let { list -> currentParent?.copy(kids = list) }
                sharedViewModel.applyIntent(ViewIntent.SaveCurrentParentIntent(currentParent))
            }
        }
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.CurrentParentState -> {
                currentParent = state.parent
                val list = currentParent?.kids
                if (list != null && list.isNotEmpty()) {
                    childAdapter?.children = list
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
                findNavController().navigate(act)
            }
            else -> {}
        }
    }
}