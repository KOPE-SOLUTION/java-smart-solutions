# EP 3.9 ตอนที่ 2 — เพิ่มและลบเครื่องจักรผ่าน Service

## สิ่งที่จะทำ

ทดสอบการเพิ่มเครื่องจักรและรหัสซ้ำ แล้วเพิ่มปุ่มลบแถวที่เลือก โดยให้ตารางและ Summary อัปเดตทันที

## ก่อนเริ่ม

ใช้ผลจากตอนก่อนหน้า หรือเลือก [ชุดก่อนเริ่มตอนนี้](../../lesson-resources/ep3-9-steps/01-read/) ซึ่งมี `pom.xml`, Java และ CSS ครบแล้ว

หากใช้ชุดไฟล์ ให้คัดลอก **เนื้อหาภายใน** `01-read` ไปไว้ใน `practice/smart-factory-dashboard` ให้ `pom.xml` อยู่ใต้โฟลเดอร์นี้ทันที หากมีงานเดิมให้เปลี่ยนชื่อโฟลเดอร์เดิมเก็บไว้ก่อน ไม่วางทับหรือรวมสองเวอร์ชันเข้าด้วยกัน

รันจากโฟลเดอร์หลักของ Repository ที่มี `mvnw.cmd`:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ต้องเห็นข้อมูลเริ่มต้น 3 เครื่อง: สถานะปกติ 2, Sensor ผิดปกติ 1 และหยุดฉุกเฉิน 0 ปิดหน้าต่างก่อนเริ่มแก้โค้ด

แก้ Java ที่ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java` ทุก Method ที่เพิ่มให้อยู่ภายใน `DashboardApp` ก่อนปีกกาปิด Class ไม่วางซ้อนใน `start()` หรือ Method อื่น

## 1. ทดลองเส้นทางเพิ่มข้อมูลที่เชื่อมไว้แล้ว

ใน `handleAddMachine()` จะมีสองบรรทัดนี้จากตอนที่ 1 อยู่ภายใน `try` หลังตรวจข้อความครบ:

```java
service.addMachine(new Machine(id, name, location));
refreshDashboard();
```

`new Machine(...)` สร้างเครื่องจักรหนึ่งเครื่อง แล้ว `service.addMachine(...)` ตรวจรหัสซ้ำและเก็บรายการ ส่วน `refreshDashboard()` นำข้อมูลล่าสุดกลับมาแสดง สองบรรทัดนี้มีอยู่แล้ว ไม่ต้องเพิ่มซ้ำ

รันด้วยคำสั่งเดิม แล้วทดลองก่อนเพิ่มปุ่มลบ:

1. เพิ่ม `M-004`, `เครื่องบรรจุ`, `Line B` ต้องเห็น 4 แถว และเครื่องใหม่เป็น `ปิดเครื่อง` สีเทา
2. เพิ่มรหัส `M-004` ซ้ำโดยกรอกชื่อและตำแหน่งให้ครบ ต้องเห็น Alert ว่ารหัสซ้ำและยังมี 4 แถว
3. ลองเว้นชื่อว่าง ต้องเห็น Alert จาก Validation เดิม และจำนวนไม่เพิ่ม

Service ส่งปัญหารหัสซ้ำด้วย `IllegalArgumentException` จึงเข้าที่ `catch` ของ `handleAddMachine()` และใช้ `showError(...)` ที่ทำไว้ใน EP3.6

หลังเพิ่มเครื่องที่ยังปิดอยู่ Summary จะเป็นทั้งหมด 4 แต่สถานะปกติยังเป็น 2 เพราะนับเฉพาะ `RUNNING` ปิดหน้าต่างก่อนแก้ขั้นต่อไป

## 2. เพิ่ม Method สำหรับลบ

ใน `DashboardApp.java` เพิ่ม Method นี้หลังปีกกาปิดของ `handleAddMachine()` และก่อน `showError(...)`:

```java
private void handleDeleteMachine() {
        Machine selected = machineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("กรุณาเลือกเครื่องจักรในตารางก่อน");
            return;
        }
        service.removeMachine(selected.getId());
        refreshDashboard();
        statusLabel.setText("ลบ " + selected.getId() + " แล้ว");
    }
```

`getSelectedItem()` คืนเครื่องจักรของแถวที่เลือก ถ้าได้ `null` ให้แจ้งผู้ใช้แล้ว `return` เพื่อจบการทำงาน ไม่ส่งค่าไปลบต่อ

## 3. เพิ่มปุ่มลบใน Form

ใน `buildMachineForm()` แทนที่เฉพาะ `form.add(addButton, 1, 3);` ด้วยชุดนี้:

```java
Button deleteButton = new Button("ลบรายการที่เลือก");
deleteButton.setOnAction(event -> handleDeleteMachine());
VBox actionButtons = new VBox(8, addButton, deleteButton);
form.add(actionButtons, 1, 3);
```

ปุ่มทั้งสองอยู่ในแนวตั้งใต้ช่องตำแหน่ง เก็บ `addButton.setOnAction(event -> handleAddMachine());` ที่มีอยู่แล้วไว้ ส่วน `VBox` มี Import มาจากการสร้าง Header

หากมีช่องอุณหภูมิและปุ่มเดิมอยู่แถว 4 ให้แทนที่ `form.add(addButton, 1, 4);` และใช้ `form.add(actionButtons, 1, 4);` เพื่อวางใต้ช่องกรอกสุดท้าย

## 4. รันและตรวจผล

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เริ่มใหม่จะมี 3 เครื่องทุกครั้ง ทดลองตามลำดับ:

| สิ่งที่ทดลอง | ผลที่ต้องเห็น |
| --- | --- |
| กดลบโดยยังไม่เลือกแถว | Alert ให้เลือกเครื่องจักร จำนวนยังเป็น 3 |
| เพิ่ม M-004 ให้ครบทุกช่อง | ทั้งหมดเป็น 4 และ M-004 เป็นปิดเครื่อง |
| เพิ่ม M-004 ซ้ำ | Alert รหัสซ้ำ จำนวนยังเป็น 4 |
| เลือก M-004 แล้วกดลบ | แถวนั้นหาย ทั้งหมดกลับเป็น 3 และแถบล่างแจ้งรหัสที่ลบ |
| เลือก M-002 แล้วกดลบ | ทั้งหมดเป็น 2, สถานะปกติ 2, Sensor ผิดปกติ 0 |

ข้อมูลยังไม่บันทึกลงไฟล์หรือฐานข้อมูล ปิดและเปิดใหม่จะกลับมามี M-001, M-002 และ M-003

## เปิดผลลัพธ์ของตอนนี้ได้ทันที

[ซอร์สหลังจบตอนนี้](../../lesson-resources/ep3-9-steps/02-add-delete/) เป็นโปรเจกต์ครบชุด หากต้องการดูผลก่อนทำตาม รันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\02-add-delete\pom.xml javafx:run
```

ชุดนี้เพิ่มและลบได้แล้ว แต่ยังไม่มีคอลัมน์ชั่วโมงและปุ่มบำรุงรักษา

ถัดไป: [EP3.9 ตอนที่ 3 — ชั่วโมงและการบำรุงรักษา](ep09c-service-maintenance.md) · [สารบัญ EP3.9](ep09-service-crud.md)
