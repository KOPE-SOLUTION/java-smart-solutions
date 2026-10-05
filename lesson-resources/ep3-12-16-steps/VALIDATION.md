# ผลตรวจชุดบทเรียน EP3.11–3.16

ตรวจเมื่อ 2026-10-05 บน Windows x64, JDK 21.0.8, JavaFX 21.0.10 ใช้พื้นที่ชั่วคราวแยกจากงานของผู้เรียน

| รายการ | ผล |
|---|---|
| คอมไพล์ Source ของจุดรันย่อย 21 ชุด | ผ่าน |
| โหลด FXML จริงและตรวจ Event/ตาราง/Binding | ผ่านทุกชุด |
| Search, AND filter, maintenance filter, SortedList | ผ่าน |
| เพิ่ม ลบ บำรุง เลือก ยกเลิก และบันทึกชื่อ/ตำแหน่ง | ผ่าน |
| Sensor, การหยุด Timeline/ขอยกเลิก Task และรักษาร่างชื่อระหว่าง Auto | ผ่าน |
| Core Test ก่อนเพิ่ม Edit | PASS: 6 tests |
| Core Test หลังเพิ่ม Edit รวมการตรวจข้อมูลก่อนเปลี่ยนทั้งสอง Field | PASS: 7 tests |
| prepare-lesson: 30 Mapping, ปฏิเสธการทับงาน, สำรองตรงทุก byte, Source ไม่ครบ | ผ่าน 150 ข้อ |
| Maven test-compile/exec และ javafx:jlink ของชุดจบ | ผ่าน |
| เปิด DesktopApp/FXML/CSS ด้วย Java ใน Runtime Image ที่สร้าง | ผ่าน |

ข้อจำกัด: ตรวจ FXML ผลลัพธ์ของ EP3.16 แล้ว แต่ยังไม่ได้ทดสอบการคลิกใน Scene Builder แต่ละเวอร์ชันหรือส่ง Runtime ไปเปิดอีกเครื่อง ไม่ได้อ้างผลบน macOS/Linux

ตัวอย่างยังมีคำเตือน unchecked จาก TableView/varargs ของบทเดิม ไม่ใช่ Compile Error
