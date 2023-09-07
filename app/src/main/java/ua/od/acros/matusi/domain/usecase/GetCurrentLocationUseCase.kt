package ua.od.acros.matusi.domain.usecase

import ua.od.acros.matusi.domain.repository.LocationRepository

class GetCurrentLocationUseCase (private val locationRepository: LocationRepository){
    suspend fun invoke() = locationRepository.getCurrentLocation()
}
