package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.data.local.entity.AdminEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins ORDER BY id ASC")
    fun getAllAdmins(): Flow<List<AdminEntity>>

    @Query("SELECT * FROM admins")
    suspend fun getAllAdminsList(): List<AdminEntity>

    @Query("SELECT * FROM admins WHERE user = :username AND pass = :password LIMIT 1")
    suspend fun findAdmin(username: String, password: String): AdminEntity?

    @Query("SELECT * FROM admins WHERE id = :id LIMIT 1")
    suspend fun getAdminById(id: Long): AdminEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmins(admins: List<AdminEntity>)

    @Update
    suspend fun updateAdmin(admin: AdminEntity)

    @Query("UPDATE admins SET pass = :newPass WHERE id = :id")
    suspend fun updatePassword(id: Long, newPass: String)

    @Query("UPDATE admins SET aktif = :aktif WHERE id = :id")
    suspend fun updateAktif(id: Long, aktif: Boolean)

    @Query("DELETE FROM admins WHERE id = :id")
    suspend fun deleteAdminById(id: Long)
}
