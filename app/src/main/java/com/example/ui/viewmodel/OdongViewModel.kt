package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AdminEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.DriverEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.RouteEntity
import com.example.data.repository.OdongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppRole {
    NONE,
    PELANGGAN,
    DRIVER,
    OPERATOR,
    ADMIN
}

data class BookingDraftState(
    val asal: String = "",
    val tujuan: String = "",
    val alamatAsal: String = "",
    val alamatTujuan: String = "",
    val jumlahUnit: Int = 1,
    val hargaDasarPerUnit: Int = 0,
    val hargaTawarPerUnit: String = "",
    val tanggal: String = "",
    val jam: String = "",
    val metodeBayar: String = "cash",
    val driverPilihan: String = "Semua Driver",
    val catatan: String = ""
) {
    val tawarInt: Int
        get() = hargaTawarPerUnit.toIntOrNull() ?: 0

    val totalDasar: Int
        get() = hargaDasarPerUnit * jumlahUnit

    val totalTawar: Int
        get() = if (tawarInt > 0) tawarInt * jumlahUnit else 0

    val totalAkhir: Int
        get() = if (tawarInt > 0) totalTawar else totalDasar
}

class OdongViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OdongRepository
    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = OdongRepository(
            routeDao = db.routeDao(),
            driverDao = db.driverDao(),
            adminDao = db.adminDao(),
            orderDao = db.orderDao(),
            chatMessageDao = db.chatMessageDao()
        )
        // Ensure defaults are inserted
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.populateInitialData(db)
        }
    }

    // Role and session
    private val _currentRole = MutableStateFlow(AppRole.NONE)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    private val _customerName = MutableStateFlow("")
    val customerName: StateFlow<String> = _customerName.asStateFlow()

    private val _customerPhone = MutableStateFlow("")
    val customerPhone: StateFlow<String> = _customerPhone.asStateFlow()

    private val _currentDriver = MutableStateFlow<DriverEntity?>(null)
    val currentDriver: StateFlow<DriverEntity?> = _currentDriver.asStateFlow()

    private val _adminTitle = MutableStateFlow("Operator Dispatch")
    val adminTitle: StateFlow<String> = _adminTitle.asStateFlow()

    private val _activeTab = MutableStateFlow("pesanan")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    // Notification toast events
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Booking draft
    private val _bookingDraft = MutableStateFlow(
        BookingDraftState(
            tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            jam = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(System.currentTimeMillis() + 1000 * 60 * 30))
        )
    )
    val bookingDraft: StateFlow<BookingDraftState> = _bookingDraft.asStateFlow()

    // Data streams
    val routes: StateFlow<List<RouteEntity>> = repository.allRoutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val drivers: StateFlow<List<DriverEntity>> = repository.allDrivers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val admins: StateFlow<List<AdminEntity>> = repository.allAdmins
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chats: StateFlow<List<ChatMessageEntity>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions
    fun setRole(role: AppRole) {
        _currentRole.value = role
        _activeTab.value = when (role) {
            AppRole.PELANGGAN -> "pesanan"
            AppRole.DRIVER -> "masuk"
            AppRole.OPERATOR, AppRole.ADMIN -> "pesanan"
            else -> "pesanan"
        }
    }

    fun setActiveTab(tab: String) {
        _activeTab.value = tab
    }

    fun loginCustomer(nama: String, hp: String): Boolean {
        if (nama.isBlank() || hp.isBlank()) return false
        _customerName.value = nama.trim()
        _customerPhone.value = hp.trim()
        setRole(AppRole.PELANGGAN)
        showNotification("Selamat datang, ${nama.trim()}!")
        return true
    }

    fun loginDriver(username: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val u = username.trim()
            val p = pass.trim()
            val driver = repository.findDriver(u, p)
            if (driver != null) {
                _currentDriver.value = driver
                _customerName.value = driver.nama
                launch(Dispatchers.Main) {
                    setRole(AppRole.DRIVER)
                    showNotification("Selamat bertugas, ${driver.nama}!")
                    onResult(true, "Berhasil masuk!")
                }
            } else if (u == "driver" && p == "dr123") {
                val fallbackDriver = DriverEntity(
                    nama = "Pak Suardi Lombok",
                    hp = "081907951938",
                    user = "driver",
                    pass = "dr123",
                    izin = "ya",
                    aktif = true,
                    platNomor = "DR 1945 AB"
                )
                _currentDriver.value = fallbackDriver
                _customerName.value = fallbackDriver.nama
                launch(Dispatchers.Main) {
                    setRole(AppRole.DRIVER)
                    showNotification("Selamat bertugas, Pak Suardi Lombok!")
                    onResult(true, "Berhasil masuk!")
                }
            } else {
                launch(Dispatchers.Main) {
                    onResult(false, "Username atau Password Driver salah!")
                }
            }
        }
    }

    fun loginOperatorAdmin(role: AppRole, username: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val u = username.trim()
            val p = pass.trim()

            if (role == AppRole.OPERATOR) {
                if ((u == "operator" && p == "op123") || (u == "admin" && p == "admin123")) {
                    _adminTitle.value = "Operator Pusat Lombok"
                    launch(Dispatchers.Main) {
                        setRole(AppRole.OPERATOR)
                        showNotification("Masuk sebagai Operator Pusat")
                        onResult(true, "Berhasil masuk!")
                    }
                } else {
                    val adm = repository.findAdmin(u, p)
                    if (adm != null && adm.aktif) {
                        _adminTitle.value = adm.nama
                        launch(Dispatchers.Main) {
                            setRole(AppRole.OPERATOR)
                            showNotification("Masuk sebagai ${adm.nama}")
                            onResult(true, "Berhasil masuk!")
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            onResult(false, "Username atau Password Operator salah!")
                        }
                    }
                }
            } else if (role == AppRole.ADMIN) {
                if (u == "admin" && p == "admin123") {
                    _adminTitle.value = "Admin Utama Lombok"
                    launch(Dispatchers.Main) {
                        setRole(AppRole.ADMIN)
                        showNotification("Masuk sebagai Admin Utama")
                        onResult(true, "Berhasil masuk!")
                    }
                } else {
                    val adm = repository.findAdmin(u, p)
                    if (adm != null && adm.aktif) {
                        _adminTitle.value = "Admin: ${adm.nama}"
                        launch(Dispatchers.Main) {
                            setRole(AppRole.ADMIN)
                            showNotification("Masuk sebagai Admin: ${adm.nama}")
                            onResult(true, "Berhasil masuk!")
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            onResult(false, "Username atau Password Admin salah!")
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        _currentRole.value = AppRole.NONE
        _customerName.value = ""
        _customerPhone.value = ""
        _currentDriver.value = null
        _adminTitle.value = "Operator Dispatch"
        _activeTab.value = "pesanan"
        showNotification("Berhasil keluar")
    }

    // Update Draft State
    fun updateDraft(transform: (BookingDraftState) -> BookingDraftState) {
        _bookingDraft.value = transform(_bookingDraft.value)
    }

    fun selectRoute(asal: String, tujuan: String) {
        val currentRoutes = routes.value
        val match = currentRoutes.firstOrNull { it.asal == asal && it.tujuan == tujuan }
            ?: currentRoutes.firstOrNull { it.asal == asal }
        val price = match?.harga ?: 150000
        _bookingDraft.value = _bookingDraft.value.copy(
            asal = asal,
            tujuan = tujuan,
            hargaDasarPerUnit = price
        )
    }

    fun submitBookingOrder(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val draft = _bookingDraft.value
        if (draft.asal.isBlank() || draft.tujuan.isBlank()) {
            onError("Silakan pilih rute asal & tujuan!")
            return
        }
        if (draft.tanggal.isBlank() || draft.jam.isBlank()) {
            onError("Silakan isi tanggal & jam penjemputan!")
            return
        }
        if (_customerName.value.isBlank() || _customerPhone.value.isBlank()) {
            onError("Data nama & nomor HP pemesan belum lengkap!")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val order = OrderEntity(
                nama = _customerName.value,
                hp = _customerPhone.value,
                asal = draft.asal,
                tujuan = draft.tujuan,
                almtAsal = draft.alamatAsal.ifBlank { "Titik jemput standar area ${draft.asal}" },
                almtTujuan = draft.alamatTujuan.ifBlank { "Area wisata ${draft.tujuan}" },
                jumlah = draft.jumlahUnit,
                hargaDasar = draft.hargaDasarPerUnit,
                hargaTawar = draft.tawarInt,
                totalAkhir = draft.totalAkhir,
                tgl = draft.tanggal,
                jam = draft.jam,
                bayar = draft.metodeBayar,
                driverPilihan = draft.driverPilihan,
                status = "menunggu",
                oleh = "",
                waktu = System.currentTimeMillis(),
                catatan = draft.catatan
            )
            repository.insertOrder(order)
            launch(Dispatchers.Main) {
                showNotification("📦 Pesanan Baru Terkirim! Semua driver online akan melihat.")
                _activeTab.value = "riwayat"
                onSuccess()
            }
        }
    }

    fun acceptOrder(orderId: Long, driverOrOpName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOrderStatus(orderId, "diterima", driverOrOpName)
            launch(Dispatchers.Main) {
                showNotification("✅ BERHASIL DITERIMA oleh $driverOrOpName — Siapa cepat dia dapat!")
            }
        }
    }

    fun rejectOrder(orderId: Long, driverOrOpName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOrderStatus(orderId, "ditolak", driverOrOpName)
            launch(Dispatchers.Main) {
                showNotification("❌ Pesanan ditolak oleh $driverOrOpName")
            }
        }
    }

    fun deleteOrder(orderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOrderById(orderId)
            launch(Dispatchers.Main) {
                showNotification("🗑️ Pesanan berhasil dihapus")
            }
        }
    }

    // Route Management
    fun saveRoute(id: Long, asal: String, tujuan: String, harga: Int, estimasi: String) {
        if (asal.isBlank() || tujuan.isBlank() || harga <= 0) {
            showNotification("⚠️ Mohon lengkapi asal, tujuan dan harga rute!")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val route = RouteEntity(
                id = if (id > 0) id else 0,
                asal = asal.trim(),
                tujuan = tujuan.trim(),
                harga = harga,
                estimasi = estimasi.ifBlank { "± 45 Menit" }
            )
            if (id > 0) {
                repository.updateRoute(route)
            } else {
                repository.insertRoute(route)
            }
            launch(Dispatchers.Main) {
                showNotification("✅ Rute $asal ➔ $tujuan tersimpan!")
            }
        }
    }

    fun deleteRoute(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRouteById(id)
            launch(Dispatchers.Main) {
                showNotification("🗑️ Rute telah dihapus")
            }
        }
    }

    // Driver Management
    fun saveDriver(
        id: Long,
        nama: String,
        hp: String,
        user: String,
        pass: String,
        izin: String,
        plat: String
    ) {
        if (nama.isBlank() || hp.isBlank() || user.isBlank() || pass.isBlank()) {
            showNotification("⚠️ Lengkapi semua data driver!")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val driver = DriverEntity(
                id = if (id > 0) id else 0,
                nama = nama.trim(),
                hp = hp.trim(),
                user = user.trim(),
                pass = pass.trim(),
                izin = izin,
                aktif = true,
                platNomor = plat.ifBlank { "DR 1945 AB" }
            )
            if (id > 0) {
                repository.updateDriver(driver)
            } else {
                repository.insertDriver(driver)
            }
            launch(Dispatchers.Main) {
                showNotification("✅ Driver ${nama.trim()} berhasil disimpan (Izin: ${if (izin == "ya") "✅ Diizinkan" else "🚫 Dilarang"})")
            }
        }
    }

    fun toggleDriverIzin(driver: DriverEntity) {
        val newIzin = if (driver.izin == "ya") "tidak" else "ya"
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDriverIzin(driver.id, newIzin)
            // If current logged-in driver is updated, reflect in session
            if (_currentDriver.value?.id == driver.id) {
                _currentDriver.value = driver.copy(izin = newIzin)
            }
            launch(Dispatchers.Main) {
                val statusText = if (newIzin == "ya") "✅ DIIZINKAN TERIMA PESANAN" else "🚫 IZIN DICABUT (DILARANG)"
                showNotification("${driver.nama}: $statusText")
            }
        }
    }

    fun toggleDriverAktif(driver: DriverEntity) {
        val newAktif = !driver.aktif
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDriverAktif(driver.id, newAktif)
            launch(Dispatchers.Main) {
                showNotification("Driver ${driver.nama} sekarang ${if (newAktif) "Aktif" else "Nonaktif"}")
            }
        }
    }

    fun deleteDriver(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDriverById(id)
            launch(Dispatchers.Main) {
                showNotification("🗑️ Driver telah dihapus")
            }
        }
    }

    // Admin Management
    fun saveAdmin(id: Long, nama: String, user: String, pass: String) {
        if (nama.isBlank() || user.isBlank() || pass.isBlank()) {
            showNotification("⚠️ Lengkapi data admin!")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val admin = AdminEntity(
                id = if (id > 0) id else 0,
                nama = nama.trim(),
                user = user.trim(),
                pass = pass.trim(),
                aktif = true
            )
            if (id > 0) {
                repository.updateAdmin(admin)
            } else {
                repository.insertAdmin(admin)
            }
            launch(Dispatchers.Main) {
                showNotification("✅ Admin ${nama.trim()} berhasil disimpan!")
            }
        }
    }

    fun updateAdminPassword(id: Long, newPass: String) {
        if (newPass.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAdminPassword(id, newPass.trim())
            launch(Dispatchers.Main) {
                showNotification("🔑 Password admin berhasil diperbarui!")
            }
        }
    }

    fun toggleAdminAktif(admin: AdminEntity) {
        val newAktif = !admin.aktif
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAdminAktif(admin.id, newAktif)
            launch(Dispatchers.Main) {
                showNotification("Admin ${admin.nama} ${if (newAktif) "Diaktifkan" else "Dinonaktifkan"}")
            }
        }
    }

    fun deleteAdmin(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAdminById(id)
            launch(Dispatchers.Main) {
                showNotification("🗑️ Admin telah dihapus")
            }
        }
    }

    // Chat
    fun sendChat(pesan: String) {
        if (pesan.isBlank()) return
        val senderRole = when (_currentRole.value) {
            AppRole.PELANGGAN -> "pelanggan"
            AppRole.DRIVER -> "driver"
            AppRole.OPERATOR -> "operator"
            AppRole.ADMIN -> "admin"
            AppRole.NONE -> "umum"
        }
        val senderName = when (_currentRole.value) {
            AppRole.PELANGGAN -> _customerName.value.ifBlank { "Pelanggan" }
            AppRole.DRIVER -> _currentDriver.value?.nama ?: "Driver"
            AppRole.OPERATOR -> _adminTitle.value.ifBlank { "Operator" }
            AppRole.ADMIN -> _adminTitle.value.ifBlank { "Admin" }
            AppRole.NONE -> "Tamu"
        }
        val timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        viewModelScope.launch(Dispatchers.IO) {
            val chatMsg = ChatMessageEntity(
                pengirimPeran = senderRole,
                pengirimNama = senderName,
                pesan = pesan.trim(),
                waktu = timeFormatted,
                timestamp = System.currentTimeMillis()
            )
            repository.sendChatMessage(chatMsg)
        }
    }

    private fun showNotification(msg: String) {
        viewModelScope.launch {
            _toastEvent.emit(msg)
        }
    }
}
