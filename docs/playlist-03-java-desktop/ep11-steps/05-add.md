# EP3.11 — ขั้นที่ 5: เพิ่มเครื่องจักรจาก Form

เป้าหมาย: เพิ่ม M-004 แล้วทั้งหมดเป็น 4

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-5 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository หลังเพิ่มโค้ดครบขั้น:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

## 1. เพิ่ม Form

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่ม GridPane หลัง `</VBox>` ของพื้นที่ตาราง และก่อน `</SplitPane>`:

```xml
<GridPane fx:id="machineForm" hgap="10" vgap="10">
                <padding><Insets top="24" right="24" bottom="24" left="24"/></padding>
                <Label text="รหัสเครื่องจักร:" GridPane.rowIndex="0"/>
                <TextField fx:id="idField" promptText="เช่น M-004" GridPane.columnIndex="1" GridPane.rowIndex="0"/>
                <Label text="ชื่อเครื่องจักร:" GridPane.rowIndex="1"/>
                <TextField fx:id="nameField" promptText="เช่น Conveyor Motor" GridPane.columnIndex="1" GridPane.rowIndex="1"/>
                <Label text="ตำแหน่งเครื่องจักร:" GridPane.rowIndex="2"/>
                <TextField fx:id="locationField" promptText="เช่น Line B" GridPane.columnIndex="1" GridPane.rowIndex="2"/>
                <VBox fx:id="actionButtons" spacing="8" GridPane.columnIndex="1" GridPane.rowIndex="3">
                    <Button fx:id="addButton" text="เพิ่มเครื่องจักร" onAction="#handleAddMachine"/>
                </VBox>
            </GridPane>
```

GridPane ใช้คอลัมน์และแถวเหมือนตอนสร้างด้วย Java: ช่องกรอกอยู่คอลัมน์ 1 และปุ่มอยู่แถว 3

## 2. เชื่อมกับ Method เพิ่มเครื่องเดิม

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Import:

```java
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
```

เพิ่ม Field หลัง `machineCount`:

```java
@FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private Button addButton;
```

คัดลอก `requireText()`, `showError()`, `handleAddMachine()` จาก DashboardApp เดิมมาไว้หลัง `refreshDashboard()` ใน Controller ตามลำดับ

เพิ่มบรรทัด `@FXML` เหนือ `private void handleAddMachine()` โดยคงเนื้อหา Method เดิม ไม่คัดลอก `buildMachineForm()` เพราะสร้าง Form ใน FXML แล้ว

**ก่อนรัน:** ถ้าข้อมูลไม่ครบ จำนวนควรเพิ่มหรือไม่?

รันด้วยคำสั่งด้านบน แล้วกดเพิ่มขณะฟอร์มว่าง แล้วกรอก M-004 / Packaging Robot / Line B และกดเพิ่มอีกครั้ง

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ครั้งแรกมี Alert และยังทั้งหมด 3; ครั้งที่สองทั้งหมด 4 มี M-004 และช่องกรอกถูกล้าง

</details>


## ลองทำเอง

ลองรหัส M-001 ซ้ำ แล้วทำนายผล

<details>
<summary>เฉลย</summary>

Service ปฏิเสธรหัสซ้ำ แสดง Alert และจำนวนไม่เพิ่ม; ไม่ต้องเพิ่มกฎซ้ำใน Controller

</details>



<details>
<summary>ดูโค้ดครบขั้นเพื่อเทียบ</summary>

[ชุดจบขั้นที่ 5](../../../lesson-resources/ep3-11-steps/05-add/) เป็นผลจากขั้นนี้ ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](04-summary.md) · [สารบัญ EP3.11](../ep11-fxml-controller.md) · [ถัดไป](06-actions.md)
