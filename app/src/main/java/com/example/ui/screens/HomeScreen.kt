package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ContactInfoCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CoralRedAlert
import com.example.ui.theme.OceanBlueDark
import com.example.ui.theme.OceanBlueLight
import com.example.ui.theme.OceanBluePrimary
import com.example.ui.theme.OceanBlueSurface
import com.example.ui.theme.RoyalPurpleAdmin
import com.example.ui.theme.RoyalPurpleLight
import com.example.ui.theme.SunsetOrangeLight
import com.example.ui.theme.SunsetOrangeSecondary
import com.example.ui.viewmodel.AppRole

@Composable
fun HomeScreen(
    onSelectRole: (AppRole) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(scrollState)
    ) {
        // Hero Image Banner & Header Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_odong),
                contentDescription = "Odong-Odong Lombok Hero",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = SunsetOrangeSecondary,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "WISATA KELILING LOMBOK",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Text(
                    text = "🚐 ODONG-ODONG LOMBOK",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Sistem Multi Driver Online & Carter Wisata",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pilih Masuk Sebagai:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Role Card 1: PELANGGAN
            RoleSelectionCard(
                title = "👤 PELANGGAN / WISATAWAN",
                description = "Booking Odong-Odong wisata, pilih rute, nego harga per unit & lacak pesanan real-time",
                icon = Icons.Default.Person,
                primaryColor = OceanBluePrimary,
                bgColor = OceanBlueSurface,
                badgeText = "Order Instan",
                testTag = "role_pelanggan",
                onClick = { onSelectRole(AppRole.PELANGGAN) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Role Card 2: DRIVER
            RoleSelectionCard(
                title = "🚐 PORTAL DRIVER",
                description = "Terima pesanan masuk (Siapa Cepat Dia Dapat), kelola jadwal & cek izin operasional",
                icon = Icons.Default.DirectionsBus,
                primaryColor = SunsetOrangeSecondary,
                bgColor = SunsetOrangeLight,
                badgeText = "Multi Driver",
                testTag = "role_driver",
                onClick = { onSelectRole(AppRole.DRIVER) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Role Card 3: OPERATOR
            RoleSelectionCard(
                title = "📊 OPERATOR PUSAT",
                description = "Pusat kontrol dispatch, kelola daftar rute, harga dasar, serta izin driver",
                icon = Icons.Default.ManageAccounts,
                primaryColor = RoyalPurpleAdmin,
                bgColor = RoyalPurpleLight,
                badgeText = "Kontrol Dispatch",
                testTag = "role_operator",
                onClick = { onSelectRole(AppRole.OPERATOR) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Role Card 4: ADMIN
            RoleSelectionCard(
                title = "👑 ADMIN UTAMA",
                description = "Akses manajemen sistem penuh, kelola akun staf admin & analisis omset",
                icon = Icons.Default.AdminPanelSettings,
                primaryColor = CoralRedAlert,
                bgColor = Color(0xFFFFEBEE),
                badgeText = "Super Admin",
                testTag = "role_admin",
                onClick = { onSelectRole(AppRole.ADMIN) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Contact CS card
            ContactInfoCard()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun RoleSelectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    primaryColor: Color,
    bgColor: Color,
    badgeText: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(bgColor, CircleShape)
                    .border(1.dp, primaryColor.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Surface(
                        color = primaryColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
