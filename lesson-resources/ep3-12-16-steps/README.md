# ชุดพร้อมเรียน EP3.12–3.16

ชุดเริ่มเป็นโค้ดจากขั้นก่อนหน้า ชุดจบเป็นผลหลังทำครบขั้น ใช้ช่วยกลับมาเรียนหรือเทียบคำตอบ ไม่ต้องคัดลอกใหม่ถ้าทำต่อเนื่อง

ปิดแอปและบันทึกไฟล์ก่อนใช้คำสั่งเตรียมจากโฟลเดอร์หลัก Repository สคริปต์เก็บงานเดิมทั้งโปรเจกต์ไว้ใน `practice/_backups` เมื่อใส่ `-BackupExisting`; ถ้าไม่ใส่และมีงานอยู่แล้วจะไม่เขียนทับ

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.14-2 -BackupExisting
```

ตัวอย่างนี้เริ่มก่อนเพิ่มตัวกรองบำรุง เปลี่ยน `-Episode` ตามตาราง:

| จุดเริ่ม | บทเรียน | ชุดก่อนเริ่ม | ชุดจบ |
|---|---|---|---|
| `3.12-1` | [เปิดบท](../../docs/playlist-03-java-desktop/ep12-thai-package-iot.md) | [07-sensor](../ep3-11-steps/07-sensor/) | [12a-tests](12a-tests/) |
| `3.12-2` | [เปิดบท](../../docs/playlist-03-java-desktop/ep12-thai-package-iot.md) | [12a-tests](12a-tests/) | [12b-module](12b-module/) |
| `3.12-3` | [เปิดบท](../../docs/playlist-03-java-desktop/ep12-thai-package-iot.md) | [12b-module](12b-module/) | [12b-module](12b-module/) |
| `3.13-1` | [เปิดบท](../../docs/playlist-03-java-desktop/ep13-search-filter.md) | [12b-module](12b-module/) | [13a-search-id](13a-search-id/) |
| `3.13-2` | [เปิดบท](../../docs/playlist-03-java-desktop/ep13-search-filter.md) | [13a-search-id](13a-search-id/) | [13b-search-all](13b-search-all/) |
| `3.14-1` | [เปิดบท](../../docs/playlist-03-java-desktop/ep14-steps/01-status.md) | [13b-search-all](13b-search-all/) | [14a-status](14a-status/) |
| `3.14-2` | [เปิดบท](../../docs/playlist-03-java-desktop/ep14-steps/02-maintenance.md) | [14a-status](14a-status/) | [14b-maintenance](14b-maintenance/) |
| `3.14-3` | [เปิดบท](../../docs/playlist-03-java-desktop/ep14-steps/03-clear.md) | [14b-maintenance](14b-maintenance/) | [14c-clear](14c-clear/) |
| `3.14-4` | [เปิดบท](../../docs/playlist-03-java-desktop/ep14-steps/04-sort.md) | [14c-clear](14c-clear/) | [14d-sort](14d-sort/) |
| `3.15-1` | [เปิดบท](../../docs/playlist-03-java-desktop/ep15-steps/01-core.md) | [14d-sort](14d-sort/) | [15a-core-edit](15a-core-edit/) |
| `3.15-2` | [เปิดบท](../../docs/playlist-03-java-desktop/ep15-steps/02-select.md) | [15a-core-edit](15a-core-edit/) | [15b-select-cancel](15b-select-cancel/) |
| `3.15-3` | [เปิดบท](../../docs/playlist-03-java-desktop/ep15-steps/03-save.md) | [15b-select-cancel](15b-select-cancel/) | [15c-save](15c-save/) |
| `3.15-4` | [เปิดบท](../../docs/playlist-03-java-desktop/ep15-steps/04-refresh.md) | [15c-save](15c-save/) | [15d-refresh](15d-refresh/) |
| `3.16-1` | [เปิดบท](../../docs/playlist-03-java-desktop/ep16-scene-builder-optional.md) | [15d-refresh](15d-refresh/) | [16a-spacing](16a-spacing/) |
| `3.16-2` | [เปิดบท](../../docs/playlist-03-java-desktop/ep16-scene-builder-optional.md) | [16a-spacing](16a-spacing/) | [16b-layout](16b-layout/) |

`3.12`, `3.13` และ `3.16` ใช้แทนจุดเริ่มขั้นที่ 1 ของ EP นั้นได้ ขั้น Runtime Image ใช้ Source เดียวกับขั้น Module แต่สร้างผลลัพธ์ใน target; ไม่เก็บไฟล์ Build ในชุดพร้อมเรียน

ชุดจบเรียงตามบท: FXML เดิม → Test/Module → ค้นหา → กรอง/เรียง → แก้ข้อมูล → จัด Layout ไม่รวมความสามารถของขั้นถัดไป

[สารบัญ Playlist](../../docs/playlist-03-java-desktop/README.md) · [ผลตรวจชุดบทเรียน](VALIDATION.md)
