package ua.od.acros.matusi.domain.usecase

import com.google.android.gms.maps.model.LatLng
import ua.od.acros.matusi.domain.repository.LocationRepository

class GetAddressFromLocationUseCase (private val locationRepository: LocationRepository) {
    suspend fun invoke(latLng: LatLng) = locationRepository.getAddressFromLocation(latLng)
}
