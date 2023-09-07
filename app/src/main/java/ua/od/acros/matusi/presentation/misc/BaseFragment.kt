package ua.od.acros.matusi.presentation.misc

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, out VM : BaseViewModel>(
    @LayoutRes layoutRes: Int,
    private val bindingInflater: (inflater: LayoutInflater) -> VB,
    private val viewModel: VM
) : Fragment(layoutRes), ActionView, StateView {

    private var _binding: VB? = null
    val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackPressedCallback()
        setActionBarMenu()
        setObservers()
        setUi()
    }

    private fun setActionBarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                when (menuItem.itemId) {
//                    android.R.id.home -> {
//                        lifecycleScope.launch {
//                            sharedViewModel.userIntent.send(ViewIntent.BackPressedIntent(from))
//                        }
//                        return true
//                    }
//                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setBackPressedCallback() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    renderState(state)
                    viewModel.applyIntent(ViewIntent.IdleIntent)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect { action ->
                    performAction(action)
                    viewModel.applyIntent(ViewIntent.IdleIntent)
                }
            }
        }
    }

    protected open fun setUi() = Unit

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun performAction(action: ViewAction) = Unit

    override fun renderState(state: ViewState) = Unit
}