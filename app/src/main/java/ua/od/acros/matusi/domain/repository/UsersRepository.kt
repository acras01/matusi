package ua.od.acros.matusi.domain.repository

import ua.od.acros.matusi.domain.model.Parent

interface UsersRepository {
    suspend fun saveUser(collection: String, id: String, parent: Parent)
    suspend fun getUser(collection: String, id: String): Parent?
    suspend fun removeUser(collection: String, id: String, parent: Parent)
}