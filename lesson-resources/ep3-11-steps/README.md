# ชุดพร้อมเรียน EP3.11

ชุดเริ่มเป็นโค้ดจากขั้นก่อนหน้า ชุดจบเป็นผลหลังทำครบขั้น ใช้ช่วยกลับมาเรียนหรือเทียบคำตอบ ไม่ต้องคัดลอกใหม่ถ้าทำต่อเนื่อง

ปิดแอปและบันทึกไฟล์ก่อนใช้คำสั่งเตรียมจากโฟลเดอร์หลัก Repository สคริปต์เก็บงานเดิมทั้งโปรเจกต์ไว้ใน `practice/_backups` เมื่อใส่ `-BackupExisting`; ถ้าไม่ใส่และมีงานอยู่แล้วจะไม่เขียนทับ

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-1 -BackupExisting
```

เปลี่ยนค่าหลัง `-Episode` ตามตาราง เลขหลังขีดคือ **ขั้นในบท** ไม่ใช่ EP หรือวิดีโอใหม่

| จุดเริ่ม | บทเรียน | ที่มาของชุดเริ่ม | ชุดจบ |
|---|---|---|---|
| `3.11-1` | [ขั้นที่ 1](../../docs/playlist-03-java-desktop/ep11-steps/01-window.md) | [จบ EP3.10 ตอนที่ 4](../ep3-10-steps/04-auto-sensor/) | [01-window](01-window/) |
| `3.11-2` | [ขั้นที่ 2](../../docs/playlist-03-java-desktop/ep11-steps/02-button.md) | [จบขั้นที่ 1](01-window/) | [02-button](02-button/) |
| `3.11-3` | [ขั้นที่ 3](../../docs/playlist-03-java-desktop/ep11-steps/03-table.md) | [จบขั้นที่ 2](02-button/) | [03-table](03-table/) |
| `3.11-4` | [ขั้นที่ 4](../../docs/playlist-03-java-desktop/ep11-steps/04-summary.md) | [จบขั้นที่ 3](03-table/) | [04-summary](04-summary/) |
| `3.11-5` | [ขั้นที่ 5](../../docs/playlist-03-java-desktop/ep11-steps/05-add.md) | [จบขั้นที่ 4](04-summary/) | [05-add](05-add/) |
| `3.11-6` | [ขั้นที่ 6](../../docs/playlist-03-java-desktop/ep11-steps/06-actions.md) | [จบขั้นที่ 5](05-add/) | [06-actions](06-actions/) |
| `3.11-7` | [ขั้นที่ 7](../../docs/playlist-03-java-desktop/ep11-steps/07-sensor.md) | [จบขั้นที่ 6](06-actions/) | [07-sensor](07-sensor/) |

Model, Service, DashboardApp และ Task มาจากชุดจบ EP3.10 เดิม ขั้นที่ 1–2 สร้าง FXML เล็กเพื่อทำความเข้าใจ ขั้นที่ 3–7 นำพฤติกรรม Dashboard เดิมกลับมา ไม่ใช้ Checkpoint จาก Production และไม่รวม Search หรือ Edit ก่อนเวลา

[สารบัญ EP3.11](../../docs/playlist-03-java-desktop/ep11-fxml-controller.md) · [ชุด EP3.12–3.16](../ep3-12-16-steps/README.md)
