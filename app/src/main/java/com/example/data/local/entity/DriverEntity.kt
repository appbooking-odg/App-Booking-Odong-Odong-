package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val hp: String,
    val user: String,
    val pass: String,
    val izin: String = "ya", // "ya" or "tidak"
    val aktif: Boolean = true,
    val platNomor: String = "DR 1945 AB"
)
