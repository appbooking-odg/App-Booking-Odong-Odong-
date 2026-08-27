package com.example.data.repository

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
import kotlinx.coroutines.flow.Flow

class OdongRepository(
    private val routeDao: RouteDao,
    private val driverDao: DriverDao,
    private val adminDao: AdminDao,
    private val orderDao: OrderDao,
    private val chatMessageDao: ChatMessageDao
) {
    // Routes
    val allRoutes: Flow<List<RouteEntity>> = routeDao.getAllRoutes()
    suspend fun insertRoute(route: RouteEntity) = routeDao.insertRoute(route)
    suspend fun updateRoute(route: RouteEntity) = routeDao.updateRoute(route)
    suspend fun deleteRouteById(id: Long) = routeDao.deleteRouteById(id)

    // Drivers
    val allDrivers: Flow<List<DriverEntity>> = driverDao.getAllDrivers()
    suspend fun findDriver(user: String, pass: String): DriverEntity? = driverDao.findDriver(user, pass)
    suspend fun insertDriver(driver: DriverEntity) = driverDao.insertDriver(driver)
    suspend fun updateDriver(driver: DriverEntity) = driverDao.updateDriver(driver)
    suspend fun updateDriverIzin(id: Long, izin: String) = driverDao.updateIzin(id, izin)
    suspend fun updateDriverAktif(id: Long, aktif: Boolean) = driverDao.updateAktif(id, aktif)
    suspend fun deleteDriverById(id: Long) = driverDao.deleteDriverById(id)

    // Admins
    val allAdmins: Flow<List<AdminEntity>> = adminDao.getAllAdmins()
    suspend fun findAdmin(user: String, pass: String): AdminEntity? = adminDao.findAdmin(user, pass)
    suspend fun insertAdmin(admin: AdminEntity) = adminDao.insertAdmin(admin)
    suspend fun updateAdmin(admin: AdminEntity) = adminDao.updateAdmin(admin)
    suspend fun updateAdminPassword(id: Long, newPass: String) = adminDao.updatePassword(id, newPass)
    suspend fun updateAdminAktif(id: Long, aktif: Boolean) = adminDao.updateAktif(id, aktif)
    suspend fun deleteAdminById(id: Long) = adminDao.deleteAdminById(id)

    // Orders
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    fun getOrdersByCustomer(nama: String): Flow<List<OrderEntity>> = orderDao.getOrdersByCustomerName(nama)
    fun getOrdersByDriver(driverName: String): Flow<List<OrderEntity>> = orderDao.getOrdersByDriver(driverName)
    suspend fun insertOrder(order: OrderEntity) = orderDao.insertOrder(order)
    suspend fun updateOrderStatus(id: Long, status: String, oleh: String) = orderDao.updateOrderStatus(id, status, oleh)
    suspend fun deleteOrderById(id: Long) = orderDao.deleteOrderById(id)

    // Chat
    val allChats: Flow<List<ChatMessageEntity>> = chatMessageDao.getAllMessages()
    suspend fun sendChatMessage(chat: ChatMessageEntity) = chatMessageDao.insertMessage(chat)
}
