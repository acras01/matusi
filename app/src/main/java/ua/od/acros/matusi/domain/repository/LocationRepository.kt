package ua.od.acros.matusi.domain.repository

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    suspend fun getCurrentLocation(): LatLng?
    suspend fun getLocationFromAddress(address: String): LatLng?
    suspend fun getAddressFromLocation(latLng: LatLng): String?
}