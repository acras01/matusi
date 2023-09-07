package ua.od.acros.matusi.data.datasource

import ua.od.acros.matusi.data.model.ParentDto

interface DbDataSource {
        suspend fun insertParent(parent: ParentDto)
        suspend fun getParentById(id: String): ParentDto?
        suspend fun removeParent(parent: ParentDto)
}