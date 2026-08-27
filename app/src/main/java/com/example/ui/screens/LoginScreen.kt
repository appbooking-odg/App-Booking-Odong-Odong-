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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TopAppHeader
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CoralRedAlert
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
fun LoginScreen(
    role: AppRole,
    viewModel: OdongViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Form states
    var namaInput by remember { mutableStateOf("") }
    var hpInput by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var passInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val screenTitle = when (role) {
        AppRole.PELANGGAN -> "👤 LOGIN PELANGGAN"
        AppRole.DRIVER -> "🚐 LOGIN DRIVER"
        AppRole.OPERATOR -> "📊 LOGIN OPERATOR"
        AppRole.ADMIN -> "👑 LOGIN ADMIN"
        else -> "LOGIN"
    }

    val primaryColor = when (role) {
        AppRole.PELANGGAN -> OceanBluePrimary
        AppRole.DRIVER -> SunsetOrangeSecondary
        AppRole.OPERATOR -> RoyalPurpleAdmin
        AppRole.ADMIN -> CoralRedAlert
        else -> OceanBluePrimary
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        TopAppHeader(
            title = screenTitle,
            subtitle = "Masuk ke Sistem Odong-Odong Lombok",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (role == AppRole.PELANGGAN) {
                        // Customer Form
                        Text(
                            text = "Data Pelanggan",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBlueDark
                        )
                        Text(
                            text = "Masukkan nama & nomor WhatsApp untuk mulai memesan armada odong-odong wisata.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        Text("Nama Lengkap", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = namaInput,
                            onValueChange = { namaInput = it },
                            placeholder = { Text("Contoh: Ibu Rina / Bpk. Hadi") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = OceanBluePrimary)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_customer_name"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Nomor WhatsApp / HP", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = hpInput,
                            onValueChange = { hpInput = it },
                            placeholder = { Text("Contoh: 081234567890") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = OceanBluePrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_customer_phone"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (namaInput.isBlank() || hpInput.isBlank()) {
                                    Toast.makeText(context, "⚠️ Isi Nama & No HP terlebih dahulu!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.loginCustomer(namaInput, hpInput)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_login_customer"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("✅ MASUK SEBAGAI PELANGGAN", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                namaInput = "Ibu Siti Aisyah"
                                hpInput = "081907123456"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("⚡ Contoh Data Wisatawan", fontSize = 12.sp, color = OceanBluePrimary)
                        }

                    } else {
                        // Driver / Operator / Admin Login Form
                        Text(
                            text = "Autentikasi Akun",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                        Text(
                            text = "Silakan masukkan username dan password akun Anda.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        Text("Username", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            placeholder = { Text("Masukkan username") },
                            leadingIcon = {
                                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = primaryColor)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_username"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Password", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = passInput,
                            onValueChange = { passInput = it },
                            placeholder = { Text("Masukkan password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = primaryColor)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_password"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (userInput.isBlank() || passInput.isBlank()) {
                                    Toast.makeText(context, "⚠️ Isi Username dan Password!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isLoading = true
                                if (role == AppRole.DRIVER) {
                                    viewModel.loginDriver(userInput, passInput) { success, msg ->
                                        isLoading = false
                                        if (!success) {
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    viewModel.loginOperatorAdmin(role, userInput, passInput) { success, msg ->
                                        isLoading = false
                                        if (!success) {
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_login_submit"),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                            } else {
                                Text("✅ MASUK", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Default Account Credentials Info
                        Surface(
                            color = OceanBlueSurface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🔑 Akun Bawaan Sistem (Demo):",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = OceanBlueDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                when (role) {
                                    AppRole.DRIVER -> {
                                        Text("🚐 Driver: driver / dr123 (Pak Suardi)", fontSize = 12.sp)
                                        Text("🚐 Driver 2: ahmad / dr123 (Mas Ahmad)", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        OutlinedButton(
                                            onClick = {
                                                userInput = "driver"
                                                passInput = "dr123"
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("⚡ Isi Akun Driver Otomatis", fontSize = 11.sp)
                                        }
                                    }
                                    AppRole.OPERATOR -> {
                                        Text("📊 Operator: operator / op123", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        OutlinedButton(
                                            onClick = {
                                                userInput = "operator"
                                                passInput = "op123"
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("⚡ Isi Akun Operator Otomatis", fontSize = 11.sp)
                                        }
                                    }
                                    AppRole.ADMIN -> {
                                        Text("👑 Admin Utama: admin / admin123", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        OutlinedButton(
                                            onClick = {
                                                userInput = "admin"
                                                passInput = "admin123"
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("⚡ Isi Akun Admin Otomatis", fontSize = 11.sp)
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
