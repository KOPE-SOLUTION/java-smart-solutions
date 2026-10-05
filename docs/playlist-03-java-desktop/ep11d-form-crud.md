# EP3.11 ตอนที่ 4 — เชื่อม Form กับการจัดการเครื่องจักร

เป้าหมาย: เพิ่ม ลบ และบำรุงรักษาเครื่องจักรผ่าน Form

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากตอนก่อนหน้า ใช้งานเดิมได้เลย หากต้องการเริ่มตอนนี้ใหม่ ให้ปิดแอปและบันทึกไฟล์ก่อนรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-4 -BackupExisting
```

คำสั่งสำรองโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมจุดเริ่ม ดู[ชุดพร้อมเรียน](../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ แล้วใช้คำสั่งนี้จากโฟลเดอร์หลัก Repository เมื่อถึงจุดรัน:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ตอนนี้มี 2 จุดรัน: ทำขั้นที่ 1 ให้เห็นผลก่อน แล้วค่อยเพิ่มขั้นที่ 2

<a id="step-1"></a>

## ขั้นที่ 1: เพิ่มเครื่องจักรจาก Form

### 1. เพิ่ม Form

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

### 2. เชื่อมกับ Method เพิ่มเครื่องเดิม

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


### ลองทำเอง

ลองรหัส M-001 ซ้ำ แล้วทำนายผล

<details>
<summary>เฉลย</summary>

Service ปฏิเสธรหัสซ้ำ แสดง Alert และจำนวนไม่เพิ่ม; ไม่ต้องเพิ่มกฎซ้ำใน Controller

</details>

<details>
<summary>เทียบโค้ดที่จุดรันนี้</summary>

[ชุดจบขั้นที่ 1](../../lesson-resources/ep3-11-steps/05-add/) ใช้ตรวจหลังรัน ก่อนทำขั้นที่ 2

</details>

---

<a id="step-2"></a>

## ขั้นที่ 2: ลบและบำรุงรักษา

### 1. เพิ่มปุ่มที่ใช้กับแถวที่เลือก

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มหลังปุ่มเพิ่มเครื่องจักร ภายใน VBox `actionButtons`:

```xml
<Button text="ลบรายการที่เลือก" onAction="#handleDeleteMachine"/>
<Button text="บำรุงเสร็จแล้ว" onAction="#handleMaintenance"/>
```

### 2. นำ Method เดิมมาใช้

ใน `src/main/java/smartfactory/ui/DashboardController.java` คัดลอก `handleDeleteMachine()` และ `handleMaintenance()` จาก DashboardApp เดิมมาไว้หลัง `handleAddMachine()` เพิ่ม `@FXML` เหนือทั้งสอง Method

ไม่คัดลอก `new Button(...)` หรือ `setOnAction(...)` เพราะสองส่วนนี้อยู่ใน FXML แล้ว

**ก่อนรัน:** บำรุง M-003 แล้วจำนวนเครื่องจักรจะลดไหม?

รันด้วยคำสั่งด้านบน แล้วเริ่มแอปใหม่ เลือก M-003 กดบำรุง จากนั้นเลือก M-001 แล้วกดลบ

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

บำรุง M-003: ชั่วโมง 0 สถานะปิดเครื่อง ทั้งหมดยัง 3 ต้องบำรุงเหลือ 1; ลบ M-001: ทั้งหมดเหลือ 2

</details>


### ลองทำเอง

กดบำรุงโดยยังไม่เลือกแถว ควรเกิดอะไร?

<details>
<summary>เฉลย</summary>

แสดง Alert ให้เลือกเครื่องจักร ไม่เปลี่ยนข้อมูล เพราะ Method เดิมตรวจ `selected == null` ไว้แล้ว

</details>

<details>
<summary>เทียบโค้ดเมื่อจบตอน</summary>

[ชุดจบตอนที่ 4](../../lesson-resources/ep3-11-steps/06-actions/) เป็นผลหลังทำครบตอน ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](ep11c-service-table-summary.md) · [สารบัญ EP3.11](ep11-fxml-controller.md) · [ถัดไป](ep11e-sensor-lifecycle.md)
