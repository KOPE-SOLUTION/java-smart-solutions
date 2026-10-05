# EP3.11 — ขั้นที่ 6: ลบและบำรุงรักษา

เป้าหมาย: บำรุง M-003 แล้วชั่วโมงเป็น 0

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-6 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository หลังเพิ่มโค้ดครบขั้น:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

## 1. เพิ่มปุ่มที่ใช้กับแถวที่เลือก

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มหลังปุ่มเพิ่มเครื่องจักร ภายใน VBox `actionButtons`:

```xml
<Button text="ลบรายการที่เลือก" onAction="#handleDeleteMachine"/>
<Button text="บำรุงเสร็จแล้ว" onAction="#handleMaintenance"/>
```

## 2. นำ Method เดิมมาใช้

ใน `src/main/java/smartfactory/ui/DashboardController.java` คัดลอก `handleDeleteMachine()` และ `handleMaintenance()` จาก DashboardApp เดิมมาไว้หลัง `handleAddMachine()` เพิ่ม `@FXML` เหนือทั้งสอง Method

ไม่คัดลอก `new Button(...)` หรือ `setOnAction(...)` เพราะสองส่วนนี้อยู่ใน FXML แล้ว

**ก่อนรัน:** บำรุง M-003 แล้วจำนวนเครื่องจักรจะลดไหม?

รันด้วยคำสั่งด้านบน แล้วเริ่มแอปใหม่ เลือก M-003 กดบำรุง จากนั้นเลือก M-001 แล้วกดลบ

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

บำรุง M-003: ชั่วโมง 0 สถานะปิดเครื่อง ทั้งหมดยัง 3 ต้องบำรุงเหลือ 1; ลบ M-001: ทั้งหมดเหลือ 2

</details>


## ลองทำเอง

กดบำรุงโดยยังไม่เลือกแถว ควรเกิดอะไร?

<details>
<summary>เฉลย</summary>

แสดง Alert ให้เลือกเครื่องจักร ไม่เปลี่ยนข้อมูล เพราะ Method เดิมตรวจ `selected == null` ไว้แล้ว

</details>



<details>
<summary>ดูโค้ดครบขั้นเพื่อเทียบ</summary>

[ชุดจบขั้นที่ 6](../../../lesson-resources/ep3-11-steps/06-actions/) เป็นผลจากขั้นนี้ ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](05-add.md) · [สารบัญ EP3.11](../ep11-fxml-controller.md) · [ถัดไป](07-sensor.md)
