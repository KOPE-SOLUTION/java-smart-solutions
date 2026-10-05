# EP3.15 — ขั้นที่ 3: บันทึกชื่อและตำแหน่ง

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.15-3 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## เชื่อมปุ่มบันทึก

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Method หลัง `handleCancelEdit()`:

```java
@FXML
    private void handleUpdateMachine() {
        Machine selected = machineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("กรุณาเลือกเครื่องจักรในตารางก่อน");
            return;
        }
        try {
            String name = requireText(nameField, "กรุณากรอกชื่อเครื่องจักร");
            String location = requireText(locationField, "กรุณากรอกตำแหน่งเครื่องจักร");
            service.updateMachineDetails(selected.getId(), name, location);
            machineTable.getSelectionModel().clearSelection();
            refreshDashboard();
            resetMachineForm();
            statusLabel.setText("แก้ไข " + selected.getId() + " แล้ว");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }
```

เพิ่มเครื่องและแก้เครื่องใช้ try-catch แบบเดียวกัน แต่ครั้งนี้เรียก updateMachineDetails()

ใน `handleMachineSelection()` เปลี่ยนเฉพาะ `editButton.setDisable(true);` เป็น:

```java
editButton.setDisable(false);
```

ไม่เปลี่ยนบรรทัดใน `resetMachineForm()` เพราะโหมดเพิ่มยังไม่ควรบันทึกแก้ไขได้

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่ม `onAction` ให้ editButton:

```xml
<Button fx:id="editButton" text="บันทึกการแก้ไข" onAction="#handleUpdateMachine" disable="true"/>
```


**ก่อนรัน:** แก้ชื่อแล้วชั่วโมงและ Sensor ควรเปลี่ยนไหม?

รันด้วยคำสั่งด้านบน แล้วเลือก M-002 เปลี่ยนเป็นสายพานลำเลียง / Line B แล้วบันทึก โดยยังไม่เปิด Auto

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ชื่อและตำแหน่งเปลี่ยน รหัสยัง M-002 ชั่วโมงยัง 481 และ Sensor/สถานะเดิม จากนั้นฟอร์มกลับโหมดเพิ่ม

</details>

ค้นหาคำว่า `ลำเลียง` ต้องพบ M-002 ลองบันทึกโดยเว้นชื่อว่าง ต้องมี Alert และข้อมูลเดิมไม่เปลี่ยน


## ลองทำเอง

แสดงชื่อเดิม → ชื่อใหม่หลังบันทึก

<details>
<summary>เฉลย</summary>

เก็บ `String oldName = selected.getName();` ก่อนเรียก Service แล้วใช้ oldName กับ name ในข้อความ statusLabel หลังบันทึกสำเร็จ

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/15c-save/)

</details>

[สารบัญ](../ep15-edit-machine-crud.md) · [ถัดไป](04-refresh.md)
