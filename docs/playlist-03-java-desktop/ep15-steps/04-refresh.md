# EP3.15 — ขั้นที่ 4: รักษาข้อความระหว่าง Auto

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.15-4 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## รักษาข้อความที่ยังไม่บันทึก

Auto ทำให้ refresh ตารางซ้ำ ขณะที่ผู้ใช้กำลังพิมพ์ เราจะเก็บข้อความที่ยังไม่บันทึกไว้เฉพาะแถวเดิมที่ยังแสดงอยู่

ใน `src/main/java/smartfactory/ui/DashboardController.java` ต้น `refreshDashboard()` ก่อน `machines.setAll(...)` เพิ่ม:

```java
Machine selected = machineTable.getSelectionModel().getSelectedItem();
        String draftName = nameField.getText();
        String draftLocation = locationField.getText();
```

หลัง `machineTable.refresh();` เพิ่ม:

```java
if (selected != null && machineTable.getItems().contains(selected)) {
            machineTable.getSelectionModel().select(selected);
            nameField.setText(draftName);
            locationField.setText(draftLocation);
        }
```

หากแถวถูกลบหรือไม่ผ่านตัวกรองแล้ว จะไม่เลือกแถวนั้นกลับ ไม่ดึงแถวที่ซ่อนอยู่มาบันทึกต่อ

**ก่อนรัน:** Auto เปลี่ยน Sensor ได้ แต่ควรเปลี่ยนข้อความที่กำลังพิมพ์หรือไม่?

รันด้วยคำสั่งด้านบน แล้วล้างตัวกรอง เลือก M-002 พิมพ์ชื่อใหม่โดยยังไม่บันทึก แล้วเปิด Auto รอ 2–3 รอบ

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

Sensor เปลี่ยน แต่ข้อความที่พิมพ์ยังอยู่ กดหยุด Auto รอรอบที่เริ่มไปแล้วจบก่อนลองบันทึก

</details>

ลองเปิดตัวกรองที่ทำให้แถวที่เลือกหาย แล้วตรวจว่าฟอร์มออกจากโหมดแก้ไข จากนั้นล้างตัวกรองและเลือกเครื่องใหม่ได้


## ลองทำเอง

ถ้าลบแถวที่กำลังแก้ไข ควรเก็บร่างข้อความไว้เพื่อแก้แถวอื่นหรือไม่?

<details>
<summary>เฉลย</summary>

ไม่ควร เพราะร่างเป็นของเครื่องที่ถูกลบ ต้องกลับโหมดเพิ่มหรือให้เลือกแถวใหม่

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/15d-refresh/)

</details>

[สารบัญ](../ep15-edit-machine-crud.md) · [ถัดไป](../ep16-scene-builder-optional.md)
