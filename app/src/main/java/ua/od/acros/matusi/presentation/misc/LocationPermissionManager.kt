package ua.od.acros.matusi.presentation.misc

interface LocationPermissionManager {
    fun checkForLocationPermission(check: Boolean)
    fun askForLocationPermission()
}