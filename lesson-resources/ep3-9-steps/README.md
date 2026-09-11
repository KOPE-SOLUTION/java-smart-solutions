# EP3.9 — ซอร์สหลังจบแต่ละตอน

บทเรียนเริ่มจากงาน EP3.8 และพาสร้างไฟล์เองใน [ตอนที่ 1](../../docs/playlist-03-java-desktop/ep09-preparation.md) ส่วนโฟลเดอร์เหล่านี้เป็นซอร์สอ้างอิงสำหรับเทียบผล แต่ละชุดมี Java, CSS, Model และ Service ครบ ใช้ Java 21 และ JavaFX 21.0.10

| จบตอน | ชุดซอร์ส | ผลเมื่อเปิด |
| --- | --- | --- |
| 1 — เตรียม OOP Core | [00-start](00-start/) | ตารางว่าง มี Model และ Service แล้ว แต่ยังไม่เชื่อมกับหน้าจอ |
| 2 — อ่านข้อมูล | [01-read](01-read/) | แสดง 3 เครื่อง เพิ่มข้อมูลผ่าน Service ได้ |
| 3 — เพิ่มและลบ | [02-add-delete](02-add-delete/) | เพิ่มและลบได้ |
| 4 — บำรุงรักษา | [03-maintenance](03-maintenance/) | แสดงชั่วโมงและบันทึกการบำรุงรักษาได้ |

## เปิดผลลัพธ์อ้างอิง

เปิด Terminal ที่โฟลเดอร์หลักของ Repository แล้วรันทีละชุด:

### จบตอนที่ 1

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\00-start\pom.xml javafx:run
```

### จบตอนที่ 2

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\01-read\pom.xml javafx:run
```

### จบตอนที่ 3

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\02-add-delete\pom.xml javafx:run
```

### จบตอนที่ 4

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\03-maintenance\pom.xml javafx:run
```

ครั้งแรกต้องใช้อินเทอร์เน็ตดาวน์โหลด Dependency ข้อมูลไม่บันทึกถาวร เปิดใหม่จะได้ข้อมูลเริ่มต้นของแต่ละชุด

<details>
<summary>ตรวจชุดซอร์สและลิงก์บทเรียน</summary>

```powershell
powershell -ExecutionPolicy Bypass -File .\lesson-resources\ep3-9-steps\verify.ps1
```

</details>

[สารบัญ EP3.9](../../docs/playlist-03-java-desktop/ep09-service-crud.md)
