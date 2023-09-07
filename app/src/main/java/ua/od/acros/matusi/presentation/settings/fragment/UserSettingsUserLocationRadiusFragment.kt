package ua.od.acros.matusi.presentation.settings.fragment

import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.PatternItem
import com.google.android.material.slider.Slider
import com.google.maps.android.ktx.awaitMap
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentUserLocationRadiusBinding
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.presentation.misc.*
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel

class UserSettingsUserLocationRadiusFragment (private val sharedViewModel: UserSettingsViewModel):
    BaseSettingsFragment<FragmentUserLocationRadiusBinding, UserSettingsViewModel>(
        R.layout.fragment_user_location_radius,
        FragmentUserLocationRadiusBinding::inflate,
        sharedViewModel
    ) {

    private lateinit var googleMap: GoogleMap

    private var redraw = false

    private var currentParent: Parent? = null
    private var currentLocation: LatLng? = null
    private var currentRadius: Long? = null

    private var from = false

    override fun setUi(){
        from = arguments?.getBoolean("from") ?: false

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val act = if (from)
                        R.id.action_global_userAccountFragment
                    else
                        R.id.action_userSettingsUserLocationRadiusFragment_to_userSettingsUserLocationFragment
                    findNavController().navigate(act)
                }
            })

        with(binding) {
            val limit = activity?.resources?.getIntArray(R.array.limit)!!
            activity?.let {
                val adapter = ArrayAdapter<Int>(it, android.R.layout.simple_spinner_item, limit.toList())
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spLimit.adapter = adapter
            }

            spLimit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (::googleMap.isInitialized) {
                        redraw = false
                        val bound = (limit[position] * 2 * KM).toDouble()
                        val builder = LatLngBounds.builder()
                        currentLocation?.let { location ->
                            var temp = EllipticalMercator
                                .newLatLngAtDistance(location, bound)
                            builder.include(temp)
                            temp = EllipticalMercator
                                .newLatLngAtDistance(location, -bound)
                            builder.include(temp)
                        }
                        val bounds = builder.build()
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
                        seekRadius.valueTo = limit[position].toFloat()
                        redraw = true
                    }
                }
            }

            seekRadius.addOnChangeListener(object : Slider.OnChangeListener{
                override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                    if (::googleMap.isInitialized && redraw) {
                        val radius = value.toInt() * KM
                        currentLocation?.let {
                            addMarker(it, radius)
                        }
                        currentRadius = radius
                    }
                }
            })

            btnNext.clicks().subscribe {
                currentParent?.radius = currentRadius
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
                val radius = currentRadius
                if (location != null && radius != null) {
                    with(binding) {
                        spLimit.post {
                            spLimit.setSelection(
                                when {
                                    radius / KM > 100 -> 5
                                    radius / KM > 50 -> 4
                                    radius / KM > 25 -> 3
                                    radius / KM > 10 -> 2
                                    radius / KM > 5 -> 1
                                    else -> 0
                                })
                            seekRadius.valueTo = (spLimit.selectedItem as Int).toFloat()
                            seekRadius.value = (radius / KM).toFloat()
                        }
                        addMarker(location, radius)
                        btnNext.isEnabled = true
                    }
                }
            }
        }
    }

    private fun addMarker(location: LatLng, radius: Long) {
        with(googleMap) {
            clear()
            addMarker(
                MarkerOptions()
                    .title(currentParent?.nickname)
                    .position(location)
            )
            val pattern: List<PatternItem> = listOf<PatternItem>(Dot())
            addCircle(
                CircleOptions()
                    .center(location)
                    .radius((radius).toDouble())
                    .fillColor(R.color.brand_color_secondary_transparent)
                    .strokeColor(R.color.brand_color_primary)
                    .strokePattern(pattern)
            )
        }
    }

    override fun renderState(state: ViewState) {
        when(state) {
            is ViewState.CurrentParentState -> {
                currentParent = state.parent
                if (currentParent != null) {
                    val location = currentParent?.location
                    val radius = currentParent?.radius
                    if (location != null) {
                        currentLocation = location
                        if (radius != null) {
                            currentRadius = radius
                            if (::googleMap.isInitialized)
                                addMarker(location, radius)
                            binding.btnNext.isEnabled = true
                        }
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
                    R.id.action_userSettingsUserLocationRadiusFragment_to_userSettingsAddChildrenFragment
                findNavController().navigate(act)
            }
            else -> {}
        }
    }

    companion object {
        private const val KM = 1000L
    }
}