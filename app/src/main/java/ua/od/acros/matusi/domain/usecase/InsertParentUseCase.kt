package ua.od.acros.matusi.domain.usecase

import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.domain.repository.UsersRepository

class InsertParentUseCase (private val usersRepository: UsersRepository) {
    suspend fun invoke(collection: String, id: String, parent: Parent) = usersRepository.saveUser(collection, id, parent)
}
