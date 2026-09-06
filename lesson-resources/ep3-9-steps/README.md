# EP3.9 — ชุดซอร์สสำหรับแต่ละตอน

แต่ละโฟลเดอร์เป็น Maven Project แยกกัน มี `pom.xml`, `DashboardApp.java`, CSS, Model และ Service ครบ ไม่อ้าง Source จากโปรเจกต์ข้างเคียง

| ชุด | ใช้เมื่อใด | ผลเมื่อเปิด |
| --- | --- | --- |
| [00-start](00-start/) | ก่อนเริ่มตอนที่ 1 | ตารางว่างแบบ EP3.8 มี Core เตรียมไว้แล้วแต่ยังไม่เรียกใช้ |
| [01-read](01-read/) | จบตอนที่ 1 / ก่อนเริ่มตอนที่ 2 | แสดงข้อมูล 3 เครื่องและ Summary เพิ่มผ่าน Service ได้ |
| [02-add-delete](02-add-delete/) | จบตอนที่ 2 / ก่อนเริ่มตอนที่ 3 | เพิ่มและลบเครื่องจักรได้ |
| [03-maintenance](03-maintenance/) | จบตอนที่ 3 / ก่อนเริ่ม EP3.10 | เพิ่ม ลบ แสดงชั่วโมง และบันทึกการบำรุงรักษาได้ |

## เปิดดูผลทันที

ใช้ Java 21 โดยเปิด Terminal ที่โฟลเดอร์หลักของ Repository แล้วเลือก **รันทีละชุด**:

### ตอนที่ 1

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\01-read\pom.xml javafx:run
```

### ตอนที่ 2

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\02-add-delete\pom.xml javafx:run
```

### ตอนที่ 3

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\03-maintenance\pom.xml javafx:run
```

การรันครั้งแรกต้องใช้อินเทอร์เน็ตเพื่อดาวน์โหลด Maven และ Dependency ปิดหน้าต่างชุดเดิมก่อนรันชุดถัดไป ทุกชุดเก็บข้อมูลในหน่วยความจำ เริ่มใหม่จะได้ข้อมูลเริ่มต้น ไม่ได้แชร์ข้อมูลกัน

## เลือกเริ่มเฉพาะตอน

คัดลอกเนื้อหาของชุดก่อนเริ่มไปยัง `practice/smart-factory-dashboard` ให้มี `pom.xml` อยู่ใต้โฟลเดอร์นี้ทันที หากมีงานเดิม ให้เปลี่ยนชื่อโฟลเดอร์เดิมเก็บไว้ก่อน ไม่รวมไฟล์จากหลายชุดเข้าด้วยกัน จากนั้นทำตาม [บทเรียน EP3.9](../../docs/playlist-03-java-desktop/ep09-service-crud.md)

ชุดก่อนเริ่มยึดเนื้อหาหลัก EP3.8: Form มีรหัส ชื่อ และตำแหน่ง ไม่รวมช่องอุณหภูมิหรือ Event คลิกแถวจาก Challenge หากเรียนต่อด้วยไฟล์ที่ทำเอง ให้ดูหมายเหตุในตอนที่ 1

ไม่มี Search, Edit, FXML หรือ Background Task จากตอนถัดไป และชุดจบตอนที่ 3 ยังไม่รวมเฉลย Challenge

## ตรวจการประกอบชุดซอร์ส

Model และ Service ของทั้งสี่ชุดต้องตรงกับ [OOP Core สำหรับ EP3.9](../ep3-9-oop-core/) ใช้สคริปต์ตรวจความตรงกันของ Core, โครงสร้าง Maven และลิงก์บทเรียนได้ด้วย:

```powershell
powershell -ExecutionPolicy Bypass -File .\lesson-resources\ep3-9-steps\verify.ps1
```

[กลับสารบัญ EP3.9](../../docs/playlist-03-java-desktop/ep09-service-crud.md)
