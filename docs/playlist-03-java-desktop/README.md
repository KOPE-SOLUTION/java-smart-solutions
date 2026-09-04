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

## Optional Integration หลังจบ Playlist

หลังจบ EP 3.16 สามารถนำ JavaFX ไปเชื่อม RTSP, กล้องเครือข่าย, MQTT หรือบริการภายนอกได้ โดยเนื้อหาเสริมจะเน้นหลักการที่นำกลับมาใช้ซ้ำได้ ได้แก่

- แสดงภาพหรือข้อมูลผ่าน JavaFX Control เช่น `ImageView`
- รับข้อมูลบน Background Thread เพื่อไม่ให้หน้าต่างค้าง
- ส่งผลกลับมาอัปเดต UI บน JavaFX Application Thread
- ออกแบบ Connect, Disconnect และ Resource Cleanup ให้ปลอดภัย
- แยก URL, Username และ Password ออกจาก Source Code และ Log

โปรแกรมตรวจ RTSP สำหรับช่างและงานแจกพัฒนาแยกอยู่ในโปรเจกต์ `kope-rtsp-camera-checker` เพื่อให้บทเรียน JavaFX ไม่ซ้ำกับ Source Code ของผลิตภัณฑ์ ผู้เรียนยังใช้ความรู้จาก Playlist นี้ไปทำ Integration ฉบับย่อได้ แต่ไม่ต้องสร้างโปรแกรมทั้งชุดตามกันทีละหน้าจอ

เริ่มที่ [EP 3.1](ep01-javafx-maven-stage-scene.md) หรือกลับไป [README หลัก](../../README.md)
