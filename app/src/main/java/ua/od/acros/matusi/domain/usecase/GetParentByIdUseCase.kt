package ua.od.acros.matusi.domain.usecase

import ua.od.acros.matusi.domain.repository.UsersRepository

class GetParentByIdUseCase (private val usersRepository: UsersRepository) {
    suspend fun invoke(collection: String, id: String) = usersRepository.getUser(collection, id)
}
