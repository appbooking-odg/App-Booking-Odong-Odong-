package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AdminEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.DriverEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.RouteEntity
import com.example.data.repository.OdongRepository
import com.example.ui.viewmodel.BookingDraftState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class OdongOdongAppTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: OdongRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = OdongRepository(
            routeDao = db.routeDao(),
            driverDao = db.driverDao(),
            adminDao = db.adminDao(),
            orderDao = db.orderDao(),
            chatMessageDao = db.chatMessageDao()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun verifyAppNameResource() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Odong-Odong Lombok", appName)
    }

    @Test
    fun testRouteInsertionAndRetrieval() = runBlocking {
        val testRoute = RouteEntity(
            asal = "Mataram",
            tujuan = "Pantai Kuta Mandalika",
            harga = 350000,
            estimasi = "± 1.5 Jam"
        )
        repository.insertRoute(testRoute)

        val routes = repository.allRoutes.first()
        assertEquals(1, routes.size)
        assertEquals("Mataram", routes[0].asal)
        assertEquals("Pantai Kuta Mandalika", routes[0].tujuan)
        assertEquals(350000, routes[0].harga)
    }

    @Test
    fun testDriverManagementAndAuthorization() = runBlocking {
        val driver = DriverEntity(
            nama = "Pak Wayan",
            hp = "0812345678",
            user = "wayan",
            pass = "dr123",
            izin = "tidak",
            platNomor = "DR 8888 WN",
            aktif = true
        )
        repository.insertDriver(driver)

        var drivers = repository.allDrivers.first()
        assertEquals(1, drivers.size)
        assertEquals("tidak", drivers[0].izin)

        // Operator grants permission
        repository.updateDriverIzin(drivers[0].id, "ya")
        drivers = repository.allDrivers.first()
        assertEquals("ya", drivers[0].izin)
    }

    @Test
    fun testOrderLifecycleAndSiapaCepatDiaDapat() = runBlocking {
        val order = OrderEntity(
            nama = "Wisatawan Budi",
            hp = "081999888777",
            asal = "Narmada",
            tujuan = "Pantai Senggigi",
            almtAsal = "Jl. Wisata Narmada No 1",
            almtTujuan = "Pantai Senggigi Sunset",
            jumlah = 2,
            hargaDasar = 150000,
            hargaTawar = 140000,
            totalAkhir = 280000,
            driverPilihan = "Semua Driver",
            tgl = "2026-09-01",
            jam = "09:00",
            bayar = "qris",
            catatan = "Rombongan keluarga besar",
            status = "menunggu",
            oleh = "-"
        )
        val orderId = repository.insertOrder(order)
        assertTrue(orderId > 0)

        // Driver accepts order
        repository.updateOrderStatus(orderId, "diterima", "Pak Suardi")

        val orders = repository.allOrders.first()
        val updatedOrder = orders.find { it.id == orderId }
        assertNotNull(updatedOrder)
        assertEquals("diterima", updatedOrder?.status)
        assertEquals("Pak Suardi", updatedOrder?.oleh)
    }

    @Test
    fun testAdminCreationAndAuthentication() = runBlocking {
        val admin = AdminEntity(
            nama = "Staf Dispatch 1",
            user = "staf1",
            pass = "pass123",
            aktif = true
        )
        repository.insertAdmin(admin)

        val authenticated = repository.findAdmin(user = "staf1", pass = "pass123")
        assertNotNull(authenticated)
        assertEquals("Staf Dispatch 1", authenticated?.nama)
    }

    @Test
    fun testChatMessaging() = runBlocking {
        val message = ChatMessageEntity(
            pengirimNama = "Ibu Siti",
            pengirimPeran = "pelanggan",
            pesan = "Halo Pak Driver, odong-odongnya sudah siap?",
            waktu = "10:00"
        )
        repository.sendChatMessage(message)

        val chats = repository.allChats.first()
        assertEquals(1, chats.size)
        assertEquals("Ibu Siti", chats[0].pengirimNama)
        assertEquals("pelanggan", chats[0].pengirimPeran)
    }

    @Test
    fun testBookingDraftCalculations() {
        val draft = BookingDraftState(
            asal = "Narmada",
            tujuan = "Pantai Senggigi",
            hargaDasarPerUnit = 150000,
            jumlahUnit = 3,
            hargaTawarPerUnit = "140000"
        )

        assertEquals(450000, draft.totalDasar)
        assertEquals(140000, draft.tawarInt)
        assertEquals(420000, draft.totalTawar)
        assertEquals(420000, draft.totalAkhir)
    }
}
