package ua.od.acros.matusi.data.db.dao

import androidx.room.*
import ua.od.acros.matusi.data.model.ParentDto
import ua.od.acros.matusi.domain.model.Parent

@Dao
interface ParentDao {

    @Query("SELECT * FROM parents")
    suspend fun getAll(): List<ParentDto?>?

    @Query("SELECT * FROM parents WHERE id = :id")
    suspend fun getById(id: String): ParentDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parent: ParentDto?)

    @Delete
    suspend fun delete(parent: ParentDto?)
}