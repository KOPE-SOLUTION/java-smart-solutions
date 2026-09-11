# OOP Core อ้างอิงสำหรับ EP3.9

[ตอนที่ 1 — ทบทวนและเตรียม OOP Core](../../docs/playlist-03-java-desktop/ep09-preparation.md) พาสร้างไฟล์ทีละตัว โฟลเดอร์นี้เก็บโค้ดเดียวกันสำหรับเทียบคำตอบ

| โฟลเดอร์ | ไฟล์ |
| --- | --- |
| `model` | `FactoryDevice.java`, `Machine.java`, `MachineStatus.java`, `Maintainable.java`, `SensorReading.java` |
| `service` | `SmartFactoryService.java` |

[ZIP อ้างอิง](ep3-9-oop-core.zip?raw=true) · [ซอร์สหลังจบทั้งสี่ตอน](../ep3-9-steps/)

## ข้อมูลตัวอย่าง

| รหัส | สถานะ | ชั่วโมง | ต้องบำรุง |
| --- | --- | --- | --- |
| M-001 | กำลังทำงาน | 121 | ไม่ |
| M-002 | Sensor ผิดปกติ | 481 | ใช่ |
| M-003 | กำลังทำงาน | 521 | ใช่ |

จำนวนเริ่มต้น: ทั้งหมด 3, สถานะปกติ 2, Sensor ผิดปกติ 1, หยุดฉุกเฉิน 0, ต้องบำรุงทั้งหมด 2

## ไฟล์ทดสอบ

`tests/SmartFactoryTest.java` ใช้ใน [EP3.12](../../docs/playlist-03-java-desktop/ep12-thai-package-iot.md) และต่อยอดใน EP3.15 โดยเตรียมแยกจาก ZIP สำหรับ EP3.9

<details>
<summary>ที่มาของซอร์ส</summary>

ไฟล์ Java ทั้งเจ็ดไฟล์คัดจาก Commit `3da3c5d282d719e1794d3f5cba8b872a74436b73` ของ Repository นี้ โดยรักษาโค้ดและชื่อ Package เดิมไว้

ZIP บรรจุเฉพาะโฟลเดอร์ `model` และ `service` รวม 6 ไฟล์ สำหรับใช้ตามลำดับบทเรียน ชุดนี้ยังไม่มี `updateDetails(...)` และ `updateMachineDetails(...)` ที่จะเพิ่มใน EP3.15

</details>
