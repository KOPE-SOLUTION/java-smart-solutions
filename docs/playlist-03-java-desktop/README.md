# Playlist 3: Java Desktop Workshop — Smart Factory Dashboard

นำ Smart Factory Core จาก Playlist OOP มาสร้าง Desktop Application สมัยใหม่ด้วย JavaFX, Maven, CSS และ FXML โดยเพิ่มความสามารถทีละเรื่อง

ไฟล์ที่ทำตามคลิปให้เก็บใน `practice/smart-factory-dashboard` ซึ่ง Git จะไม่นำขึ้น Repository ส่วนตัวอย่างฉบับสมบูรณ์อยู่ใน `src/main/java/smartfactory/ui` และ `src/main/resources/smartfactory/ui`

| EP | เนื้อหา | ผลลัพธ์ |
|---|---|---|
| [3.1](ep01-javafx-maven-stage-scene.md) | JavaFX, Maven, Stage และ Scene | เปิดหน้าต่าง JavaFX แรก |
| [3.2](ep02-layout-pane.md) | Layout Pane | แบ่ง Header, Summary, Content และ Status Bar |
| [3.3](ep03-css-theme.md) | JavaFX CSS | สร้าง Theme Smart Factory |
| [3.4](ep04-controls-form.md) | Controls และ Form | รับข้อมูลเครื่องจักร |
| [3.5](ep05-event-binding.md) | Event, Property และ Binding | ให้ UI ตอบสนองและอัปเดตอัตโนมัติ |
| [3.6](ep06-validation-alert.md) | Validation และ Alert | ป้องกันข้อมูลไม่ครบหรือผิดรูปแบบ |
| [3.7](ep07-tableview-observablelist.md) | TableView และ ObservableList | แสดงข้อมูลเครื่องจักรในตาราง |
| [3.8](ep08-cellfactory-summary.md) | CellFactory และ Summary Card | แยกสีสถานะและสรุปจำนวน |
| [3.9](ep09-service-crud.md) | Service และ CRUD เบื้องต้น | เพิ่ม อ่าน และลบผ่าน OOP Core |
| [3.10](ep10-task-timeline.md) | Task, Thread และ Timeline | จำลอง Sensor โดยหน้าต่างไม่ค้าง |
| [3.11](ep11-fxml-controller.md) | FXML และ Controller | แยก View ออกจาก Logic |
| [3.12](ep12-thai-package-iot.md) | ภาษาไทย, Runtime Image และ IoT | ตรวจภาษาไทยและเตรียมส่งมอบ |
| [3.13](ep13-search-filter.md) | Search และ FilteredList | ค้นหาข้อมูลแบบทันที |
| [3.14](ep14-multi-filter-sort.md) | Multi-filter และ SortedList | กรองหลายเงื่อนไขและเรียงข้อมูล |
| [3.15](ep15-edit-machine-crud.md) | Edit Machine และ Complete CRUD | แก้ไขชื่อและตำแหน่งเครื่องจักร |
| [3.16 Optional](ep16-scene-builder-optional.md) | Scene Builder Workflow | จัด Form แบบ Drag & Drop |
| [3.17 Optional Roadmap](camera-integration-roadmap.md#ep-317--rtsp-live-camera-monitor) | RTSP Live Camera Monitor | แสดงภาพกล้องสดและจัดการ Connection Lifecycle |
| [3.18 Optional Roadmap](camera-integration-roadmap.md#ep-318--camera-capability-probe) | Camera Capability Probe | ตรวจ RTSP, ONVIF และ Dahua Service ก่อนเลือกวิธีเชื่อมต่อ |
| [3.19 Conditional Roadmap](camera-integration-roadmap.md#ep-319--ptz-control) | PTZ Control | ควบคุม Pan, Tilt และ Stop เมื่อกล้องเปิด API ที่รองรับ |
| [3.20 Advanced Roadmap](camera-integration-roadmap.md#ep-320--push-to-talk) | Push to Talk | ส่งเสียงไปยังลำโพงกล้องเมื่อยืนยัน Audio Backchannel หรือ NetSDK แล้ว |

EP 3.17–3.20 เป็น [แผน Optional Camera Integration](camera-integration-roadmap.md) จึงยังไม่มีไฟล์บทเรียนหรือ Source Code ให้ทำตาม โดย EP 3.19–3.20 จะเริ่มสร้างเมื่อผล Capability Probe ยืนยันช่องทางที่กล้องรองรับแล้ว

เริ่มที่ [EP 3.1](ep01-javafx-maven-stage-scene.md) หรือกลับไป [README หลัก](../../README.md)
