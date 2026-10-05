# EP3.14 — ขั้นที่ 4: เรียงหัวตาราง

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.14-4 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## เรียงมุมมองของตาราง

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Import:

```java
import javafx.collections.transformation.SortedList;
```

เพิ่ม Field หลัง filteredMachines:

```java
private final SortedList<Machine> sortedMachines = new SortedList<>(filteredMachines);
```

ท้าย `configureTable()` แทนที่ `machineTable.setItems(filteredMachines);`:

```java
sortedMachines.comparatorProperty().bind(machineTable.comparatorProperty());
machineTable.setItems(sortedMachines);
```

SortedList รับรายการที่กรองแล้ว ส่วน Binding รับวิธีเรียงจากหัวตาราง โดยไม่เรียง List ใน Service ใหม่

**ก่อนรัน:** คลิกชั่วโมงสองครั้งจะเปลี่ยนลำดับอย่างไร?

รันด้วยคำสั่งด้านบน แล้วล้างตัวกรอง คลิกหัวชั่วโมง แล้วลองเลือกต้องบำรุงขณะเรียงอยู่

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ครั้งแรก 121 → 481 → 521; ครั้งที่สองเรียงกลับ เมื่อเลือกต้องบำรุงเหลือ M-003 และ M-002 ตามลำดับชั่วโมงมากไปน้อย

</details>


## ลองทำเอง

ทำไมคอลัมน์ชั่วโมงจึงเรียง 9, 80, 500 ได้ถูกต้อง?

<details>
<summary>เฉลย</summary>

คอลัมน์ใช้ Integer ไม่ใช่ข้อความ จึงเปรียบเทียบค่าตัวเลข

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/14d-sort/)

</details>

[สารบัญ](../ep14-multi-filter-sort.md) · [ถัดไป](../ep15-edit-machine-crud.md)
