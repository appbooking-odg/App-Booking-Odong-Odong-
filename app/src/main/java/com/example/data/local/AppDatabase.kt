package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.AdminDao
import com.example.data.local.dao.ChatMessageDao
import com.example.data.local.dao.DriverDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.dao.RouteDao
import com.example.data.local.entity.AdminEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.DriverEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.RouteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        RouteEntity::class,
        DriverEntity::class,
        AdminEntity::class,
        OrderEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
    abstract fun driverDao(): DriverDao
    abstract fun adminDao(): AdminDao
    abstract fun orderDao(): OrderDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "odong_lombok_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val routeDao = database.routeDao()
            val driverDao = database.driverDao()
            val adminDao = database.adminDao()
            val orderDao = database.orderDao()
            val chatDao = database.chatMessageDao()

            if (routeDao.getAllRoutesList().isEmpty()) {
                val defaultRoutes = listOf(
                    RouteEntity(asal = "Narmada", tujuan = "Senggigi", harga = 150000, estimasi = "± 45 Menit"),
                    RouteEntity(asal = "Senggigi", tujuan = "Narmada", harga = 150000, estimasi = "± 45 Menit"),
                    RouteEntity(asal = "Mataram", tujuan = "Lombok Timur", harga = 500000, estimasi = "± 90 Menit"),
                    RouteEntity(asal = "Mataram", tujuan = "Pantai Kuta Mandalika", harga = 400000, estimasi = "± 75 Menit"),
                    RouteEntity(asal = "Ampenan", tujuan = "Senggigi Sunset Point", harga = 120000, estimasi = "± 30 Menit"),
                    RouteEntity(asal = "Cakranegara", tujuan = "Pelabuhan Bangsal Gili", harga = 350000, estimasi = "± 60 Menit"),
                    RouteEntity(asal = "Praya", tujuan = "Sade & Sukarara Village", harga = 250000, estimasi = "± 40 Menit"),
                    RouteEntity(asal = "Lombok Barat", tujuan = "Air Terjun Benang Stokel", harga = 300000, estimasi = "± 60 Menit")
                )
                routeDao.insertRoutes(defaultRoutes)
            }

            if (driverDao.getAllDriversList().isEmpty()) {
                val defaultDrivers = listOf(
                    DriverEntity(
                        nama = "Pak Suardi Lombok",
                        hp = "081907951938",
                        user = "driver",
                        pass = "dr123",
                        izin = "ya",
                        aktif = true,
                        platNomor = "DR 1945 AB"
                    ),
                    DriverEntity(
                        nama = "Mas Ahmad Cakranegara",
                        hp = "082230336091",
                        user = "ahmad",
                        pass = "dr123",
                        izin = "ya",
                        aktif = true,
                        platNomor = "DR 8821 LK"
                    ),
                    DriverEntity(
                        nama = "Bang Deni Senggigi",
                        hp = "087865432100",
                        user = "deni",
                        pass = "dr123",
                        izin = "tidak",
                        aktif = true,
                        platNomor = "DR 4512 ZZ"
                    )
                )
                driverDao.insertDrivers(defaultDrivers)
            }

            if (adminDao.getAllAdminsList().isEmpty()) {
                val defaultAdmins = listOf(
                    AdminEntity(nama = "Admin Utama Lombok", user = "admin", pass = "admin123", aktif = true),
                    AdminEntity(nama = "Operator Dispatch", user = "operator", pass = "op123", aktif = true)
                )
                adminDao.insertAdmins(defaultAdmins)
            }

            // Initial demo sample order for immediate preview rich experience
            val defaultOrders = listOf(
                OrderEntity(
                    nama = "Ibu Nurhayati",
                    hp = "081803214567",
                    asal = "Narmada",
                    tujuan = "Senggigi",
                    almtAsal = "Jl. Wisata Narmada No. 12 (Depan Taman)",
                    almtTujuan = "Pantai Senggigi Hotel Merumatta",
                    jumlah = 2,
                    hargaDasar = 150000,
                    hargaTawar = 140000,
                    totalAkhir = 280000,
                    tgl = "2026-08-27",
                    jam = "09:00",
                    bayar = "cash",
                    driverPilihan = "Semua Driver",
                    status = "menunggu",
                    oleh = "",
                    waktu = System.currentTimeMillis() - 1000 * 60 * 15,
                    catatan = "Rombongan arisan keluarga 16 orang, minta lampu hias malam dan sound system dangdut/pop"
                ),
                OrderEntity(
                    nama = "Bpk. Wayan Sukadana",
                    hp = "087765123980",
                    asal = "Mataram",
                    tujuan = "Pantai Kuta Mandalika",
                    almtAsal = "Mataram Mall Parkir Timur",
                    almtTujuan = "Bazar Mandalika Lombok Tengah",
                    jumlah = 1,
                    hargaDasar = 400000,
                    hargaTawar = 0,
                    totalAkhir = 400000,
                    tgl = "2026-08-27",
                    jam = "13:30",
                    bayar = "qris",
                    driverPilihan = "Pak Suardi Lombok",
                    status = "diterima",
                    oleh = "Pak Suardi Lombok",
                    waktu = System.currentTimeMillis() - 1000 * 60 * 60,
                    catatan = "Tour keliling sirkuit mandalika & pantai kuta"
                )
            )
            orderDao.insertOrders(defaultOrders)

            val initialChats = listOf(
                ChatMessageEntity(
                    pengirimPeran = "operator",
                    pengirimNama = "Operator Dispatch",
                    pesan = "Selamat datang di Layanan Odong-Odong Wisata Lombok! Siap melayani carter & tour rombongan.",
                    waktu = "08:00",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 120
                ),
                ChatMessageEntity(
                    pengirimPeran = "driver",
                    pengirimNama = "Pak Suardi Lombok",
                    pesan = "Armada Odong-Odong 01 siap meluncur, bersih, sound system jernih dan berlampu led.",
                    waktu = "08:15",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 90
                )
            )
            initialChats.forEach { chatDao.insertMessage(it) }
        }
    }
}
