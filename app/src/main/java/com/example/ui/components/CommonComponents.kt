package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.OrderEntity
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
import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount.toLong()).replace(",00", "")
}

fun formatRupiahSimple(amount: Int): String {
    return "Rp " + String.format(Locale("id", "ID"), "%,d", amount).replace(",", ".")
}

@Composable
fun TopAppHeader(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    badgeText: String? = null,
    badgeColor: Color = SunsetOrangeSecondary,
    actions: @Composable (() -> Unit)? = null
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(OceanBluePrimary, OceanBlueDark)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(SunsetOrangeSecondary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBus,
                                contentDescription = "Logo",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (badgeText != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = badgeColor,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = badgeText,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                if (actions != null) {
                    actions()
                }
            }
        }
    }
}

@Composable
fun NotificationToastBanner(
    message: String?,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = !message.isNullOrBlank(),
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        if (!message.isNullOrBlank()) {
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(3500)
                onDismiss()
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = OceanBlueDark,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = SunsetOrangeSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactInfoCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = OceanBlueSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📞 Layanan Pelanggan & Informasi Wisata Lombok",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = OceanBlueDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:081907951938")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "CS: 081907951938", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Telepon CS",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CS: 081907951938", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        try {
                            val uri = Uri.parse("https://api.whatsapp.com/send?phone=6282230336091&text=Halo%20Admin%20Odong%20Odong%20Lombok,%20saya%20ingin%20tanya%20layanan%20wisata")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "WA: 082230336091", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSuccess),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WA: 082230336091", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: OrderEntity,
    currentRole: String, // "pelanggan", "driver", "operator", "admin"
    isDriverPermitted: Boolean = true,
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val statusBg = when (order.status) {
        "menunggu" -> AmberGoldLight
        "diterima" -> EmeraldGreenLight
        "ditolak" -> CoralRedLight
        else -> OceanBlueLight
    }

    val statusBorder = when (order.status) {
        "menunggu" -> AmberGoldWarning
        "diterima" -> EmeraldGreenSuccess
        "ditolak" -> CoralRedAlert
        else -> OceanBluePrimary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Customer Name & Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(OceanBlueLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (order.nama.isNotEmpty()) order.nama.take(1).uppercase() else "P",
                            fontWeight = FontWeight.Bold,
                            color = OceanBlueDark,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = order.nama,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "📞 ${order.hp}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusBorder.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = when (order.status) {
                            "menunggu" -> "⏳ MENUNGGU"
                            "diterima" -> "✅ DITERIMA"
                            "ditolak" -> "❌ DITOLAK"
                            else -> order.status.uppercase()
                        },
                        color = statusBorder,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = CardBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Route details
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = OceanBluePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${order.asal}  ➔  ${order.tujuan}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = OceanBlueDark
                )
            }

            if (order.almtAsal.isNotBlank()) {
                Text(
                    text = "🏠 Jemput: ${order.almtAsal}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 24.dp, top = 2.dp)
                )
            }
            if (order.almtTujuan.isNotBlank()) {
                Text(
                    text = "🎯 Tujuan: ${order.almtTujuan}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 24.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Unit count and Price Details
            Surface(
                color = OceanBlueSurface,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🚐 ${order.jumlah} Unit Odong-Odong",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Dasar: ${formatRupiahSimple(order.hargaDasar)}/unit",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (order.hargaTawar > 0) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "🔥 Harga Nego/Tawar:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SunsetOrangeDark
                            )
                            Text(
                                text = "${formatRupiahSimple(order.hargaTawar)}/unit",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SunsetOrangeDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = OceanBlueLight)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💰 TOTAL AKHIR:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBlueDark
                        )
                        Text(
                            text = formatRupiahSimple(order.totalAkhir),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OceanBluePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date, Time, Payment & Driver info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = SunsetOrangeDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${order.tgl} | ${order.jam}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = SunsetOrangeLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "💳 ${order.bayar.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunsetOrangeDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (order.status == "diterima" && order.oleh.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = EmeraldGreenLight,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✅ Diterima oleh: ${order.oleh}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreenSuccess,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (order.catatan.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📝 Catatan: ${order.catatan}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Action Buttons
            if (order.status == "menunggu") {
                Spacer(modifier = Modifier.height(10.dp))
                if (currentRole == "driver") {
                    if (isDriverPermitted) {
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSuccess),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("✅ SAYA TERIMA! (Siapa Cepat)", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            color = CoralRedLight,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🚫 Anda belum diizinkan Operator untuk mengambil pesanan",
                                color = CoralRedAlert,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                } else if (currentRole == "operator" || currentRole == "admin") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSuccess),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("✅ TERIMA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = CoralRedAlert),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("❌ TOLAK", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = onDelete,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = CoralRedAlert,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            } else if (currentRole == "operator" || currentRole == "admin") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus",
                            tint = CoralRedAlert,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hapus Riwayat", fontSize = 11.sp, color = CoralRedAlert)
                    }
                }
            }
        }
    }
}

@Composable
fun LiveChatView(
    messages: List<ChatMessageEntity>,
    currentUserRole: String,
    currentUserName: String,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Quick Reply Suggestions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = when (currentUserRole) {
                "pelanggan" -> listOf("Apakah armada siap?", "Ada sound system?", "Bisa jemput sekarang?")
                "driver" -> listOf("Armada siap berangkat!", "Sudah tiba di lokasi jemput", "Siap meluncur!")
                else -> listOf("Pesanan telah kami proses", "Driver dalam perjalanan", "Hubungi CS jika butuh bantuan")
            }
            suggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = { onSendMessage(suggestion) },
                    label = { Text(suggestion, fontSize = 11.sp) }
                )
            }
        }

        // Chat Bubble List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💬 Belum ada pesan chat.\nMulai kirim pesan untuk berdiskusi!",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                val isMe = msg.pengirimPeran == currentUserRole &&
                        (msg.pengirimNama == currentUserName || currentUserName.isBlank())

                val bubbleBg = when (msg.pengirimPeran) {
                    "pelanggan" -> if (isMe) OceanBluePrimary else OceanBlueLight
                    "driver" -> if (isMe) SunsetOrangeSecondary else SunsetOrangeLight
                    "operator" -> if (isMe) RoyalPurpleAdmin else RoyalPurpleLight
                    "admin" -> if (isMe) RoyalPurpleAdmin else RoyalPurpleLight
                    else -> Color(0xFFE2E8F0)
                }

                val textColor = if (isMe) Color.White else MaterialTheme.colorScheme.onBackground

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = when (msg.pengirimPeran) {
                                "pelanggan" -> "👤 ${msg.pengirimNama}"
                                "driver" -> "🚐 ${msg.pengirimNama}"
                                "operator" -> "📊 ${msg.pengirimNama}"
                                "admin" -> "👑 ${msg.pengirimNama}"
                                else -> msg.pengirimNama
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = msg.waktu,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 14.dp,
                                    topEnd = 14.dp,
                                    bottomStart = if (isMe) 14.dp else 2.dp,
                                    bottomEnd = if (isMe) 2.dp else 14.dp
                                )
                            )
                            .background(bubbleBg)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = msg.pesan,
                            color = textColor,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Input Field Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Tulis pesan...", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBluePrimary,
                        unfocusedBorderColor = CardBorder
                    ),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onSendMessage(textInput)
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .background(OceanBluePrimary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Kirim",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
