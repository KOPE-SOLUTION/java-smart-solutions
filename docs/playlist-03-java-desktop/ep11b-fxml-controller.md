# EP3.11 ตอนที่ 2 — เชื่อม FXML กับ Controller

เป้าหมาย: กดปุ่มแล้วข้อความเปลี่ยน

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากตอนก่อนหน้า ใช้งานเดิมได้เลย หากต้องการเริ่มตอนนี้ใหม่ ให้ปิดแอปและบันทึกไฟล์ก่อนรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-2 -BackupExisting
```

คำสั่งสำรองโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมจุดเริ่ม ดู[ชุดพร้อมเรียน](../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ แล้วใช้คำสั่งนี้จากโฟลเดอร์หลัก Repository เมื่อถึงจุดรัน:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เพิ่มโค้ดให้ครบขั้นด้านล่าง แล้วรันตรวจผล

<a id="step-1"></a>

## ขั้นที่ 1: เชื่อมปุ่มกับ Controller

### 1. สร้าง Controller

สร้าง `src/main/java/smartfactory/ui/DashboardController.java`:

```java
package smartfactory.ui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label statusLabel;

    @FXML
    private void handleHello() {
        statusLabel.setText("Controller รับการกดปุ่มแล้ว");
    }
}
```

`@FXML` ให้ตัวโหลดเชื่อม Field และ Method นี้กับ FXML โดย `statusLabel` อ้างถึง Label ที่โหลดมา ไม่ต้อง `new Label()` ซ้ำ

### 2. เชื่อมหน้าจอ

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่ม `fx:controller` ในแท็กเปิด BorderPane:

```xml
<BorderPane xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="smartfactory.ui.DashboardController">
```

แทนที่บล็อก `center` เดิม:

```xml
<center>
    <VBox spacing="12">
        <Label fx:id="statusLabel" text="พร้อมใช้งาน"/>
        <Button text="ทดสอบปุ่ม" onAction="#handleHello"/>
    </VBox>
</center>
```

`fx:id` จับคู่กับชื่อ Field ส่วน `onAction` จับคู่กับชื่อ Method หลังเครื่องหมาย `#`

**ก่อนรัน:** ใครเปลี่ยนข้อความ: FXML หรือ Controller?

รันด้วยคำสั่งด้านบน แล้วกด “ทดสอบปุ่ม”

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ข้อความเปลี่ยนเป็น “Controller รับการกดปุ่มแล้ว”

</details>


### ลองทำเอง

เปลี่ยนข้อความเมื่อกดปุ่มเป็น “เชื่อมสำเร็จ”

<details>
<summary>เฉลย</summary>

เปลี่ยนข้อความใน `statusLabel.setText(...)` ของ `handleHello()`

</details>

<details>
<summary>เทียบโค้ดเมื่อจบตอน</summary>

[ชุดจบตอนที่ 2](../../lesson-resources/ep3-11-steps/02-button/) เป็นผลหลังทำครบตอน ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](ep11a-fxml-window.md) · [สารบัญ EP3.11](ep11-fxml-controller.md) · [ถัดไป](ep11c-service-table-summary.md)
