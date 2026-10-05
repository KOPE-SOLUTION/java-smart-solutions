# EP 3.11 — FXML และ Controller

แยกหน้าจอออกจากการทำงาน โดยใช้ Model, Service และงาน Sensor จาก EP3.10

FXML บอกว่า **หน้าจอมีอะไรและวางตรงไหน** ส่วน Controller บอกว่า **เมื่อกดปุ่มแล้วทำอะไร**

เรียนตามลำดับ **ตอนที่ 1 → 2 → 3 → 4 → 5** แต่ละตอนมีขั้นลงมือทำและจุดรันของตัวเอง

| ตอน | เนื้อหา | จุดรันตรวจผล |
|---|---|---|
| [ตอนที่ 1](ep11a-fxml-window.md) | เปิดหน้าต่างด้วย FXML | เปิดหน้าต่างได้ |
| [ตอนที่ 2](ep11b-fxml-controller.md) | เชื่อม FXML กับ Controller | กดปุ่มแล้วข้อความเปลี่ยน |
| [ตอนที่ 3](ep11c-service-table-summary.md) | แสดงตารางและ Summary จาก Service | ตาราง 3 แถว → เพิ่ม Summary แล้วรันอีกครั้ง |
| [ตอนที่ 4](ep11d-form-crud.md) | เชื่อม Form กับการจัดการเครื่องจักร | เพิ่มเครื่องได้ → เพิ่มลบ/บำรุงแล้วรันอีกครั้ง |
| [ตอนที่ 5](ep11e-sensor-lifecycle.md) | นำ Sensor และ Auto กลับมา | Sensor และ Auto ทำงาน พร้อมปิดงานได้ |

เริ่มที่ [ตอนที่ 1](ep11a-fxml-window.md) ทำและรันทีละขั้น ไม่ต้องทำทั้ง EP ในครั้งเดียว

เก็บ `DashboardApp.java` จาก EP3.10 ไว้เทียบระหว่างย้ายหน้าจอ ไม่ลบและไม่ดึง Source ฉบับเต็มจาก Git มาแทน

[ชุดเริ่มต้นและชุดจบแต่ละตอน](../../lesson-resources/ep3-11-steps/README.md) · [EP3.12](ep12-thai-package-iot.md)

<details>
<summary>อ่านเพิ่มเติม</summary>

[Introduction to FXML — OpenJFX](https://openjfx.io/javadoc/21/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html)

</details>
