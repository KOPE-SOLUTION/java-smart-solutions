# ชุดพร้อมเรียน EP3.11 — 5 ตอน

ชุดเริ่มมาจากตอนก่อนหน้า ชุดจบใช้เทียบหลังทำครบตอน ไม่ต้องเตรียมใหม่ถ้าทำต่อเนื่อง

ปิดแอปและบันทึกไฟล์ก่อนรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-1 -BackupExisting
```

เปลี่ยน `3.11-1` เป็นตอนที่ต้องการ หมายเลข `3.11-1` ถึง `3.11-5` อ้างถึง **ตอนที่** ไม่ใช่เลขขั้นแบบเดิม

สคริปต์สำรองโปรเจกต์เดิมทั้งชุดใน `practice/_backups` เมื่อใส่ `-BackupExisting` หากไม่ใส่และมีงานอยู่แล้วจะไม่เขียนทับ

| ตอน / คำสั่ง | ชุดก่อนเริ่ม | ชุดจบ |
|---|---|---|
| [ตอนที่ 1](../../docs/playlist-03-java-desktop/ep11a-fxml-window.md) — `3.11-1` | [จบ EP3.10 ตอนที่ 4](../ep3-10-steps/04-auto-sensor/) | [01-window](01-window/) |
| [ตอนที่ 2](../../docs/playlist-03-java-desktop/ep11b-fxml-controller.md) — `3.11-2` | [จบตอนที่ 1](01-window/) | [02-button](02-button/) |
| [ตอนที่ 3](../../docs/playlist-03-java-desktop/ep11c-service-table-summary.md) — `3.11-3` | [จบตอนที่ 2](02-button/) | [04-summary](04-summary/) |
| [ตอนที่ 4](../../docs/playlist-03-java-desktop/ep11d-form-crud.md) — `3.11-4` | [จบตอนที่ 3](04-summary/) | [06-actions](06-actions/) |
| [ตอนที่ 5](../../docs/playlist-03-java-desktop/ep11e-sensor-lifecycle.md) — `3.11-5` | [จบตอนที่ 4](06-actions/) | [07-sensor](07-sensor/) |

<details>
<summary>กลับมาเรียนต่อกลางตอนที่ 3 หรือ 4</summary>

ถ้าทำขั้นแรกเสร็จอยู่แล้ว ใช้งานเดิมต่อได้เลย หรือใช้ค่าหลัง `-Episode` ต่อไปนี้เพื่อเตรียมจุดเริ่มกลางตอน:

| จุดที่ต้องการเริ่ม | ค่า -Episode | ชุดเริ่ม | ชุดจบ |
|---|---|---|---|
| [ตอนที่ 3 ขั้นที่ 2: Summary](../../docs/playlist-03-java-desktop/ep11c-service-table-summary.md#step-2) | `3.11-3-summary` | [03-table](03-table/) | [04-summary](04-summary/) |
| [ตอนที่ 4 ขั้นที่ 2: ลบ/บำรุง](../../docs/playlist-03-java-desktop/ep11d-form-crud.md#step-2) | `3.11-4-actions` | [05-add](05-add/) | [06-actions](06-actions/) |

ตัวอย่าง:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-3-summary -BackupExisting
```

คำสั่งเดิม `3.11-6` และ `3.11-7` เลิกใช้แล้ว ให้เลือกจากตารางปัจจุบัน

</details>


โฟลเดอร์อ้างอิงยังมี 7 ชุดเพื่อรักษาจุดรันทั้ง 7 จุด โดยตอนที่ 3 และ 4 มีตอนละ 2 จุด ไม่รวม Search หรือ Edit ก่อนเวลา

[สารบัญ EP3.11](../../docs/playlist-03-java-desktop/ep11-fxml-controller.md) · [ชุด EP3.12–3.16](../ep3-12-16-steps/README.md)
