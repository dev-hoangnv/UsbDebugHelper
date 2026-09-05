# USB Debug Helper

Ứng dụng hỗ trợ bật/tắt nhanh tính năng **Gỡ lỗi qua USB (USB Debugging)** trực tiếp từ màn hình hoặc qua phím tắt trên thanh trạng thái (Quick Settings Tile).

---

## ⚠️ Yêu cầu quan trọng: Cấp quyền qua ADB

Để ứng dụng có thể bật hoặc tắt cài đặt hệ thống, bạn **bắt buộc phải cấp quyền một lần duy nhất qua máy tính bằng lệnh ADB**.

### 1. Tại sao cần chạy lệnh này?

* **Bảo mật của Android:** Tính năng gỡ lỗi USB nằm trong bảng dữ liệu hệ thống `Settings.Global.ADB_ENABLED`. Để thay đổi cài đặt này, ứng dụng cần quyền `android.permission.WRITE_SECURE_SETTINGS`.
* **Cơ chế cấp quyền:** Đây là quyền cấp hệ thống (**Signature/System Permission**). Google **không cho phép** ứng dụng thông thường tự hiển thị hộp thoại xin quyền trực tiếp trên màn hình điện thoại như Camera hay Vị trí.
* **Không cần Root:** Bằng cách sử dụng cầu nối gỡ lỗi Android (ADB) từ máy tính, tiến trình Shell cấp cao (`UID 2000`) sẽ gán quyền trực tiếp cho ứng dụng mà không cần can thiệp sâu hay bẻ khóa thiết bị (root).

---

## 🛠 Hướng dẫn cấp quyền từng bước

### Bước 1: Chuẩn bị trên điện thoại

1. Mở **Cài đặt (Settings)** > **Thông tin điện thoại (About phone)**.
2. Chạm liên tục **7 lần** vào dòng **Số hiệu bản dựng (Build number)** cho đến khi máy báo *Bạn đã là nhà phát triển*.
3. Quay lại danh mục Cài đặt > **Hệ thống (System)** > **Tùy chọn nhà phát triển (Developer Options)**:
   * Bật mục **Gỡ lỗi qua USB (USB Debugging)**.
   * *(Đối với máy Xiaomi / HyperOS / MIUI)*: Bật thêm mục **Gỡ lỗi USB (Cài đặt bảo mật)**.

### Bước 2: Kết nối máy tính

1. Cắm cáp USB kết nối điện thoại với máy tính.
2. Trên màn hình điện thoại sẽ xuất hiện thông báo hỏi tin cậy máy tính:
   * Tích chọn **Luôn cho phép từ máy tính này (Always allow from this computer)**.
   * Nhấn **OK / Cho phép**.

### Bước 3: Chạy lệnh trên máy tính

1. Mở công cụ gõ lệnh trên máy tính:
   * **Windows:** Mở `Command Prompt` (cmd) hoặc `PowerShell`.
   * **macOS / Linux:** Mở `Terminal`.

2. *(Tùy chọn)* Kiểm tra máy tính đã nhận thiết bị hay chưa bằng lệnh:
   ```bash
   adb devices
   ```
   *(Nếu danh sách hiện mã thiết bị kèm chữ `device` là kết nối thành công).*

3. Sao chép và chạy lệnh cấp quyền:
   ```bash
   adb shell pm grant com.wangdev.usbdebughelper android.permission.WRITE_SECURE_SETTINGS
   ```

4. Nếu Terminal không báo lỗi (tự xuống dòng trống mới) nghĩa là quá trình cấp quyền đã thành công.
