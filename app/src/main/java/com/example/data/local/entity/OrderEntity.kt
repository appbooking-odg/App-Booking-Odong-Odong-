package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val hp: String,
    val asal: String,
    val tujuan: String,
    val almtAsal: String = "",
    val almtTujuan: String = "",
    val jumlah: Int = 1,
    val hargaDasar: Int = 0,
    val hargaTawar: Int = 0,
    val totalAkhir: Int = 0,
    val tgl: String = "",
    val jam: String = "",
    val bayar: String = "cash", // "cash", "transfer", "qris"
    val driverPilihan: String = "Semua Driver",
    val status: String = "menunggu", // "menunggu", "diterima", "ditolak"
    val oleh: String = "", // driver name or operator
    val waktu: Long = System.currentTimeMillis(),
    val catatan: String = ""
)
