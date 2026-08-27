package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val asal: String,
    val tujuan: String,
    val harga: Int,
    val estimasi: String = "± 45-60 Menit"
)
