package ua.od.acros.matusi.presentation.authorization.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentAuthorizationBinding
import ua.od.acros.matusi.presentation.misc.ActionBarManager

class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        with(binding) {
            tvTitle.movementMethod = ScrollingMovementMethod()

            btnExistingUser.clicks().subscribe {
                (activity as ActionBarManager).removeActionBarLogo()
                findNavController().navigate(R.id.action_authorizationFragment_to_signInFragment)
            }

            btnNewUser.clicks().subscribe {
                (activity as ActionBarManager).removeActionBarLogo()
                findNavController().navigate(R.id.action_authorizationFragment_to_registerFragment)
            }

            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onResume() {
        super.onResume()
        (activity as ActionBarManager).setActionBarLogo(R.drawable.logo_matusi_small)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}