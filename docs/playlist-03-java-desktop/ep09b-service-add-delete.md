# EP 3.9 ตอนที่ 3 — เพิ่มและลบเครื่องจักรผ่าน Service

เป้าหมาย: เพิ่มข้อมูล ตรวจรหัสซ้ำ และลบแถวที่เลือก

ใช้โปรเจกต์ที่จบ [ตอนที่ 2](ep09a-service-table.md) ต่อ แก้ไฟล์ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

## 1. ทบทวนจุดเพิ่มข้อมูล

ใน `handleAddMachine()` มีสองบรรทัดนี้แล้ว:

```java
service.addMachine(new Machine(id, name, location));
refreshDashboard();
```

Service ตรวจรหัสซ้ำก่อนเพิ่ม ถ้าซ้ำจะส่ง `IllegalArgumentException` ไปยัง `catch` เดิมเพื่อแสดง Alert

## 2. เพิ่ม Method ลบ

วางหลังปีกกาปิดของ `handleAddMachine()` ก่อน `showError(...)`:

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

`getSelectedItem()` คือเครื่องจักรที่เลือก ถ้ายังไม่เลือกจะได้ `null` จึงแจ้งเตือนแล้วจบด้วย `return`

## 3. เพิ่มปุ่มลบ

ใน `buildMachineForm()` แทนที่เฉพาะ `form.add(addButton, 1, 3);`:

```java
Button deleteButton = new Button("ลบรายการที่เลือก");
deleteButton.setOnAction(event -> handleDeleteMachine());
VBox actionButtons = new VBox(8, addButton, deleteButton);
form.add(actionButtons, 1, 3);
```

## 4. รันและตรวจผล

บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เริ่มใหม่แล้วทดลองตามลำดับ:

| ทดลอง | ผลที่ต้องเห็น |
| --- | --- |
| กดลบโดยไม่เลือกแถว | Alert ให้เลือกเครื่องจักร |
| เพิ่ม M-004 / เครื่องบรรจุ / Line B | ทั้งหมด 4 เครื่องใหม่เป็นปิดเครื่องสีเทา สถานะปกติยังเป็น 2 |
| เพิ่ม M-004 ซ้ำ โดยกรอกครบ | Alert รหัสซ้ำ จำนวนยังเป็น 4 |
| เลือก M-004 แล้วกดลบ | เหลือ 3 แถว |
| เลือก M-002 แล้วกดลบ | ทั้งหมด 2 · สถานะปกติ 2 · Sensor ผิดปกติ 0 |

ข้อมูลยังไม่บันทึกถาวร เปิดโปรแกรมใหม่จะกลับมามี 3 เครื่องเดิม

[ซอร์สหลังจบตอนนี้](../../lesson-resources/ep3-9-steps/02-add-delete/) · [ต่อ ตอนที่ 4 — ชั่วโมงและการบำรุงรักษา](ep09c-service-maintenance.md)
