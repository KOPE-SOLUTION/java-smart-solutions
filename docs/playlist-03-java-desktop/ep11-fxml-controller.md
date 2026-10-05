# EP 3.11 — FXML และ Controller

เปลี่ยนวิธีสร้างหน้าจอทีละส่วน โดยใช้ Model, Service และงาน Sensor จาก EP3.10

FXML บอกว่า **หน้าจอมีอะไรและวางตรงไหน** ส่วน Controller บอกว่า **เมื่อกดปุ่มแล้วทำอะไร** เริ่มจากหน้าต่างเล็กก่อน แล้วนำ Dashboard เดิมกลับมา

| ขั้น | ทำแล้วรันเห็นอะไร |
|---|---|
| [1. เปิดหน้าต่าง FXML](ep11-steps/01-window.md) | เปิดหน้าต่างได้ |
| [2. เชื่อมปุ่มกับ Controller](ep11-steps/02-button.md) | กดปุ่มแล้วข้อความเปลี่ยน |
| [3. แสดงตารางจาก Service](ep11-steps/03-table.md) | เห็นเครื่องจักร 3 แถว |
| [4. นำ Summary กลับมา](ep11-steps/04-summary.md) | ยอดทั้งหมด 3 และต้องบำรุง 2 |
| [5. เพิ่มเครื่องจักรจาก Form](ep11-steps/05-add.md) | เพิ่ม M-004 แล้วทั้งหมดเป็น 4 |
| [6. ลบและบำรุงรักษา](ep11-steps/06-actions.md) | บำรุง M-003 แล้วชั่วโมงเป็น 0 |
| [7. นำ Sensor และ Auto กลับมา](ep11-steps/07-sensor.md) | Sensor เปลี่ยนและหยุดงานเมื่อปิดหน้าต่าง |

เริ่มที่ [ขั้นที่ 1](ep11-steps/01-window.md) แล้วหยุดตรวจผลทุกขั้น ไม่ต้องทำทั้งบทในครั้งเดียว

เก็บ `DashboardApp.java` จาก EP3.10 ไว้เทียบระหว่างย้ายหน้าจอ ไม่ลบและไม่ดึง Source ฉบับเต็มจาก Git มาแทน

[ชุดเริ่มต้นและชุดจบแต่ละขั้น](../../lesson-resources/ep3-11-steps/README.md) · [EP3.12](ep12-thai-package-iot.md)

<details>
<summary>อ่านเพิ่มเติม</summary>

[Introduction to FXML — OpenJFX](https://openjfx.io/javadoc/21/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html)

</details>
