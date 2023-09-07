package ua.od.acros.matusi.presentation.settings.fragment

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.databinding.FragmentUserLocationBinding
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel

class UserSettingsUserLocationFragment (
    private val sharedViewModel: UserSettingsViewModel
): BaseSettingsFragment<FragmentUserLocationBinding, UserSettingsViewModel>(
    R.layout.fragment_user_account,
    FragmentUserLocationBinding::inflate,
    sharedViewModel
), LocationPermissionManager {

    private val onMapClickListener = GoogleMap.OnMapClickListener { latLng ->
        sharedViewModel.applyIntent(ViewIntent.SetCurrentLocationIntent(latLng))
    }

    private val locationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for (permissionGranted in result.values) {
                allAreGranted = allAreGranted && permissionGranted
            }
            sharedViewModel.setLocationPermission(allAreGranted)
        }

    private lateinit var googleMap: GoogleMap

    private var currentParent: Parent? = null
    private var currentLocation: LatLng? = null

    private var from = false

    override fun setUi(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        checkForLocationPermission(prefs.getBoolean("show_dialog", false))

        from = arguments?.getBoolean("from") ?: false

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val act = if (from)
                        R.id.action_global_userAccountFragment
                    else
                        R.id.action_userSettingsUserLocationFragment_to_userSettingsNameAndSocialFragment
                    findNavController().navigate(act)
                }
            })

        with(binding) {
            btnNext.isEnabled = false

            btnEnterAddress.clicks().subscribe {
                val address = binding.etEnterAddress.text.toString()
                sharedViewModel.applyIntent(ViewIntent.SetLocationFromAddressIntent(address))
            }

            btnCurrentLocation.isEnabled = sharedViewModel.locationPerm == true
            btnCurrentLocation.clicks().subscribe {
                sharedViewModel.applyIntent(ViewIntent.GetCurrentLocationIntent)
            }

            btnNext.clicks().subscribe {
                sharedViewModel.applyIntent(ViewIntent.SaveCurrentParentIntent(currentParent))
            }
        }

        lifecycleScope.launchWhenCreated {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            val map = mapFragment?.awaitMap()
            if (map != null) {
                googleMap = map
                val location = currentLocation
                if (location != null) {
                    addMarker(location)
                    binding.btnNext.isEnabled = true
                }
                googleMap.setOnMapClickListener(onMapClickListener)
            }
        }
    }

    private fun addMarker(location: LatLng) {
        with(googleMap) {
            clear()
            addMarker(
                MarkerOptions()
                    .title(currentParent?.nickname)
                    .position(location)
            )
            val builder = LatLngBounds.builder()
            builder.include(
                EllipticalMercator
                    .newLatLngAtDistance(location, (100 * 1000).toDouble()))
            builder.include(
                EllipticalMercator
                    .newLatLngAtDistance(location, (-100 * 1000).toDouble()))
            val bounds = builder.build()
            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
        }
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.CurrentParentState -> {
                currentParent = state.parent
                if (currentParent != null) {
                    currentLocation = currentParent?.location
                    if (currentLocation != null && ::googleMap.isInitialized) {
                        addMarker(currentLocation ?: LatLng(0.0, 0.0))
                        binding.btnNext.isEnabled = true
                    }
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

    override fun checkForLocationPermission(check: Boolean) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            sharedViewModel.setLocationPermission(true)
        } else if (!check) {
            val dialog = AskPermissionDialogFragment()
            dialog.show(childFragmentManager, "AskPermissionDialogFragment")
        }
    }

    override fun askForLocationPermission() {
        val appPerms = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
        locationResultLauncher.launch(appPerms)
    }
}