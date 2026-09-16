# Playlist 3: Java Desktop Workshop — Smart Factory Dashboard

นำ Smart Factory Core จาก Playlist OOP มาสร้าง Desktop Application สมัยใหม่ด้วย JavaFX, Maven, CSS และ FXML โดยเพิ่มความสามารถทีละเรื่อง

ไฟล์ที่ทำตามคลิปให้เก็บใน `practice/smart-factory-dashboard` ซึ่ง Git จะไม่นำขึ้น Repository ส่วนตัวอย่างฉบับสมบูรณ์อยู่ใน `src/main/java/smartfactory/ui` และ `src/main/resources/smartfactory/ui`

## วิธีอ่านตำแหน่งโค้ดในบทเรียน

- `เพิ่ม Import` — วางด้านบนของไฟล์ `.java` ต่อจาก `package` และ Import เดิม
- `เพิ่ม Field` — วางภายใน Class ต่อจาก Field ที่บทเรียนระบุ และอยู่นอก Constructor หรือ Method
- `เพิ่ม Method` — วางหลังปีกกาปิดของ Method ที่บทเรียนระบุ ให้ทั้งสอง Method อยู่ระดับเดียวกัน ไม่ซ้อนกันและไม่อยู่นอก Class
- `แทนที่` — ลบโค้ดเดิมตามชื่อที่ระบุ แล้ววางโค้ดใหม่เพียงชุดเดียว
- โค้ด FXML และ CSS จะระบุชื่อไฟล์กับตำแหน่งที่แก้ไว้ในแต่ละ EP

EP3.10 เรียนตามลำดับ 1 → 2 → 3A → 3B → 4 ส่วน EP3.11 เป็นต้นไปมี[ร่างการแบ่งตอนให้ค่อย ๆ เข้าใจ](../../README.md#แผนแบ่งตอนถัดจาก-ep310) ก่อนเริ่มอัด เอกสารเดิมของ EP3.11 เป็นต้นไปยังใช้ดูขอบเขต ไม่ใช่ฉบับตอนย่อยที่ปรับเสร็จแล้ว

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
| [3.9 ตอนที่ 1](ep09-preparation.md) | ทบทวนและเตรียม OOP Core | สร้าง Model และ Service ก่อนเชื่อมหน้าจอ |
| [3.9 ตอนที่ 2](ep09a-service-table.md) | อ่านข้อมูลจาก Service | เปิดแล้วเห็นเครื่องจักรตัวอย่าง 3 เครื่อง |
| [3.9 ตอนที่ 3](ep09b-service-add-delete.md) | เพิ่มและลบผ่าน Service | ตรวจรหัสซ้ำและอัปเดตตารางกับ Summary |
| [3.9 ตอนที่ 4](ep09c-service-maintenance.md) | ชั่วโมงและการบำรุงรักษา | รีเซ็ตชั่วโมงและสรุปจำนวนที่ต้องบำรุง |
| [3.10 ตอนที่ 1](ep10a-sensor-button.md) | กดปุ่มจำลอง Sensor | แสดงอุณหภูมิ แรงสั่น และ Summary |
| [3.10 ตอนที่ 2](ep10b-task-thread.md) | Task และ Thread | ทดลองงานช้าโดยหน้าจอยังตอบสนอง |
| [3.10 ตอนที่ 3A](ep10c-sensor-task-result.md) | ส่งผล Sensor กลับมา | อัปเดตข้อมูลจาก Background Task |
| [3.10 ตอนที่ 3B](ep10c2-sensor-task-safety.md) | ป้องกันงานซ้อนและรับข้อผิดพลาด | ปุ่มไม่ค้างและข้ามผลของเครื่องที่ถูกลบ |
| [3.10 ตอนที่ 4](ep10d-auto-sensor-timeline.md) | Auto Sensor ด้วย Timeline | เริ่ม–หยุดการอัปเดตโดยไม่สร้างงานซ้อน |
| [3.11](ep11-fxml-controller.md) | FXML และ Controller | แยก View ออกจาก Logic |
| [3.12](ep12-thai-package-iot.md) | ภาษาไทย, Runtime Image และ IoT | ตรวจภาษาไทยและเตรียมส่งมอบ |
| [3.13](ep13-search-filter.md) | Search และ FilteredList | ค้นหาข้อมูลแบบทันที |
| [3.14](ep14-multi-filter-sort.md) | Multi-filter และ SortedList | กรองหลายเงื่อนไขและเรียงข้อมูล |
| [3.15](ep15-edit-machine-crud.md) | Edit Machine และ Complete CRUD | แก้ไขชื่อและตำแหน่งเครื่องจักร |
| [3.16 Optional](ep16-scene-builder-optional.md) | Scene Builder Workflow | จัด Form แบบ Drag & Drop |
| [Case Study: STM32 Motion Dashboard](case-study-stm32-motion-dashboard.md) | USB Serial, Background callback และ Canvas | รับข้อมูล 9 แกนจาก STM32F3 จริง |

## Optional Integration หลังจบ Playlist

หลังจบ EP 3.16 สามารถนำ JavaFX ไปเชื่อม RTSP, กล้องเครือข่าย, MQTT หรือบริการภายนอกได้ โดยเนื้อหาเสริมจะเน้นหลักการที่นำกลับมาใช้ซ้ำได้ ได้แก่

- แสดงภาพหรือข้อมูลผ่าน JavaFX Control เช่น `ImageView`
- รับข้อมูลบน Background Thread เพื่อไม่ให้หน้าต่างค้าง
- ส่งผลกลับมาอัปเดต UI บน JavaFX Application Thread
- ออกแบบ Connect, Disconnect และ Resource Cleanup ให้ปลอดภัย
- แยก URL, Username และ Password ออกจาก Source Code และ Log

ตัวอย่างที่ทำงานครบเส้นทาง Device → USB → Desktop UI อยู่ใน
[STM32F3 Motion Dashboard Case Study](case-study-stm32-motion-dashboard.md) พร้อม Demo Mode สำหรับผู้ที่ยังไม่มีบอร์ด

โปรแกรมตรวจ RTSP สำหรับช่างและงานแจกพัฒนาแยกอยู่ในโปรเจกต์ `kope-rtsp-camera-checker` เพื่อให้บทเรียน JavaFX ไม่ซ้ำกับ Source Code ของผลิตภัณฑ์ ผู้เรียนยังใช้ความรู้จาก Playlist นี้ไปทำ Integration ฉบับย่อได้ แต่ไม่ต้องสร้างโปรแกรมทั้งชุดตามกันทีละหน้าจอ

เริ่มที่ [EP 3.1](ep01-javafx-maven-stage-scene.md) หรือกลับไป [README หลัก](../../README.md)
