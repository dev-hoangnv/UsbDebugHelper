package com.wangdev.usbdebughelper

import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class AdbTileService : TileService() {

    // Được gọi khi người dùng kéo thanh thông báo xuống
    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    // Xử lý khi người dùng chạm vào nút Tile
    override fun onClick() {
        super.onClick()

        val isCurrentlyEnabled = isAdbEnabled()
        val targetValue = if (isCurrentlyEnabled) 0 else 1

        try {
            Settings.Global.putInt(
                contentResolver,
                Settings.Global.ADB_ENABLED,
                targetValue
            )
            updateTileState()
        } catch (e: SecurityException) {
            Toast.makeText(
                this,
                "Chưa cấp quyền WRITE_SECURE_SETTINGS!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Kiểm tra trạng thái ADB hiện tại
    private fun isAdbEnabled(): Boolean {
        return Settings.Global.getInt(
            contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        ) == 1
    }

    // Cập nhật trạng thái hiển thị (sáng/tối) của Tile
    private fun updateTileState() {
        val tile = qsTile ?: return
        val enabled = isAdbEnabled()

        tile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.subtitle = if (enabled) "Đang Bật" else "Đang Tắt"
        tile.updateTile()
    }
}