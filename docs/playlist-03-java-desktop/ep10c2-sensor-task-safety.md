# EP 3.10 ตอนที่ 3B — ป้องกันงานซ้อนและรับข้อผิดพลาด

เป้าหมาย: ระหว่างรองาน กดเริ่มซ้อนไม่ได้ และเมื่อสำเร็จหรือล้มเหลว ปุ่มกลับมาใช้งานได้

ใช้โปรเจกต์จาก [ตอนที่ 3A](ep10c-sensor-task-result.md) ต่อ ปิดโปรแกรมก่อนแก้ `DashboardApp.java` ในโฟลเดอร์ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop`

## 1. ป้องกันเริ่มงานซ้อน

เพิ่ม Field ถัดจาก `sensorButton` ให้อยู่ระดับเดียวกัน นอกทุก Method:

```java
private boolean sensorBusy;
```

ใน `DashboardApp.java` ภายใน `simulateInBackground()` เพิ่มสองเงื่อนไขนี้ทันทีหลังบรรทัดเปิด Method ก่อน `List<String> ids = new ArrayList<>();` ที่มีจาก 3A:

```java
if (sensorBusy) {
    return;
}
if (service.getMachines().isEmpty()) {
    statusLabel.setText("ยังไม่มีเครื่องจักรให้จำลอง");
    return;
}
```

ตรวจให้ทั้งสอง `if` อยู่ก่อนการสร้างรายการ: ถ้ายังมีงานหรือไม่มีเครื่องจักร เรา `return` ได้ทันที ไม่ต้องเตรียมข้อมูลที่ไม่ได้ใช้

จากนั้นเพิ่ม **หลังปีกกาปิด Loop `for (Machine machine : service.getMachines())`** ก่อน `SensorSimulationTask task = ...` ไม่วางไว้ภายใน Loop:

```java
sensorBusy = true;
sensorButton.setDisable(true);
statusLabel.setText("กำลังจำลองค่า Sensor...");
```

`sensorBusy` กันการเรียก Method ซ้อน ส่วน `setDisable(true)` ทำให้ผู้ใช้เห็นว่าปุ่มยังไม่พร้อม

## 2. คืนสถานะเมื่อจบงาน

เพิ่ม Method นี้ถัดจากปีกกาปิด `simulateInBackground()` ทันที ก่อน Method ถัดไป โดยอยู่ระดับเดียวกัน:

```java
private void finishSensorTask() {
    sensorBusy = false;
    sensorButton.setDisable(false);
}
```

ใน `simulateInBackground()` เพิ่มเป็นบรรทัดแรกภายใน `task.setOnSucceeded(event -> {` ก่อน Loop อ่านผล:

```java
finishSensorTask();
```

เพิ่มตัวรับข้อผิดพลาด **หลัง `});` ที่ปิด `setOnSucceeded` และก่อน `Thread worker = ...`:

```java
task.setOnFailed(event -> {
    finishSensorTask();
    statusLabel.setText("จำลองค่า Sensor ไม่สำเร็จ กรุณาลองใหม่");
});
```

`setOnFailed` ใช้เมื่อ `call()` ล้มเหลว ต้องคืนปุ่มเหมือนกรณีสำเร็จ ทั้งสองตัวรับเหตุการณ์ทำงานบน JavaFX Thread

## 3. ข้ามผลของเครื่องที่ถูกลบระหว่างรอ

ใน `DashboardApp.java` ภายใน `simulateInBackground()` เพิ่มบรรทัดนี้ **หลังปีกกาปิด `if (service.getMachines().isEmpty())` และก่อน `List<String> ids = new ArrayList<>();`** ไม่วางไว้ก่อน `if (sensorBusy)`:

```java
List<Machine> snapshot = List.copyOf(service.getMachines());
```

แทนที่เฉพาะหัว Loop เก็บรหัส จาก `for (Machine machine : service.getMachines()) {` เป็น:

```java
for (Machine machine : snapshot) {
```

เก็บ `ids.add(machine.getId());` และปีกกาปิด Loop เดิมไว้ ลำดับต้น Method ต้องเป็น **ตรวจงานซ้อน → ตรวจรายการว่าง → สร้าง snapshot → เก็บรหัส**

<details>
<summary>ดูตำแหน่งต้น Method หลังทำขั้นนี้</summary>

ใช้เทียบกับโค้ดที่มี ไม่เพิ่ม Method ซ้ำ ตัวอย่างนี้แสดงเฉพาะส่วนต้น ส่วนสร้าง Task และตัวรับผลด้านล่างเก็บไว้ตามเดิม:

```java
private void simulateInBackground() {
    if (sensorBusy) {
        return;
    }
    if (service.getMachines().isEmpty()) {
        statusLabel.setText("ยังไม่มีเครื่องจักรให้จำลอง");
        return;
    }

    List<Machine> snapshot = List.copyOf(service.getMachines());
    List<String> ids = new ArrayList<>();
    for (Machine machine : snapshot) {
        ids.add(machine.getId());
    }

    sensorBusy = true;
    sensorButton.setDisable(true);
    statusLabel.setText("กำลังจำลองค่า Sensor...");
    // ต่อด้วย SensorSimulationTask task = ... และโค้ดเดิมจนจบ Method
```

ถ้ามี `snapshot` หรือ `ids` อยู่ก่อนสอง `if` ให้ย้ายลงมา ไม่ประกาศซ้ำ การวางไว้ก่อนยังทำงานได้ในตัวอย่างนี้ แต่จะสร้างรายการโดยไม่จำเป็นในรอบที่ต้อง `return`

</details>

ภายใน `setOnSucceeded` เพิ่มเป็นส่วนแรกของ Loop `for (SensorUpdate update : task.getValue())` ก่อน `service.updateSensor(...);`:

```java
Machine current = service.findById(update.machineId()).orElse(null);
if (current == null || !snapshot.contains(current)) {
    continue;
}
```

`snapshot` เก็บรายการอ้างอิงเครื่องที่เริ่มงานรอบนี้ ไม่ได้แช่แข็งข้อมูลภายใน Machine ส่วน `orElse(null)` ให้ค่า null เมื่อหาไม่พบ และ `continue` ข้ามผลนั้นไป

ใน Model ปัจจุบัน Machine ไม่ได้ Override `equals()` การตรวจ `contains` จึงแยกเครื่องเดิมออกจาก Object ที่เพิ่งสร้างใหม่ แม้ใช้รหัสซ้ำกันได้

ถ้ากดบำรุงรักษาระหว่างรอ ผล Sensor รอบนี้ยังนำมาใช้กับเครื่องเดิมได้ จึงอาจเพิ่มชั่วโมงและเปลี่ยน OFFLINE อีกครั้ง หากต้องการดูผลบำรุงรักษาค้างไว้ ให้รองานจบก่อนกด

## 4. รันและตรวจผลให้เห็นชัด

ใน `SensorSimulationTask.java` แทนที่เฉพาะหัว Method `protected List<SensorUpdate> call() {` ด้วยสองบรรทัดนี้ เพื่อรอ 3 วินาทีชั่วคราว:

```java
protected List<SensorUpdate> call() throws Exception {
    Thread.sleep(3000);
```

เก็บเนื้อหาเดิมตั้งแต่ `Random random = ...` จนถึงปีกกาปิดไว้ครบ การรออยู่บน Background Thread จึงไม่หยุดหน้าจอ

บันทึกทั้งสองไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

1. เปิดโปรแกรมใหม่ กดจำลอง ปุ่มต้องถูกปิดระหว่างรอ กดซ้ำไม่ได้ เมื่อครบประมาณ 3 วินาที ปุ่มกลับมาใช้ได้ ชั่วโมง M-001 เพิ่มจาก 121 เป็น 122 เพียงครั้งเดียว
2. กดจำลองอีกครั้ง แล้วลบ M-002 ก่อนครบเวลา เมื่อรับผล แถวนั้นต้องไม่กลับมา ส่วนเครื่องที่เหลืออัปเดตตามปกติ
3. เมื่องานจบ ลบทุกเครื่องแล้วกดจำลอง ต้องขึ้น **ยังไม่มีเครื่องจักรให้จำลอง** และปุ่มไม่ค้าง

### ทดลองงานล้มเหลว

ปิดแล้วเปิดโปรแกรมใหม่เพื่อให้มีข้อมูลตัวอย่าง ก่อนรันรอบนี้ ให้แก้ `SensorSimulationTask.call()` โดยแทนที่เฉพาะ `return results;` ชั่วคราวด้วย:

```java
throw new IllegalStateException("ทดลองงานล้มเหลว");
```

กดจำลอง รอ 3 วินาที ต้องเห็น **จำลองค่า Sensor ไม่สำเร็จ กรุณาลองใหม่** ปุ่มกลับมาใช้ได้ และค่า Sensor กับชั่วโมงไม่เปลี่ยน

**ก่อนเรียนต่อ:** ปิดโปรแกรม คืน `return results;` ลบ `Thread.sleep(3000);` และนำ `throws Exception` ออกจากหัว `call()` จากนั้นรันอีกครั้ง ตรวจว่ากดจำลองสำเร็จและปุ่มกลับมาใช้งานได้

ยังไม่เพิ่มการยกเลิกงานในตอนนี้ ให้รอรอบปัจจุบันจบก่อนปิดหน้าต่าง

ถัดไป: [ตอนที่ 4 — เริ่มและหยุด Auto Sensor ด้วย Timeline](ep10d-auto-sensor-timeline.md)
