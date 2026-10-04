# ชุดพร้อมเรียน EP3.10

เลือกจุดเริ่มของตอนที่ต้องการได้ โดยไม่ต้องไล่ลบโค้ดหรือใช้ประวัติ Undo หากทำต่อจากตอนก่อนอยู่แล้ว ใช้โปรเจกต์เดิมต่อได้เลย

## เริ่มเรียนต่อ

บันทึกไฟล์และปิดโปรแกรม Dashboard ก่อน เปิด Terminal ที่โฟลเดอร์หลักของ Repository แล้วรัน เช่น เริ่ม **ตอนที่ 4**:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.10-4 -BackupExisting
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- เปิดโค้ดที่ `practice/smart-factory-dashboard` แล้วทำตาม [บทเรียนตอนที่ 4](../../docs/playlist-03-java-desktop/ep10d-auto-sensor-timeline.md)
- ถ้ามีงานเดิม คำสั่งจะย้ายทั้งโฟลเดอร์ไปเก็บใน `practice/_backups/` พร้อมวันเวลา ก่อนวางชุดเริ่มใหม่ ไม่ลบงานเดิม
- หากไม่ใส่ `-BackupExisting` แล้วพบงานเดิม คำสั่งจะหยุดโดยไม่แก้ไฟล์
- `LESSON-START.md` ในโปรเจกต์ที่เตรียมไว้บอกว่าเริ่มจากตอนไหน ไม่ใช่การติดตามว่าเรียนถึงขั้นใดแล้ว

**จุดเริ่มตอนที่ 4:** มีเครื่องจักร 3 รายการ ชั่วโมง 121 / 481 / 521 มีปุ่ม **จำลอง Sensor 1 ครั้ง** แต่ยังไม่มีปุ่ม Auto Sensor และไม่มีคำสั่งทดลองหน่วงเวลาหรือบังคับ Error จาก 3B

## เลือกตอน

เปลี่ยนค่าหลัง `-Episode` ตามตาราง ชุดเริ่มคือผลจบตอนก่อนหน้า ส่วนชุดจบมีไว้เปิดเทียบเมื่อทำเสร็จ

| บทเรียน | ค่า `-Episode` | โค้ดก่อนเริ่ม | โค้ดเมื่อจบ |
| --- | --- | --- | --- |
| [ตอนที่ 1 — ปุ่ม Sensor](../../docs/playlist-03-java-desktop/ep10a-sensor-button.md) | `3.10-1` | [จบ EP3.9 ตอนที่ 4](../ep3-9-steps/03-maintenance/) | [01-sensor-button](01-sensor-button/) |
| [ตอนที่ 2 — Task และ Thread](../../docs/playlist-03-java-desktop/ep10b-task-thread.md) | `3.10-2` | [จบตอนที่ 1](01-sensor-button/) | [02-task-thread](02-task-thread/) |
| [ตอนที่ 3A — รับผล Sensor](../../docs/playlist-03-java-desktop/ep10c-sensor-task-result.md) | `3.10-3A` | [จบตอนที่ 2](02-task-thread/) | [03a-sensor-result](03a-sensor-result/) |
| [ตอนที่ 3B — ป้องกันงานซ้อน](../../docs/playlist-03-java-desktop/ep10c2-sensor-task-safety.md) | `3.10-3B` | [จบตอนที่ 3A](03a-sensor-result/) | [03b-sensor-safety](03b-sensor-safety/) |
| [ตอนที่ 4 — Auto Sensor](../../docs/playlist-03-java-desktop/ep10d-auto-sensor-timeline.md) | `3.10-4` | [จบตอนที่ 3B](03b-sensor-safety/) | [04-auto-sensor](04-auto-sensor/) |

ชุดเหล่านี้ต่อจากตัวอย่าง EP3.9 และเพิ่มโค้ดตามบทเรียนทีละตอน ไม่ใช่แอปฉบับสมบูรณ์จาก `src` หลัก จึงไม่มีความสามารถจากตอนถัดไปแทรกเข้ามา และไม่รวม Challenge หรือการตกแต่งที่เพิ่มเอง

<details>
<summary>เปิดเทียบชุดจบ หรือกลับไปดูงานที่สำรองไว้</summary>

แต่ละชุดมี `pom.xml` และ `src` ครบ สามารถรันแยกโดยไม่เปลี่ยนโปรเจกต์ฝึก เช่น ชุดจบตอนที่ 4:

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-10-steps\04-auto-sensor\pom.xml javafx:run
```

งานเดิมอยู่ในโฟลเดอร์สำรองที่คำสั่งแสดงท้ายการทำงาน เปิดไฟล์จากที่นั่นเพื่อเทียบหรือคัดลอกส่วนที่ต้องการกลับมา การเตรียมชุดใหม่ไม่รวมโค้ดที่แก้เองให้อัตโนมัติ

</details>

กลับไป [สารบัญ EP3.10](../../docs/playlist-03-java-desktop/ep10-task-timeline.md)

<details>
<summary>ผลตรวจชุดตัวอย่าง — 4 ตุลาคม 2026</summary>

- คอมไพล์ทั้ง 5 ชุดผ่านด้วย JDK 21 และ JavaFX 21.0.10
- ทดสอบการเปิดหน้าจอ ข้อมูลตั้งต้น ปุ่มจำลอง Task การบำรุง/ลบระหว่างรองาน การข้ามผลของเครื่องที่ถูกลบหรือแทนที่ และ Auto Sensor เริ่ม–หยุด ผ่าน
- ทดสอบรับข้อผิดพลาดแล้วลองใหม่ และปิดหน้าต่างระหว่างมีงานค้าง ผ่าน โดยบังคับ Error/หน่วงเวลาเฉพาะสำเนาทดสอบ ไม่อยู่ในชุดพร้อมเรียน
- ทดสอบคำสั่งเตรียมทั้ง 5 จุดเริ่มในโฟลเดอร์ชั่วคราว: ปฏิเสธการทับงานเดิม เก็บสำรองครบ ปฏิเสธไฟล์ต้นทางที่ขาดหรือโฟลเดอร์ลิงก์ และคืนงานเดิมเมื่อเตรียมไม่สำเร็จ ผ่าน

การตรวจนี้ไม่เปลี่ยนไฟล์ใน `practice` หรือ `src` หลัก และไม่ได้เชื่อม Sensor จริง

</details>
