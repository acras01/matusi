package ua.od.acros.matusi.data.datasource.impl

import androidx.room.withTransaction
import ua.od.acros.matusi.data.datasource.DbDataSource
import ua.od.acros.matusi.data.db.AppDatabase
import ua.od.acros.matusi.data.model.ParentDto

class DbDataSourceImpl (private val db: AppDatabase): DbDataSource {

    override suspend fun insertParent(parent: ParentDto) =
        db.withTransaction {
            db.parentDao().insert(parent)
        }

    override suspend fun getParentById(id: String) = db.parentDao().getById(id)

    override suspend fun removeParent(parent: ParentDto) =
        db.withTransaction {
            db.parentDao().delete(parent)
        }
}