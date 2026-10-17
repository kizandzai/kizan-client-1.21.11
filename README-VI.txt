KIZAN CLIENT — dự án khởi đầu cho Fabric 1.21.11
================================================

Đây là mã nguồn dự án, CHƯA phải file .jar đã build/kiểm thử.

Có sẵn:
- Menu mở bằng Right Shift.
- HUD tọa độ và FPS.
- Gửi tọa độ của chính người chơi đang dùng client tới Discord mỗi 60 giây,
  chỉ khi người dùng tự bật tính năng và lưu webhook trong menu.
- Các nút Player Scanner, Auto Totem, Auto Sprint hiện chỉ là công tắc giao diện
  (chưa triển khai hành vi).

Cần:
- JDK 21
- IntelliJ IDEA
- Kết nối internet lần đầu để Gradle tải Minecraft/Fabric dependencies.

Cách build:
1. Giải nén ZIP.
2. Mở thư mục KizanClient trong IntelliJ IDEA.
3. Chọn Gradle sync và chờ tải thư viện.
4. Mở Terminal ở thư mục dự án, chạy:
   .\gradlew.bat build

LƯU Ý:
- Gói này chưa kèm Gradle Wrapper JAR; nếu lệnh trên báo không tìm thấy gradlew.bat,
  hãy tạo project Fabric 1.21.11 từ https://fabricmc.net/develop/template/ rồi
  chép các file src/ và cấu hình tương ứng vào project đó, hoặc cài Gradle.
- File build đầu ra (nếu build thành công) thường nằm ở build/libs/.
- Discord webhook là bí mật như mật khẩu. Không gửi webhook cho người khác.
- Discord Coordinates mặc định TẮT; bật và bấm Save settings để dùng.
- Chỉ chia sẻ tọa độ của chính m và chỉ dùng trên server/kênh Discord m kiểm soát.
