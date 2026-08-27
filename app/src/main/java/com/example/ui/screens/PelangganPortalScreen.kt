package com.example.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.OrderEntity
import com.example.ui.components.LiveChatView
import com.example.ui.components.OrderCard
import com.example.ui.components.TopAppHeader
import com.example.ui.components.formatRupiahSimple
import com.example.ui.theme.CardBorder
import com.example.ui.theme.EmeraldGreenSuccess
import com.example.ui.theme.OceanBlueDark
import com.example.ui.theme.OceanBlueLight
import com.example.ui.theme.OceanBluePrimary
import com.example.ui.theme.OceanBlueSurface
import com.example.ui.theme.SunsetOrangeDark
import com.example.ui.theme.SunsetOrangeLight
import com.example.ui.theme.SunsetOrangeSecondary
import com.example.ui.viewmodel.OdongViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganPortalScreen(
    viewModel: OdongViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val customerName by viewModel.customerName.collectAsState()
    val customerPhone by viewModel.customerPhone.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()

    val routes by viewModel.routes.collectAsState()
    val drivers by viewModel.drivers.collectAsState()
    val allOrders by viewModel.orders.collectAsState()
    val chats by viewModel.chats.collectAsState()
    val draft by viewModel.bookingDraft.collectAsState()

    // Filter customer's own orders
    val myOrders = allOrders.filter { it.nama.equals(customerName, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        TopAppHeader(
            title = "👤 PORTAL PELANGGAN",
            subtitle = "$customerName | 📞 $customerPhone",
            badgeText = "Wisatawan",
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

        // Custom Modern Tabs
        TabRow(
            selectedTabIndex = when (activeTab) {
                "pesanan" -> 0
                "riwayat" -> 1
                "chat" -> 2
                else -> 0
            },
            containerColor = Color.White,
            contentColor = OceanBluePrimary,
            indicator = { tabPositions ->
                val tabIndex = when (activeTab) {
                    "pesanan" -> 0
                    "riwayat" -> 1
                    "chat" -> 2
                    else -> 0
                }
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = OceanBluePrimary
                )
            }
        ) {
            Tab(
                selected = activeTab == "pesanan",
                onClick = { viewModel.setActiveTab("pesanan") },
                text = { Text("📝 Pesanan", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "riwayat",
                onClick = { viewModel.setActiveTab("riwayat") },
                text = { Text("📜 Riwayat (${myOrders.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "chat",
                onClick = { viewModel.setActiveTab("chat") },
                text = { Text("💬 Chat", fontWeight = FontWeight.Bold) }
            )
        }

        when (activeTab) {
            "pesanan" -> {
                BookingFormContent(
                    viewModel = viewModel,
                    draft = draft,
                    routes = routes,
                    drivers = drivers
                )
            }
            "riwayat" -> {
                CustomerRiwayatContent(
                    orders = myOrders,
                    onStartNewOrder = { viewModel.setActiveTab("pesanan") }
                )
            }
            "chat" -> {
                LiveChatView(
                    messages = chats,
                    currentUserRole = "pelanggan",
                    currentUserName = customerName,
                    onSendMessage = { viewModel.sendChat(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingFormContent(
    viewModel: OdongViewModel,
    draft: com.example.ui.viewmodel.BookingDraftState,
    routes: List<com.example.data.local.entity.RouteEntity>,
    drivers: List<com.example.data.local.entity.DriverEntity>
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var asalExpanded by remember { mutableStateOf(false) }
    var tujuanExpanded by remember { mutableStateOf(false) }
    var driverExpanded by remember { mutableStateOf(false) }
    var bayarExpanded by remember { mutableStateOf(false) }

    // Unique Origin and Destination options from routes
    val asalOptions = remember(routes) {
        val list = routes.map { it.asal }.distinct().toMutableList()
        if (list.isEmpty()) listOf("Narmada", "Mataram", "Senggigi", "Ampenan", "Praya") else list
    }

    val tujuanOptions = remember(routes, draft.asal) {
        val matched = routes.filter { it.asal.equals(draft.asal, ignoreCase = true) }.map { it.tujuan }
        if (matched.isNotEmpty()) matched else routes.map { it.tujuan }.distinct()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "🚐 Form Booking Odong-Odong Wisata",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanBlueDark
                )
                Text(
                    text = "Tentukan rute, jumlah unit odong-odong & nego harga langsung dengan driver",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // 1. Pilih Driver (Opsional / Semua Driver)
                Text("Pilih Driver / Armada", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = driverExpanded,
                    onExpandedChange = { driverExpanded = !driverExpanded }
                ) {
                    OutlinedTextField(
                        value = draft.driverPilihan,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = driverExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = driverExpanded,
                        onDismissRequest = { driverExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("🌟 Semua Driver (Siapa Cepat Dia Dapat)") },
                            onClick = {
                                viewModel.updateDraft { it.copy(driverPilihan = "Semua Driver") }
                                driverExpanded = false
                            }
                        )
                        drivers.filter { it.aktif }.forEach { d ->
                            DropdownMenuItem(
                                text = { Text("🚐 ${d.nama} (${d.platNomor})") },
                                onClick = {
                                    viewModel.updateDraft { it.copy(driverPilihan = d.nama) }
                                    driverExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Asal Jemput
                Text("📍 Asal Jemput (Wilayah Lombok)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = asalExpanded,
                    onExpandedChange = { asalExpanded = !asalExpanded }
                ) {
                    OutlinedTextField(
                        value = if (draft.asal.isEmpty()) "-- Pilih Wilayah Asal --" else draft.asal,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = asalExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth()
                            .testTag("dropdown_asal"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = asalExpanded,
                        onDismissRequest = { asalExpanded = false }
                    ) {
                        asalOptions.forEach { asal ->
                            DropdownMenuItem(
                                text = { Text(asal) },
                                onClick = {
                                    viewModel.selectRoute(asal, draft.tujuan)
                                    asalExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = draft.alamatAsal,
                    onValueChange = { viewModel.updateDraft { d -> d.copy(alamatAsal = it) } },
                    placeholder = { Text("Tulis alamat lengkap titik jemput...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Tujuan
                Text("🎯 Tujuan Wisata", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = tujuanExpanded,
                    onExpandedChange = { tujuanExpanded = !tujuanExpanded }
                ) {
                    OutlinedTextField(
                        value = if (draft.tujuan.isEmpty()) "-- Pilih Destinasi Tujuan --" else draft.tujuan,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tujuanExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth()
                            .testTag("dropdown_tujuan"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = tujuanExpanded,
                        onDismissRequest = { tujuanExpanded = false }
                    ) {
                        tujuanOptions.forEach { tujuan ->
                            DropdownMenuItem(
                                text = { Text(tujuan) },
                                onClick = {
                                    viewModel.selectRoute(draft.asal, tujuan)
                                    tujuanExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = draft.alamatTujuan,
                    onValueChange = { viewModel.updateDraft { d -> d.copy(alamatTujuan = it) } },
                    placeholder = { Text("Tulis alamat tujuan / lokasi pantai / hotel...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Jumlah Unit Odong-Odong Stepper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("🚐 Jumlah Unit Odong-Odong", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Kapasitas ±10-15 orang / unit", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (draft.jumlahUnit > 1) {
                                    viewModel.updateDraft { it.copy(jumlahUnit = it.jumlahUnit - 1) }
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(OceanBlueLight, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Kurang", tint = OceanBlueDark)
                        }

                        Text(
                            text = "${draft.jumlahUnit}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )

                        IconButton(
                            onClick = {
                                viewModel.updateDraft { it.copy(jumlahUnit = it.jumlahUnit + 1) }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(OceanBluePrimary, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Harga Nego / Tawar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 Harga Tawar / Unit (Opsional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunsetOrangeDark
                    )
                    Text(
                        text = "Dasar: ${if (draft.hargaDasarPerUnit > 0) formatRupiahSimple(draft.hargaDasarPerUnit) else "Pilih Rute"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = draft.hargaTawarPerUnit,
                    onValueChange = { viewModel.updateDraft { d -> d.copy(hargaTawarPerUnit = it) } },
                    placeholder = { Text("Kosongkan jika mengikuti harga dasar") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SunsetOrangeSecondary,
                        unfocusedBorderColor = CardBorder
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 6. Live Breakdown Card
                Surface(
                    color = OceanBlueSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OceanBlueLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("💰 Harga Dasar / Unit:", fontSize = 12.sp)
                            Text(formatRupiahSimple(draft.hargaDasarPerUnit), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🚐 Jumlah Unit:", fontSize = 12.sp)
                            Text("${draft.jumlahUnit} Unit", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("💳 Total Harga Dasar:", fontSize = 12.sp)
                            Text(formatRupiahSimple(draft.totalDasar), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        if (draft.tawarInt > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(color = OceanBlueLight)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("🔥 Harga Nego / Unit:", fontSize = 12.sp, color = SunsetOrangeDark, fontWeight = FontWeight.Bold)
                                Text(formatRupiahSimple(draft.tawarInt), fontSize = 12.sp, color = SunsetOrangeDark, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("🔥 Total Nego:", fontSize = 12.sp, color = SunsetOrangeDark, fontWeight = FontWeight.Bold)
                                Text(formatRupiahSimple(draft.totalTawar), fontSize = 12.sp, color = SunsetOrangeDark, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = CardBorder)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✅ TOTAL AKHIR:", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = OceanBlueDark)
                            Text(
                                text = formatRupiahSimple(draft.totalAkhir),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OceanBluePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 7. Tanggal & Jam Picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("📅 Tanggal", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, y, m, d ->
                                            val mFormatted = String.format("%02d", m + 1)
                                            val dFormatted = String.format("%02d", d)
                                            viewModel.updateDraft { it.copy(tanggal = "$y-$mFormatted-$dFormatted") }
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(draft.tanggal.ifBlank { "Pilih Tanggal" }, fontSize = 13.sp)
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("🕐 Jam", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val calendar = Calendar.getInstance()
                                    TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            val h = String.format("%02d", hourOfDay)
                                            val min = String.format("%02d", minute)
                                            viewModel.updateDraft { it.copy(jam = "$h:$min") }
                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        true
                                    ).show()
                                }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = SunsetOrangeDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(draft.jam.ifBlank { "Pilih Jam" }, fontSize = 13.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 8. Metode Pembayaran
                Text("💳 Metode Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = bayarExpanded,
                    onExpandedChange = { bayarExpanded = !bayarExpanded }
                ) {
                    OutlinedTextField(
                        value = when (draft.metodeBayar) {
                            "cash" -> "💵 Tunai (Bayar langsung ke Driver)"
                            "transfer" -> "🏦 Transfer Bank (BCA / BRI / BSI)"
                            "qris" -> "📱 QRIS (Scan Semua E-Wallet / Bank)"
                            else -> draft.metodeBayar
                        },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bayarExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = bayarExpanded,
                        onDismissRequest = { bayarExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("💵 Tunai (Cash ke Driver saat jemput)") },
                            onClick = {
                                viewModel.updateDraft { it.copy(metodeBayar = "cash") }
                                bayarExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🏦 Transfer Bank (BCA/BRI)") },
                            onClick = {
                                viewModel.updateDraft { it.copy(metodeBayar = "transfer") }
                                bayarExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("📱 QRIS (Semua Pembayaran Digital)") },
                            onClick = {
                                viewModel.updateDraft { it.copy(metodeBayar = "qris") }
                                bayarExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 9. Catatan Khusus
                Text("📝 Catatan Tambahan (Opsional)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = draft.catatan,
                    onValueChange = { viewModel.updateDraft { d -> d.copy(catatan = it) } },
                    placeholder = { Text("Contoh: Minta lagu pop/dangdut, ada rombongan anak-anak...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        viewModel.submitBookingOrder(
                            onSuccess = {
                                Toast.makeText(context, "✅ Pesanan Berhasil Dikirim!", Toast.LENGTH_LONG).show()
                            },
                            onError = { err ->
                                Toast.makeText(context, "⚠️ $err", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_submit_order"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSuccess),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("✅ KIRIM PESANAN SEKARANG", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun CustomerRiwayatContent(
    orders: List<OrderEntity>,
    onStartNewOrder: () -> Unit
) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = OceanBluePrimary.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Belum Ada Riwayat Pesanan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Pesanan yang Anda buat akan muncul di sini secara real-time.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onStartNewOrder,
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Buat Pesanan Baru")
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            items(orders, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    currentRole = "pelanggan"
                )
            }
        }
    }
}
