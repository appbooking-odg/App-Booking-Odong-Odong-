package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.NotificationToastBanner
import com.example.ui.screens.DriverPortalScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OperatorAdminPortalScreen
import com.example.ui.screens.PelangganPortalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppRole
import com.example.ui.viewmodel.OdongViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OdongApp()
            }
        }
    }
}

@Composable
fun OdongApp(
    viewModel: OdongViewModel = viewModel()
) {
    val currentRole by viewModel.currentRole.collectAsState()
    var selectedLoginRole by remember { mutableStateOf<AppRole?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { msg ->
            toastMessage = msg
        }
    }

    // Handle Android system back press
    BackHandler(enabled = currentRole != AppRole.NONE || selectedLoginRole != null) {
        if (selectedLoginRole != null && currentRole == AppRole.NONE) {
            selectedLoginRole = null
        } else if (currentRole != AppRole.NONE) {
            viewModel.logout()
            selectedLoginRole = null
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = Pair(currentRole, selectedLoginRole),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenNavigation"
            ) { (role, loginRole) ->
                when {
                    role == AppRole.PELANGGAN -> {
                        PelangganPortalScreen(viewModel = viewModel)
                    }
                    role == AppRole.DRIVER -> {
                        DriverPortalScreen(viewModel = viewModel)
                    }
                    role == AppRole.OPERATOR || role == AppRole.ADMIN -> {
                        OperatorAdminPortalScreen(viewModel = viewModel)
                    }
                    loginRole != null -> {
                        LoginScreen(
                            role = loginRole,
                            viewModel = viewModel,
                            onBack = { selectedLoginRole = null }
                        )
                    }
                    else -> {
                        HomeScreen(
                            onSelectRole = { chosenRole ->
                                selectedLoginRole = chosenRole
                            }
                        )
                    }
                }
            }

            // In-app Floating Toast/Notification Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                NotificationToastBanner(
                    message = toastMessage,
                    onDismiss = { toastMessage = null }
                )
            }
        }
    }
}
