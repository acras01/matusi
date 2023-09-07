package ua.od.acros.matusi.presentation.main.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ua.od.acros.matusi.R
import ua.od.acros.matusi.presentation.main.fragment.composables.FindUser
import ua.od.acros.matusi.presentation.main.fragment.composables.MainMenu
import ua.od.acros.matusi.presentation.main.vm.MainApplicationViewModel
import ua.od.acros.matusi.presentation.misc.AskPermissionDialogFragment
import ua.od.acros.matusi.presentation.misc.LocationPermissionManager

class MainMenuFragment: Fragment(), LocationPermissionManager {

    private val mainViewModel: MainApplicationViewModel by lazy { getViewModel() }

    private val locationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (permissionGranted in result.values) {
                allAreGranted = allAreGranted && permissionGranted
            }
            mainViewModel.setLocationPermission(allAreGranted)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                val navController = rememberNavController()

                val brandColor = colorResource(id = R.color.brand_color_primary)
                val whiteColor = colorResource(id = R.color.white)

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainMenu(
                            modifier = Modifier
                                .background(brandColor)
                        ) {
                            navController.navigate("find_user")
                        }
                    }
                    composable("find_user") {
                        FindUser(
                            modifier = Modifier
                                .background(whiteColor)
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        checkForLocationPermission(prefs.getBoolean("show_dialog", false))
    }

    override fun checkForLocationPermission(check: Boolean) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mainViewModel.setLocationPermission(true)
        } else if (!check) {
            val dialog = AskPermissionDialogFragment()
            dialog.show(childFragmentManager, "AskPermissionDialogFragment")
        }
    }

    override fun askForLocationPermission() {
        val appPerms = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        locationResultLauncher.launch(appPerms)
    }
}