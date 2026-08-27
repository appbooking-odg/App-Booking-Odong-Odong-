package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.data.local.entity.DriverEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers ORDER BY id ASC")
    fun getAllDrivers(): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers")
    suspend fun getAllDriversList(): List<DriverEntity>

    @Query("SELECT * FROM drivers WHERE user = :username AND pass = :password LIMIT 1")
    suspend fun findDriver(username: String, password: String): DriverEntity?

    @Query("SELECT * FROM drivers WHERE id = :id LIMIT 1")
    suspend fun getDriverById(id: Long): DriverEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriver(driver: DriverEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrivers(drivers: List<DriverEntity>)

    @Update
    suspend fun updateDriver(driver: DriverEntity)

    @Query("UPDATE drivers SET izin = :izin WHERE id = :id")
    suspend fun updateIzin(id: Long, izin: String)

    @Query("UPDATE drivers SET aktif = :aktif WHERE id = :id")
    suspend fun updateAktif(id: Long, aktif: Boolean)

    @Query("DELETE FROM drivers WHERE id = :id")
    suspend fun deleteDriverById(id: Long)

    @Delete
    suspend fun deleteDriver(driver: DriverEntity)
}
