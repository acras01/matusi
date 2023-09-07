package ua.od.acros.matusi.presentation.settings.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import ua.od.acros.matusi.presentation.misc.BaseFragment
import ua.od.acros.matusi.presentation.misc.BaseViewModel
import ua.od.acros.matusi.presentation.misc.ViewIntent

abstract class BaseSettingsFragment<VB : ViewBinding, VM : BaseViewModel>(
    @LayoutRes layoutRes: Int,
    private val bindingInflater: (inflater: LayoutInflater) -> VB,
    private val viewModel: VM
) : BaseFragment<VB, VM>(layoutRes, bindingInflater, viewModel) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchInitialIntent()
    }

    private fun launchInitialIntent() {
        viewModel.applyIntent(ViewIntent.GetCurrentParentIntent)
    }
}