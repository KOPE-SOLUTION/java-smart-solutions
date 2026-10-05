# EP3.14 — ขั้นที่ 3: ล้างเงื่อนไข

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.14-3 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## ล้างตัวกรองด้วยปุ่มเดียว

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มก่อน filterResultLabel:

```xml
<Button text="ล้างตัวกรอง" onAction="#handleClearFilters"/>
```

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่มหลัง `configureMaintenanceFilter()`:

```java
@FXML
    private void handleClearFilters() {
        searchField.clear();
        statusFilter.setValue("ทุกสถานะ");
        maintenanceFilter.setValue("ทุกเครื่อง");
    }
```

Listener ที่มีอยู่จะกรองใหม่เมื่อค่าเปลี่ยน ไม่ต้องโหลดข้อมูลจาก Service ใหม่

**ก่อนรัน:** ล้างตัวกรองแล้วข้อมูลถูกสร้างใหม่หรือแค่กลับมาแสดง?

รันด้วยคำสั่งด้านบน แล้วตั้งคำค้นและตัวกรองหลายค่า แล้วกดล้าง

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ช่องค้นหาว่าง ตัวกรองกลับทุกสถานะ/ทุกเครื่อง ตารางแสดง 3 แถว ข้อมูลเครื่องไม่ได้ถูกรีเซ็ต

</details>


## ลองทำเอง

เพิ่มข้อความแจ้งว่าล้างแล้ว

<details>
<summary>เฉลย</summary>

```java
statusLabel.setText("ล้างตัวกรองแล้ว");
```
เพิ่มท้าย handleClearFilters()

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/14c-clear/)

</details>

[สารบัญ](../ep14-multi-filter-sort.md) · [ถัดไป](04-sort.md)
