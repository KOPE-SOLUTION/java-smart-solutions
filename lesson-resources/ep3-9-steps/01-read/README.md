# EP3.9 — จบตอนที่ 2 — อ่านข้อมูลจาก Service

ซอร์สอ้างอิงสำหรับ [บทเรียนตอนที่ 2](../../../docs/playlist-03-java-desktop/ep09a-service-table.md) ใช้ Java 21 และ JavaFX 21.0.10

เปิด Terminal ที่โฟลเดอร์หลักของ Repository แล้วรัน:

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\01-read\pom.xml javafx:run
```

เมื่อเริ่มจะมี M-001, M-002 และ M-003 รวม 3 เครื่อง สถานะปกติ 2, Sensor ผิดปกติ 1 และหยุดฉุกเฉิน 0

ข้อมูลอยู่ในหน่วยความจำ เปิดโปรแกรมใหม่จะเริ่มจากข้อมูลเดิม การรันครั้งแรกต้องใช้อินเทอร์เน็ต

[ซอร์สทั้งสี่ตอน](../README.md)
