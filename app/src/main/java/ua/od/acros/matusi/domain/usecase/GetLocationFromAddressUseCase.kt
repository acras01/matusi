package ua.od.acros.matusi.domain.usecase

import ua.od.acros.matusi.domain.repository.LocationRepository

class GetLocationFromAddressUseCase (private val locationRepository: LocationRepository){
    suspend fun invoke(address: String) = locationRepository.getLocationFromAddress(address)
}
