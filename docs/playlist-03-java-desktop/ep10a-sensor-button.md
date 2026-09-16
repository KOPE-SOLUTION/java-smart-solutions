# EP 3.10 ตอนที่ 1 — กดปุ่มจำลอง Sensor

เป้าหมาย: กดหนึ่งครั้งแล้วค่าอุณหภูมิ แรงสั่น และ Summary อัปเดต โดยยังไม่ใช้ Task หรือ Thread

ใช้โปรเจกต์ที่จบ [EP3.9 ตอนที่ 4](ep09c-service-maintenance.md) ต่อ ปิดหน้าต่างโปรแกรมก่อนแก้โค้ด

แก้ไฟล์ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

## 1. เพิ่มคอลัมน์ Sensor

เพิ่ม Import ต่อจาก Import เดิม:

```java
import java.util.Locale;
import java.util.Random;
```

ใน `buildMachineTable()` หลังตั้งค่า `maintenanceColumn` และก่อน `machineTable.getColumns().setAll(...)` เพิ่ม:

```java
TableColumn<Machine, String> temperatureColumn = new TableColumn<>("อุณหภูมิ °C");
temperatureColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(format(data.getValue().getLatestReading().getTemperature())));

TableColumn<Machine, String> vibrationColumn = new TableColumn<>("แรงสั่น mm/s");
vibrationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(format(data.getValue().getLatestReading().getVibration())));
```

แทนที่บรรทัด `machineTable.getColumns().setAll(...)` เดิม:

```java
machineTable.getColumns().setAll(idColumn, nameColumn, locationColumn, statusColumn,
        temperatureColumn, vibrationColumn, hoursColumn, maintenanceColumn);
```

เพิ่ม Method หลังปีกกาปิดของ `buildMachineTable()` ก่อน `refreshDashboard()`:

```java
private static String format(double value) {
    return String.format(Locale.ROOT, "%.1f", value);
}
```

`%.1f` แสดงทศนิยมหนึ่งตำแหน่ง เช่น `65.5`

## 2. เพิ่มปุ่มจำลองหนึ่งครั้ง

เพิ่ม Field ต่อจาก `addButton`:

```java
private final Button sensorButton = new Button("จำลอง Sensor 1 ครั้ง");
```

เพิ่ม Method หลังปีกกาปิดของ `refreshDashboard()`:

```java
private void simulateOnce() {
    service.simulateSensorReadings(new Random());
    refreshDashboard();
    statusLabel.setText("อัปเดต Sensor แล้ว");
}
```

ใน `buildMachineForm()` เพิ่มหลัง `actionButtons.getChildren().add(maintenanceButton);`:

```java
sensorButton.setOnAction(event -> simulateOnce());
actionButtons.getChildren().add(sensorButton);
```

ใช้ Method จำลองของ Service ที่มีอยู่แล้ว งานสุ่มสั้น ๆ นี้ทำบน Thread ของหน้าจอได้ ส่วนงานที่ต้องรอจะทดลองในตอนหน้า

## 3. รันและตรวจผล

บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- เปิดแล้วเห็นสองคอลัมน์ใหม่ เช่น M-001 มีอุณหภูมิ `65.5` และแรงสั่น `3.1`
- กดปุ่มหนึ่งครั้ง ค่า Sensor อัปเดตทุกเครื่อง และชั่วโมงเพิ่มเครื่องละ 1
- ถ้าไม่กดปุ่ม ค่าไม่อัปเดตเอง สถานะและ Summary อาจเหมือนเดิมได้หากยังอยู่ในช่วงเดิม

ชั่วโมงนี้เป็นค่าจำลอง ไม่ใช่เวลาจริง Method เดิมสุ่มอุณหภูมิต่ำกว่า 90 จึงยังไม่เกิดสถานะหยุดฉุกเฉินจากการสุ่มในตอนนี้

ถัดไป: [ตอนที่ 2 — ทดลองงานเบื้องหลังด้วย Task และ Thread](ep10b-task-thread.md)
