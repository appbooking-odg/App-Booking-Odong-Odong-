package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pengirimPeran: String, // "pelanggan", "driver", "operator", "admin"
    val pengirimNama: String,
    val pesan: String,
    val waktu: String,
    val timestamp: Long = System.currentTimeMillis()
)
