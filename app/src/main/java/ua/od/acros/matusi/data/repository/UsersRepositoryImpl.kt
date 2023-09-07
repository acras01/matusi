package ua.od.acros.matusi.data.repository

import ua.od.acros.matusi.data.datasource.DbDataSource
import ua.od.acros.matusi.data.datasource.FirestoreDataSource
import ua.od.acros.matusi.data.mapper.*
import ua.od.acros.matusi.data.mapper.fromLocation
import ua.od.acros.matusi.data.mapper.toKidsList
import ua.od.acros.matusi.data.mapper.toLocation
import ua.od.acros.matusi.data.mapper.toScheduleList
import ua.od.acros.matusi.data.model.ParentDto
import ua.od.acros.matusi.domain.model.Parent
import ua.od.acros.matusi.domain.repository.UsersRepository

class UsersRepositoryImpl(
    private val firestoreDataSource: FirestoreDataSource,
    private val dbDataSource: DbDataSource,
    private val parentDtoMapper: ParentDtoMapper,
    private val parentMapper: ParentMapper
): UsersRepository {
    override suspend fun saveUser(collection: String, id: String, parent: Parent) {
        dbDataSource.insertParent(parentDtoMapper.map(parent))
        val map = hashMapOf<String, Any?>(
            "id" to parent.id,
            "nickname" to parent.nickname,
            "facebook" to parent.facebook,
            "skype" to parent.skype,
            "viber" to parent.viber,
            "twitter" to parent.twitter,
            "location" to parent.location?.fromLocation(),
            "radius" to parent.radius,
            "kids" to parent.kids.toJsonString(),
            "karma" to parent.karma,
            "readyToJoin" to parent.readyToJoin,
            "schedule" to parent.schedule.toJsonString()
        )
        firestoreDataSource.insertDocumentToDatabaseById(collection, id, map)
    }

    override suspend fun getUser(collection: String, id: String): Parent? {
        var parent = dbDataSource.getParentById(id)
        if (parent == null) {
            val userMap = firestoreDataSource.getDocumentFromCollectionById(collection, id)
            if (userMap != null) {
                val nick = userMap["nickname"] as String
                val fb = userMap["facebook"] as String
                val sky = userMap["skype"] as String
                val vib = userMap["viber"] as String
                val twi = userMap["twitter"] as String
                val location = (userMap["location"] as String).toLocation()
                val radius = userMap["radius"] as Long
                val kids = (userMap["kids"] as String).toKidsList()
                val karma = userMap["karma"] as Long
                val join = userMap["readyToJoin"] as Boolean
                val schedule = (userMap["schedule"] as String).toScheduleList()
                parent = ParentDto(
                    id = id,
                    nickname = nick,
                    facebook = fb,
                    skype = sky,
                    viber = vib,
                    twitter = twi,
                    location = location,
                    radius = radius,
                    kids = kids,
                    karma = karma,
                    readyToJoin = join,
                    schedule = schedule
                )
            }
        }
        return parent?.let { parentMapper.map(it) }
    }

    override suspend fun removeUser(collection: String, id: String, parent: Parent) {
        dbDataSource.removeParent(parentDtoMapper.map(parent))
        firestoreDataSource.removeDocumentFromDatabaseById(collection, id)
    }
}