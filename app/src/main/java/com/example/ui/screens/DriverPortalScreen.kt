package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DriverEntity
import com.example.data.local.entity.OrderEntity
import com.example.ui.components.LiveChatView
import com.example.ui.components.OrderCard
import com.example.ui.components.TopAppHeader
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CoralRedAlert
import com.example.ui.theme.CoralRedLight
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenSuccess
import com.example.ui.theme.SunsetOrangeDark
import com.example.ui.theme.SunsetOrangeLight
import com.example.ui.theme.SunsetOrangeSecondary
import com.example.ui.viewmodel.OdongViewModel

@Composable
fun DriverPortalScreen(
    viewModel: OdongViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentDriver by viewModel.currentDriver.collectAsState()
    val allDrivers by viewModel.drivers.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val allOrders by viewModel.orders.collectAsState()
    val chats by viewModel.chats.collectAsState()

    // Match fresh driver state from database
    val driverData = allDrivers.find { it.user == currentDriver?.user } ?: currentDriver
    val driverName = driverData?.nama ?: "Driver Lombok"
    val isPermitted = driverData?.izin == "ya"

    val pendingOrders = allOrders.filter { it.status == "menunggu" }
    val myAcceptedOrders = allOrders.filter { it.status == "diterima" && it.oleh.equals(driverName, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        TopAppHeader(
            title = "🚐 PORTAL DRIVER",
            subtitle = "$driverName | ${driverData?.platNomor ?: "DR 1945 AB"}",
            badgeText = if (isPermitted) "Aktif" else "Non-Aktif",
            badgeColor = if (isPermitted) EmeraldGreenSuccess else CoralRedAlert,
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

        // Permission Warning Status Banner
        if (!isPermitted) {
            Surface(
                color = CoralRedLight,
                shape = RoundedCornerShape(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CoralRedAlert.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = CoralRedAlert,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🚫 BELUM DIIZINKAN OLEH OPERATOR UNTUK MENGAMBIL PESANAN. Silakan hubungi operator pusat untuk aktivasi.",
                        color = CoralRedAlert,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 15.sp
                    )
                }
            }
        } else {
            Surface(
                color = EmeraldGreenLight,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✅ STATUS: DIIZINKAN OPERATOR (Siap Menerima Orderan Wisata)",
                        color = EmeraldGreenSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Tab Navigation
        TabRow(
            selectedTabIndex = when (activeTab) {
                "masuk" -> 0
                "riwayat" -> 1
                "chat" -> 2
                else -> 0
            },
            containerColor = Color.White,
            contentColor = SunsetOrangeSecondary,
            indicator = { tabPositions ->
                val tabIndex = when (activeTab) {
                    "masuk" -> 0
                    "riwayat" -> 1
                    "chat" -> 2
                    else -> 0
                }
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = SunsetOrangeSecondary
                )
            }
        ) {
            Tab(
                selected = activeTab == "masuk",
                onClick = { viewModel.setActiveTab("masuk") },
                text = { Text("📥 Masuk (${pendingOrders.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "riwayat",
                onClick = { viewModel.setActiveTab("riwayat") },
                text = { Text("📜 Riwayat Saya (${myAcceptedOrders.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeTab == "chat",
                onClick = { viewModel.setActiveTab("chat") },
                text = { Text("💬 Live Chat", fontWeight = FontWeight.Bold) }
            )
        }

        when (activeTab) {
            "masuk" -> {
                DriverIncomingOrders(
                    orders = pendingOrders,
                    driverName = driverName,
                    isPermitted = isPermitted,
                    onAccept = { orderId ->
                        if (!isPermitted) {
                            Toast.makeText(context, "🚫 Anda belum diizinkan oleh Operator!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.acceptOrder(orderId, driverName)
                        }
                    }
                )
            }
            "riwayat" -> {
                DriverHistoryContent(orders = myAcceptedOrders)
            }
            "chat" -> {
                LiveChatView(
                    messages = chats,
                    currentUserRole = "driver",
                    currentUserName = driverName,
                    onSendMessage = { viewModel.sendChat(it) }
                )
            }
        }
    }
}

@Composable
private fun DriverIncomingOrders(
    orders: List<OrderEntity>,
    driverName: String,
    isPermitted: Boolean,
    onAccept: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface(
            color = SunsetOrangeLight,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBus,
                    contentDescription = null,
                    tint = SunsetOrangeDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "⚡ SISTEM SIAPA CEPAT DIA DAPAT!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SunsetOrangeDark
                    )
                    Text(
                        text = "Klik 'SAYA TERIMA!' pada pesanan yang cocok sebelum diambil driver lain.",
                        fontSize = 11.sp,
                        color = SunsetOrangeDark.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = SunsetOrangeSecondary.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Belum Ada Pesanan Masuk",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Pesanan baru dari wisatawan akan otomatis muncul di sini.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        currentRole = "driver",
                        isDriverPermitted = isPermitted,
                        onAccept = { onAccept(order.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverHistoryContent(
    orders: List<OrderEntity>
) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "📭 Belum ada pesanan yang Anda terima",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orders, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    currentRole = "driver",
                    isDriverPermitted = true
                )
            }
        }
    }
}
