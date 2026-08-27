package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AdminEntity
import com.example.data.local.entity.DriverEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.RouteEntity
import com.example.ui.components.LiveChatView
import com.example.ui.components.OrderCard
import com.example.ui.components.TopAppHeader
import com.example.ui.components.formatRupiahSimple
import com.example.ui.theme.AmberGoldLight
import com.example.ui.theme.AmberGoldWarning
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CoralRedAlert
import com.example.ui.theme.CoralRedLight
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenSuccess
import com.example.ui.theme.OceanBlueDark
import com.example.ui.theme.OceanBlueLight
import com.example.ui.theme.OceanBluePrimary
import com.example.ui.theme.OceanBlueSurface
import com.example.ui.theme.RoyalPurpleAdmin
import com.example.ui.theme.RoyalPurpleLight
import com.example.ui.theme.SunsetOrangeDark
import com.example.ui.theme.SunsetOrangeLight
import com.example.ui.theme.SunsetOrangeSecondary
import com.example.ui.viewmodel.AppRole
import com.example.ui.viewmodel.OdongViewModel

@Composable
fun OperatorAdminPortalScreen(
    viewModel: OdongViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentRole by viewModel.currentRole.collectAsState()
    val adminTitle by viewModel.adminTitle.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()

    val routes by viewModel.routes.collectAsState()
    val drivers by viewModel.drivers.collectAsState()
    val admins by viewModel.admins.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val chats by viewModel.chats.collectAsState()

    val isAdmin = currentRole == AppRole.ADMIN
    val portalColor = if (isAdmin) CoralRedAlert else RoyalPurpleAdmin

    val tabs = remember(isAdmin) {
        val list = mutableListOf(
            "pesanan" to "📋 Semua Pesanan",
            "driver" to "🚐 Kelola Driver",
            "harga" to "💰 Rute & Tarif"
        )
        if (isAdmin) {
            list.add("admin" to "👑 Kelola Admin")
        }
        list.add("riwayat" to "📜 Riwayat & Omset")
        list.add("chat" to "💬 Dispatch Chat")
        list
    }

    val selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        TopAppHeader(
            title = if (isAdmin) "👑 PORTAL SUPER ADMIN" else "📊 PORTAL OPERATOR PUSAT",
            subtitle = adminTitle,
            badgeText = if (isAdmin) "Super Admin" else "Pusat Kontrol",
            badgeColor = portalColor,
            actions = {
                IconButton(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Keluar",
                        tint = Color.White
                    )
                }
            }
        )

        // Summary Statistics Ribbon
        StatsRibbon(orders = orders)

        // Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = portalColor,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = portalColor
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, (key, label) ->
                Tab(
                    selected = activeTab == key,
                    onClick = { viewModel.setActiveTab(key) },
                    text = { Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
            }
        }

        // Tab Content
        when (activeTab) {
            "pesanan" -> {
                OperatorOrdersTab(
                    orders = orders,
                    onAccept = { id -> viewModel.acceptOrder(id, adminTitle) },
                    onReject = { id -> viewModel.rejectOrder(id, adminTitle) },
                    onDelete = { id -> viewModel.deleteOrder(id) }
                )
            }
            "driver" -> {
                OperatorDriverManagementTab(
                    drivers = drivers,
                    onSaveDriver = { id, nama, hp, u, p, iz, plat ->
                        viewModel.saveDriver(id, nama, hp, u, p, iz, plat)
                    },
                    onToggleIzin = { viewModel.toggleDriverIzin(it) },
                    onToggleAktif = { viewModel.toggleDriverAktif(it) },
                    onDeleteDriver = { viewModel.deleteDriver(it) }
                )
            }
            "harga" -> {
                OperatorRouteManagementTab(
                    routes = routes,
                    onSaveRoute = { id, asal, tujuan, harga, estimasi ->
                        viewModel.saveRoute(id, asal, tujuan, harga, estimasi)
                    },
                    onDeleteRoute = { viewModel.deleteRoute(it) }
                )
            }
            "admin" -> {
                if (isAdmin) {
                    OperatorAdminManagementTab(
                        admins = admins,
                        onSaveAdmin = { id, nama, user, pass ->
                            viewModel.saveAdmin(id, nama, user, pass)
                        },
                        onUpdatePass = { id, pass ->
                            viewModel.updateAdminPassword(id, pass)
                        },
                        onToggleAktif = { viewModel.toggleAdminAktif(it) },
                        onDeleteAdmin = { viewModel.deleteAdmin(it) }
                    )
                }
            }
            "riwayat" -> {
                OperatorAuditHistoryTab(
                    orders = orders,
                    onDeleteOrder = { viewModel.deleteOrder(it) }
                )
            }
            "chat" -> {
                LiveChatView(
                    messages = chats,
                    currentUserRole = if (isAdmin) "admin" else "operator",
                    currentUserName = adminTitle,
                    onSendMessage = { viewModel.sendChat(it) }
                )
            }
        }
    }
}

@Composable
private fun StatsRibbon(orders: List<OrderEntity>) {
    val totalOrders = orders.size
    val pending = orders.count { it.status == "menunggu" }
    val accepted = orders.count { it.status == "diterima" }
    val totalRevenue = orders.filter { it.status == "diterima" }.sumOf { it.totalAkhir }

    Surface(
        color = OceanBlueSurface,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total Order", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$totalOrders", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OceanBlueDark)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Menunggu", fontSize = 10.sp, color = SunsetOrangeDark)
                Text("$pending", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SunsetOrangeDark)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Diterima", fontSize = 10.sp, color = EmeraldGreenSuccess)
                Text("$accepted", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldGreenSuccess)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Total Omset", fontSize = 10.sp, color = OceanBlueDark)
                Text(formatRupiahSimple(totalRevenue), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = OceanBluePrimary)
            }
        }
    }
}

@Composable
private fun OperatorOrdersTab(
    orders: List<OrderEntity>,
    onAccept: (Long) -> Unit,
    onReject: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    var statusFilter by remember { mutableStateOf("semua") }

    val filteredOrders = remember(orders, statusFilter) {
        when (statusFilter) {
            "menunggu" -> orders.filter { it.status == "menunggu" }
            "diterima" -> orders.filter { it.status == "diterima" }
            "ditolak" -> orders.filter { it.status == "ditolak" }
            else -> orders
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = statusFilter == "semua",
                onClick = { statusFilter = "semua" },
                label = { Text("Semua (${orders.size})", fontSize = 11.sp) }
            )
            FilterChip(
                selected = statusFilter == "menunggu",
                onClick = { statusFilter = "menunggu" },
                label = { Text("Menunggu (${orders.count { it.status == "menunggu" }})", fontSize = 11.sp) }
            )
            FilterChip(
                selected = statusFilter == "diterima",
                onClick = { statusFilter = "diterima" },
                label = { Text("Diterima (${orders.count { it.status == "diterima" }})", fontSize = 11.sp) }
            )
            FilterChip(
                selected = statusFilter == "ditolak",
                onClick = { statusFilter = "ditolak" },
                label = { Text("Ditolak (${orders.count { it.status == "ditolak" }})", fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("📭 Tidak ada pesanan pada filter ini", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        currentRole = "operator",
                        onAccept = { onAccept(order.id) },
                        onReject = { onReject(order.id) },
                        onDelete = { onDelete(order.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OperatorDriverManagementTab(
    drivers: List<DriverEntity>,
    onSaveDriver: (Long, String, String, String, String, String, String) -> Unit,
    onToggleIzin: (DriverEntity) -> Unit,
    onToggleAktif: (DriverEntity) -> Unit,
    onDeleteDriver: (Long) -> Unit
) {
    val scrollState = rememberScrollState()

    var editId by remember { mutableStateOf(0L) }
    var namaInput by remember { mutableStateOf("") }
    var hpInput by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var passInput by remember { mutableStateOf("") }
    var izinInput by remember { mutableStateOf("ya") }
    var platInput by remember { mutableStateOf("DR 1945 AB") }
    var izinExpanded by remember { mutableStateOf(false) }

    fun resetForm() {
        editId = 0L
        namaInput = ""
        hpInput = ""
        userInput = ""
        passInput = ""
        izinInput = "ya"
        platInput = "DR 1945 AB"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(scrollState)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (editId > 0) "✏️ Edit Driver Wisata" else "➕ Tambah Driver Baru",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SunsetOrangeDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = namaInput,
                        onValueChange = { namaInput = it },
                        label = { Text("Nama Driver") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = hpInput,
                        onValueChange = { hpInput = it },
                        label = { Text("No WhatsApp") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text("Username Login") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = passInput,
                        onValueChange = { passInput = it },
                        label = { Text("Password") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = platInput,
                        onValueChange = { platInput = it },
                        label = { Text("Plat Nomor Unit") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = izinExpanded,
                        onExpandedChange = { izinExpanded = !izinExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = if (izinInput == "ya") "✅ DIIZINKAN" else "🚫 DILARANG",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Izin Order") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = izinExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = izinExpanded,
                            onDismissRequest = { izinExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("✅ YA — Diizinkan Ambil Order") },
                                onClick = {
                                    izinInput = "ya"
                                    izinExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🚫 TIDAK — Dilarang/Nonaktif") },
                                onClick = {
                                    izinInput = "tidak"
                                    izinExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (namaInput.isNotBlank() && hpInput.isNotBlank() && userInput.isNotBlank() && passInput.isNotBlank()) {
                                onSaveDriver(editId, namaInput, hpInput, userInput, passInput, izinInput, platInput)
                                resetForm()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (editId > 0) "✅ UPDATE DRIVER" else "✅ SIMPAN DRIVER", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { resetForm() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🔄 Reset")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "🚐 Daftar Driver Odong-Odong (${drivers.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        drivers.forEach { driver ->
            val isIzin = driver.izin == "ya"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🚐 ${driver.nama}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (isIzin) EmeraldGreenLight else CoralRedLight,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (isIzin) "✅ DIIZINKAN" else "🚫 DILARANG",
                                        color = if (isIzin) EmeraldGreenSuccess else CoralRedAlert,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text("📞 ${driver.hp} | Plat: ${driver.platNomor}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("👤 Login: ${driver.user} / 🔑 ${driver.pass}", fontSize = 11.sp, color = OceanBlueDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                editId = driver.id
                                namaInput = driver.nama
                                hpInput = driver.hp
                                userInput = driver.user
                                passInput = driver.pass
                                izinInput = driver.izin
                                platInput = driver.platNomor
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onToggleIzin(driver) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isIzin) CoralRedAlert else EmeraldGreenSuccess
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.3f)
                        ) {
                            Text(if (isIzin) "🚫 Cabut Izin" else "✅ Beri Izin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { onDeleteDriver(driver.id) },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = CoralRedAlert, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OperatorRouteManagementTab(
    routes: List<RouteEntity>,
    onSaveRoute: (Long, String, String, Int, String) -> Unit,
    onDeleteRoute: (Long) -> Unit
) {
    val scrollState = rememberScrollState()

    var editId by remember { mutableStateOf(0L) }
    var asalInput by remember { mutableStateOf("") }
    var tujuanInput by remember { mutableStateOf("") }
    var hargaInput by remember { mutableStateOf("") }
    var estimasiInput by remember { mutableStateOf("± 45-60 Menit") }

    fun resetForm() {
        editId = 0L
        asalInput = ""
        tujuanInput = ""
        hargaInput = ""
        estimasiInput = "± 45-60 Menit"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(scrollState)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (editId > 0) "✏️ Edit Rute Wisata" else "➕ Tambah Rute Baru Lombok",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanBlueDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = asalInput,
                        onValueChange = { asalInput = it },
                        label = { Text("Asal Jemput") },
                        placeholder = { Text("Contoh: Narmada") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tujuanInput,
                        onValueChange = { tujuanInput = it },
                        label = { Text("Destinasi Tujuan") },
                        placeholder = { Text("Contoh: Senggigi") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hargaInput,
                        onValueChange = { hargaInput = it },
                        label = { Text("Harga Dasar / Unit") },
                        placeholder = { Text("Contoh: 150000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = estimasiInput,
                        onValueChange = { estimasiInput = it },
                        label = { Text("Estimasi Waktu") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val harga = hargaInput.toIntOrNull() ?: 0
                            if (asalInput.isNotBlank() && tujuanInput.isNotBlank() && harga > 0) {
                                onSaveRoute(editId, asalInput, tujuanInput, harga, estimasiInput)
                                resetForm()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (editId > 0) "✅ UPDATE RUTE" else "✅ SIMPAN RUTE", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { resetForm() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🔄 Reset")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "📍 Daftar Rute & Tarif Lombok (${routes.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        routes.forEach { route ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "📍 ${route.asal}  ➔  ${route.tujuan}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OceanBlueDark
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "💰 ${formatRupiahSimple(route.harga)} / Unit  •  ⏱️ ${route.estimasi}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SunsetOrangeDark
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                editId = route.id
                                asalInput = route.asal
                                tujuanInput = route.tujuan
                                hargaInput = route.harga.toString()
                                estimasiInput = route.estimasi
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = OceanBluePrimary)
                        }
                        IconButton(onClick = { onDeleteRoute(route.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = CoralRedAlert)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OperatorAdminManagementTab(
    admins: List<AdminEntity>,
    onSaveAdmin: (Long, String, String, String) -> Unit,
    onUpdatePass: (Long, String) -> Unit,
    onToggleAktif: (AdminEntity) -> Unit,
    onDeleteAdmin: (Long) -> Unit
) {
    val scrollState = rememberScrollState()

    var editId by remember { mutableStateOf(0L) }
    var namaInput by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var passInput by remember { mutableStateOf("") }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var selectedAdminForPass by remember { mutableStateOf<AdminEntity?>(null) }
    var newPassText by remember { mutableStateOf("") }

    fun resetForm() {
        editId = 0L
        namaInput = ""
        userInput = ""
        passInput = ""
    }

    if (showPasswordDialog && selectedAdminForPass != null) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("🔑 Reset Password Admin: ${selectedAdminForPass?.nama}") },
            text = {
                OutlinedTextField(
                    value = newPassText,
                    onValueChange = { newPassText = it },
                    label = { Text("Password Baru") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPassText.isNotBlank()) {
                            onUpdatePass(selectedAdminForPass!!.id, newPassText)
                            showPasswordDialog = false
                            newPassText = ""
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPasswordDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(scrollState)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (editId > 0) "✏️ Edit Admin" else "👑 Tambah Admin Baru",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoralRedAlert
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = namaInput,
                    onValueChange = { namaInput = it },
                    label = { Text("Nama Admin") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text("Username") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = passInput,
                        onValueChange = { passInput = it },
                        label = { Text("Password") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (namaInput.isNotBlank() && userInput.isNotBlank() && passInput.isNotBlank()) {
                                onSaveAdmin(editId, namaInput, userInput, passInput)
                                resetForm()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralRedAlert),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (editId > 0) "✅ UPDATE ADMIN" else "✅ SIMPAN ADMIN", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { resetForm() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🔄 Reset")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "👑 Daftar Pengelola Admin (${admins.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        admins.forEach { admin ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "👑 ${admin.nama}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (admin.aktif) EmeraldGreenLight else CoralRedLight,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (admin.aktif) "✅ AKTIF" else "🚫 NONAKTIF",
                                        color = if (admin.aktif) EmeraldGreenSuccess else CoralRedAlert,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text("👤 User: ${admin.user} | 🔑 Pass: ${admin.pass}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedAdminForPass = admin
                                newPassText = ""
                                showPasswordDialog = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text("🔑 Ganti Pass", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onToggleAktif(admin) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (admin.aktif) SunsetOrangeSecondary else EmeraldGreenSuccess
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (admin.aktif) "Nonaktif" else "Aktifkan", fontSize = 11.sp)
                        }

                        if (admin.user != "admin") {
                            OutlinedButton(
                                onClick = { onDeleteAdmin(admin.id) },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = CoralRedAlert, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OperatorAuditHistoryTab(
    orders: List<OrderEntity>,
    onDeleteOrder: (Long) -> Unit
) {
    val totalRevenue = orders.filter { it.status == "diterima" }.sumOf { it.totalAkhir }
    val totalUnits = orders.filter { it.status == "diterima" }.sumOf { it.jumlah }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OceanBlueSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, OceanBlueLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 Laporan & Omset Wisata", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OceanBlueDark)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Omset Berhasil:", fontSize = 13.sp)
                    Text(formatRupiahSimple(totalRevenue), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = EmeraldGreenSuccess)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Unit Beroperasi:", fontSize = 13.sp)
                    Text("$totalUnits Unit Odong-Odong", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("📜 Log Audit Semua Transaksi (${orders.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("📭 Belum ada riwayat pesanan", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        currentRole = "operator",
                        onDelete = { onDeleteOrder(order.id) }
                    )
                }
            }
        }
    }
}
