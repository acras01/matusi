package ua.od.acros.matusi.data.repository

import com.google.android.gms.maps.model.LatLng
import ua.od.acros.matusi.data.datasource.LocationDataSource
import ua.od.acros.matusi.domain.repository.LocationRepository

class LocationRepositoryImpl (private val locationDataSource: LocationDataSource): LocationRepository {
    override suspend fun getCurrentLocation() = locationDataSource.getCurrentLocation()
    override suspend fun getLocationFromAddress(address: String) = locationDataSource.getLocationFromAddress(address)
    override suspend fun getAddressFromLocation(latLng: LatLng) = locationDataSource.getAddressFromLocation(latLng)
}