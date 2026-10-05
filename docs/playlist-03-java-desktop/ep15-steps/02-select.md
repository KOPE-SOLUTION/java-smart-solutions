# EP3.15 — ขั้นที่ 2: เลือกแถวและยกเลิก

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.15-2 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## 1. เพิ่มปุ่มแก้ไขและยกเลิก

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มหลัง addButton ภายใน actionButtons:

```xml
<Button fx:id="editButton" text="บันทึกการแก้ไข" disable="true"/>
<Button fx:id="cancelEditButton" text="ยกเลิกแก้ไข" onAction="#handleCancelEdit" disable="true"/>
```

ขั้นนี้ปุ่มบันทึกยังปิดไว้ เราจะลองเลือกและยกเลิกให้เห็นก่อน

## 2. แสดงข้อมูลแถวที่เลือกในฟอร์ม

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Field หลัง addButton:

```java
@FXML private Button editButton;
@FXML private Button cancelEditButton;
```

เพิ่ม Method หลัง `handleAddMachine()` ตามลำดับ:

```java
private void handleMachineSelection(Machine selected) {
        if (selected == null) {
            resetMachineForm();
            return;
        }
        idField.setText(selected.getId());
        nameField.setText(selected.getName());
        locationField.setText(selected.getLocation());
        idField.setEditable(false);
        addButton.setDisable(true);
        editButton.setDisable(true);
        cancelEditButton.setDisable(false);
    }
```

```java
private void resetMachineForm() {
        idField.clear();
        nameField.clear();
        locationField.clear();
        idField.setEditable(true);
        addButton.setDisable(false);
        editButton.setDisable(true);
        cancelEditButton.setDisable(true);
    }
```

```java
@FXML
    private void handleCancelEdit() {
        machineTable.getSelectionModel().clearSelection();
        resetMachineForm();
        statusLabel.setText("ยกเลิกแก้ไขแล้ว");
    }
```

เพิ่มหลัง `configureTable();` ใน `initialize()`:

```java
machineTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, selected) -> handleMachineSelection(selected));
```


**ก่อนรัน:** พิมพ์ชื่อใหม่แต่ยกเลิก ชื่อในตารางจะเปลี่ยนไหม?

รันด้วยคำสั่งด้านบน แล้วเลือก M-002 พิมพ์ชื่ออื่น แล้วกดยกเลิก โดยยังไม่เปิด Auto

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ฟอร์มรับข้อมูลแถวที่เลือก รหัสแก้ไม่ได้ ปุ่มเพิ่มถูกปิด; ยกเลิกแล้วฟอร์มว่างและกลับมาเพิ่มได้ ชื่อในตารางยังเป็นสายพาน

</details>


## ลองทำเอง

ทำไมต้องปิดปุ่มเพิ่มขณะเลือกแก้ไข?

<details>
<summary>เฉลย</summary>

เพื่อแยกเจตนาการแก้เครื่องเดิมจากเพิ่มเครื่องใหม่ และไม่ส่งรหัสเดิมไปเพิ่มซ้ำ

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/15b-select-cancel/)

</details>

[สารบัญ](../ep15-edit-machine-crud.md) · [ถัดไป](03-save.md)
