package com.wangdev.usbdebughelper

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UsbDebugScreen()
                }
            }
        }
    }
}

@Composable
fun UsbDebugScreen() {
    val context = LocalContext.current

    fun readAdbStatus(): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        ) == 1
    }

    var isAdbEnabled by remember { mutableStateOf(readAdbStatus()) }

    fun updateAdbState(enable: Boolean) {
        val targetValue = if (enable) 1 else 0
        try {
            Settings.Global.putInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED,
                targetValue
            )
            isAdbEnabled = readAdbStatus()
            val msg = if (enable) "Đã bật gỡ lỗi USB" else "Đã tắt gỡ lỗi USB"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(
                context,
                "Lỗi: Cần cấp quyền WRITE_SECURE_SETTINGS qua ADB",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openDeveloperOptions() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Thiết bị chưa kích hoạt Tuỳ chọn nhà phát triển!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trạng thái: " + if (isAdbEnabled) "ĐANG BẬT" else "ĐANG TẮT",
            style = MaterialTheme.typography.titleMedium,
            color = if (isAdbEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { updateAdbState(true) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bật gỡ lỗi USB")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { updateAdbState(false) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Tắt gỡ lỗi USB")
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        OutlinedButton(
            onClick = { openDeveloperOptions() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mở Tùy chọn nhà phát triển")
        }
    }
}