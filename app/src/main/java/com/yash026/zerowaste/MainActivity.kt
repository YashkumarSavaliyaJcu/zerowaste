package com.yash026.zerowaste

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.yash026.zerowaste.composables.MainScreen
import com.yash026.zerowaste.ui.theme.ZerowasteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZerowasteTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PermissionScreen()
                }
            }
        }
    }
}



// Permission functions
@Composable
fun PermissionScreen() {
    val context = LocalContext.current

    val imagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.POST_NOTIFICATIONS
    } else null

    var hasImagePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                imagePermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasNotificationPermission by remember {
        mutableStateOf(
            notificationPermission == null ||
                    ContextCompat.checkSelfPermission(
                        context,
                        notificationPermission
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val imagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasImagePermission = isGranted
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasImagePermission && hasNotificationPermission) {
            GreetingPreview()
        } else {
            if (!hasImagePermission) {
                Text("❌ Image permission not granted")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    imagePermissionLauncher.launch(imagePermission)
                }) {
                    Text("Request Image Permission")
                }
            }
            if (!hasNotificationPermission && notificationPermission != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("🔕 Notification permission not granted")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    notificationPermissionLauncher.launch(notificationPermission)
                }) {
                    Text("Request Notification Permission")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZerowasteTheme {
        val navController = rememberNavController()
        Surface(modifier = Modifier.fillMaxSize()) {
            MainScreen(navController = navController)
        }
    }
}
