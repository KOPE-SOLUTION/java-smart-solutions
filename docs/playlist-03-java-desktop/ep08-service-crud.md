# EP 3.8 — เชื่อม SmartFactoryService และ CRUD

## เป้าหมาย

- ใช้ `SmartFactoryService` จาก Playlist 2 เป็นแหล่งข้อมูลจริง
- เพิ่ม อ่าน อัปเดต และลบ Machine ผ่าน Service
- Refresh Table และ Summary จาก State ล่าสุด
- แสดง Error จาก Model และ Service ด้วย Dialog

EP นี้แก้สองไฟล์:

1. `SmartFactoryService.java` — เพิ่ม Method ที่ UI ยังต้องใช้
2. `FirstWindow.java` — เปลี่ยนข้อมูลทดลองเป็นข้อมูลจาก Service

ทำทีละ Use Case และ Run ตรวจทุกส่วนก่อนเพิ่มส่วนถัดไป

## 1. เพิ่ม Method ที่ UI ต้องใช้ใน Service

เปิด `SmartFactoryService.java` จาก Playlist 2 แล้ววางสอง Method นี้ภายใน Class

Method อ่าน Machine ทั้งหมด:

```java
public List<Machine> getMachines() {
    return List.copyOf(machines);
}
```

Method ลบ Machine:

```java
public void removeMachine(String id) {
    Machine machine = findRequired(id);
    machines.remove(machine);
}
```

`List` ถูก Import ไว้แล้วตั้งแต่ EP2.9 ส่วน `List.copyOf(...)` ป้องกัน UI เพิ่มหรือลบข้อมูลใน List ของ Service โดยตรง

ตรวจว่า Service มี Method จาก EP2.10 ครบด้วย:

- `addMachine(...)`
- `findById(...)`
- `findRequired(...)`
- `updateSensor(...)`
- `countByStatus(...)`

## 2. สร้าง Service ใน FirstWindow.java

เปิด `FirstWindow.java` แล้ววางภายใน `createWindow()` หลังสร้าง `frame`:

```java
SmartFactoryService service = new SmartFactoryService();
```

เพิ่มข้อมูลเริ่มต้นหนึ่งเครื่องเพื่อให้ Table ไม่ว่าง:

```java
Machine sample = new Machine("M-001", "Mixer", "Line A");
sample.updateReading(new SensorReading(65.5, 3.1));
service.addMachine(sample);
```

## 3. สร้าง Method refreshDashboard()

วาง Method นี้ภายใน `FirstWindow` แต่ให้อยู่นอก `createWindow()`:

```java
private static void refreshDashboard(
        SmartFactoryService service,
        DefaultTableModel tableModel,
        JLabel totalLabel,
        JLabel runningLabel,
        JLabel warningLabel
) {
    tableModel.setRowCount(0);

    for (Machine machine : service.getMachines()) {
        tableModel.addRow(new Object[]{
                machine.getId(),
                machine.getName(),
                machine.getLocation(),
                machine.getStatus().name()
        });
    }

    totalLabel.setText("Total: " + service.getMachines().size());
    runningLabel.setText(
            "Running: " + service.countByStatus(MachineStatus.RUNNING)
    );
    warningLabel.setText(
            "Warning: " + service.countByStatus(MachineStatus.WARNING)
    );
}
```

ลบแถวทดลองเดิมที่เพิ่มด้วย `tableModel.addRow(...)` และลบการเรียก `refreshSummary(...)` เดิม จากนั้นเรียก Method ใหม่ก่อน `frame.setVisible(true);`:

```java
refreshDashboard(
        service, tableModel, totalLabel, runningLabel, warningLabel
);
```

Method `refreshSummary(...)` จาก EP3.7 สามารถลบออกได้ เพราะ `refreshDashboard(...)` รับหน้าที่แทนแล้ว

Compile และ Run ตรงนี้ก่อน Table ต้องแสดง M-001 จาก Service

## 4. เตรียมคำสั่ง Refresh สำหรับ Event

วางภายใน `createWindow()` หลังสร้าง Service, TableModel และ Summary Label ครบแล้ว:

```java
Runnable refreshAction = () -> refreshDashboard(
        service, tableModel, totalLabel, runningLabel, warningLabel
);
```

Listener ทุกตัวสามารถเรียก `refreshAction.run()` หลังข้อมูลเปลี่ยน โดยไม่ต้องส่ง Parameter ซ้ำหลายครั้ง

## 5. เชื่อม Inline Form กับ Service

ลบ Listener เดิมของ `saveButton` จาก EP3.4 แล้ววางชุดนี้แทน:

```java
saveButton.addActionListener(event -> {
    try {
        Machine machine = new Machine(
                idField.getText(),
                nameField.getText(),
                locationField.getText()
        );

        service.addMachine(machine);
        refreshAction.run();
        statusLabel.setText("เพิ่มเครื่องจักรแล้ว: " + machine.getId());
    } catch (IllegalArgumentException exception) {
        JOptionPane.showMessageDialog(
                frame,
                exception.getMessage(),
                "ข้อมูลไม่ถูกต้อง",
                JOptionPane.ERROR_MESSAGE
        );
    }
});
```

ทดสอบเพิ่มรหัสใหม่หนึ่งครั้ง แล้วทดลองเพิ่มรหัสซ้ำเพื่อดู Error จาก Service

## 6. เชื่อม Add Dialog กับ Service

เปลี่ยน Listener ของ `addButton` เป็น:

```java
addButton.addActionListener(
        event -> showAddDialog(frame, service, refreshAction)
);
```

จากนั้นเปลี่ยน Parameter ของ Method `showAddDialog(...)` เป็น:

```java
private static void showAddDialog(
        JFrame frame,
        SmartFactoryService service,
        Runnable refreshAction
) {
```

ภายใน Method เดิม ให้แทนที่บรรทัดที่เปลี่ยน `statusLabel` ด้วย:

```java
try {
    service.addMachine(new Machine(id, name, location));
    refreshAction.run();
} catch (IllegalArgumentException exception) {
    JOptionPane.showMessageDialog(
            frame,
            exception.getMessage(),
            "ข้อมูลไม่ถูกต้อง",
            JOptionPane.ERROR_MESSAGE
    );
}
```

ตอนนี้ทั้ง Inline Form และ Add Dialog เพิ่มข้อมูลผ่าน Service เดียวกัน

## 7. อัปเดต Sensor ของแถวที่เลือก

ลบ Listener เดิมของ `updateButton` ถ้ามี แล้ววาง:

```java
updateButton.addActionListener(event -> {
    int selectedRow = table.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(frame, "กรุณาเลือกเครื่องจักรก่อน");
        return;
    }

    String id = tableModel.getValueAt(selectedRow, 0).toString();
    String input = JOptionPane.showInputDialog(
            frame,
            "กรอกอุณหภูมิใหม่",
            "65.0"
    );

    if (input == null) {
        return;
    }

    try {
        double temperature = Double.parseDouble(input.trim());
        service.updateSensor(id, temperature, 3.0);
        refreshAction.run();
    } catch (NumberFormatException exception) {
        JOptionPane.showMessageDialog(frame, "กรุณากรอกตัวเลขให้ถูกต้อง");
    } catch (IllegalArgumentException exception) {
        JOptionPane.showMessageDialog(frame, exception.getMessage());
    }
});
```

ใน EP นี้กำหนดแรงสั่นเป็น `3.0` ชั่วคราวเพื่อให้โค้ดสั้น Challenge จะให้เพิ่มช่องแรงสั่นใน Dialog

## 8. ลบ Machine ที่เลือก

ถ้ายังไม่มี `deleteButton` ให้สร้างและเพิ่มลง `actions`:

```java
JButton deleteButton = new JButton("Delete");
actions.add(deleteButton);
```

ถ้าทำ Challenge ของ EP3.2 แล้ว ให้ใช้ตัวแปรเดิมและไม่สร้างซ้ำ

ผูก Event หลังสร้าง `refreshAction`:

```java
deleteButton.addActionListener(event -> {
    int selectedRow = table.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(frame, "กรุณาเลือกเครื่องจักรก่อน");
        return;
    }

    String id = tableModel.getValueAt(selectedRow, 0).toString();
    service.removeMachine(id);
    refreshAction.run();
});
```

## 9. Compile และ Run

```powershell
javac -encoding UTF-8 MachineStatus.java SensorReading.java FactoryDevice.java Maintainable.java Machine.java SmartFactoryService.java FirstWindow.java
java -Dfile.encoding=UTF-8 FirstWindow
```

ทดสอบตามลำดับ:

1. เปิดโปรแกรมแล้วเห็นข้อมูลเริ่มต้น
2. เพิ่ม Machine ผ่าน Inline Form
3. เพิ่ม Machine ผ่าน Add Dialog
4. เลือกแถวแล้วอัปเดตอุณหภูมิ
5. ตรวจว่าอุณหภูมิสูงทำให้สถานะเป็น WARNING
6. เลือกแถวแล้วลบ
7. ตรวจว่า Table และ Summary เปลี่ยนตาม Service ทุกครั้ง

## ตรวจความพร้อมก่อนเข้า EP 3.9

- UI ไม่เก็บ `List<Machine>` ของตัวเอง
- ทุก Action เรียก `SmartFactoryService`
- ทุกการเปลี่ยนข้อมูลจบด้วย `refreshAction.run()`
- Error จาก Model และ Service ถูกแสดงด้วย Dialog
- เพิ่ม อัปเดต และลบได้จริง

ซอร์สฉบับเต็ม: [`SmartFactoryFrame.java`](../../src/main/java/smartfactory/ui/SmartFactoryFrame.java)

## Challenge

1. เปลี่ยน Update Dialog ให้มีช่องอุณหภูมิและแรงสั่นสะเทือน
2. เพิ่ม Confirm Dialog ก่อนลบ
3. เพิ่มปุ่มค้นหา ID โดยใช้ `service.findById(...)`

ทำทีละข้อและ Run ตรวจหลังจบแต่ละข้อ

ถัดไป: [EP 3.9 — Timer และ UI Thread](ep09-timer-thread.md)
