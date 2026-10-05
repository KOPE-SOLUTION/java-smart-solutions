# EP3.11 — ขั้นที่ 2: เชื่อมปุ่มกับ Controller

เป้าหมาย: กดปุ่มแล้วข้อความเปลี่ยน

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-2 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository หลังเพิ่มโค้ดครบขั้น:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

## 1. สร้าง Controller

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

## 2. เชื่อมหน้าจอ

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


## ลองทำเอง

เปลี่ยนข้อความเมื่อกดปุ่มเป็น “เชื่อมสำเร็จ”

<details>
<summary>เฉลย</summary>

เปลี่ยนข้อความใน `statusLabel.setText(...)` ของ `handleHello()`

</details>



<details>
<summary>ดูโค้ดครบขั้นเพื่อเทียบ</summary>

[ชุดจบขั้นที่ 2](../../../lesson-resources/ep3-11-steps/02-button/) เป็นผลจากขั้นนี้ ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](01-window.md) · [สารบัญ EP3.11](../ep11-fxml-controller.md) · [ถัดไป](03-table.md)
