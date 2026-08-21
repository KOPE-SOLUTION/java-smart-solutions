# EP 2.5 — Composition และ Value Object

## เป้าหมาย

- ใช้ความสัมพันธ์แบบ has-a
- รวมอุณหภูมิและแรงสั่นสะเทือนเป็นข้อมูล Sensor หนึ่งชุด
- ให้ `Machine` รับและอ่านค่า `SensorReading` ได้จริง
- ป้องกันค่าภายใน `SensorReading` ถูกแก้หลังสร้าง Object

EP นี้ทำต่อจากไฟล์ `Machine.java`, `MachineStatus.java` และ `ClassObjectDemo.java` ของ EP 2.4 โดยวางทุกไฟล์ไว้ในโฟลเดอร์เดียวกัน

ลำดับที่เราจะทำคือ:

1. สร้าง `SensorReading.java`
2. ให้ `Machine` เก็บ SensorReading ล่าสุด
3. เพิ่ม Method สำหรับอัปเดตและอ่านค่า
4. ทดลองใช้งานใน `ClassObjectDemo.java`

## 1. สร้างไฟล์ SensorReading.java

สร้างไฟล์ใหม่ชื่อ `SensorReading.java` แล้ววางโครง Class:

```java
public class SensorReading {
    private final double temperature;
    private final double vibration;
}
```

ค่า Sensor หนึ่งครั้งประกอบด้วยอุณหภูมิและแรงสั่นสะเทือน จึงเก็บสองค่านี้ไว้ใน Object เดียวกัน

## 2. เพิ่ม Constructor

วาง Constructor ต่อจาก Field และยังอยู่ภายใน Class `SensorReading`:

```java
public SensorReading(double temperature, double vibration) {
    if (vibration < 0) {
        throw new IllegalArgumentException("vibration must not be negative");
    }

    this.temperature = temperature;
    this.vibration = vibration;
}
```

Field ทั้งสองเป็น `final` จึงต้องได้รับค่าภายใน Constructor และจะเปลี่ยนภายหลังไม่ได้ ลักษณะนี้เหมาะกับ Value Object ที่ใช้แทนข้อมูลหนึ่งชุด

## 3. เพิ่ม Getter

วาง Getter ต่อจาก Constructor ภายใน `SensorReading`:

```java
public double getTemperature() {
    return temperature;
}

public double getVibration() {
    return vibration;
}
```

ตอนนี้ `SensorReading.java` พร้อมใช้งานแล้ว ยังไม่ต้องแก้ไฟล์นี้เพิ่มเติม

## 4. ให้ Machine เก็บ SensorReading

เปิด `Machine.java` แล้วทำตามทีละส่วน

### 4.1 เพิ่ม Field

วาง Field นี้ไว้บริเวณเดียวกับ `id` และ `name`:

```java
private SensorReading latestReading;
```

บรรทัดนี้เพียงประกาศว่า Machine สามารถเก็บ SensorReading ได้ แต่ยังไม่มี Object ถูกนำมาใส่ จึงต้องกำหนดค่าใน Constructor ต่อ

### 4.2 กำหนดค่าเริ่มต้นใน Constructor

แทนที่ Constructor เดิมของ `Machine` ด้วยรูปแบบนี้ เพื่อให้ทุกคนเริ่มจากโครงเดียวกัน:

```java
public Machine(String id, String name) {
    if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("id must not be blank");
    }
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("name must not be blank");
    }

    this.id = id.trim();
    this.name = name.trim();
    this.latestReading = new SensorReading(0, 0);
}
```

ถ้า Constructor เดิมรับ `double temp` ให้ลบ Parameter นั้นออก เพราะต่อจากนี้เราจะอัปเดตค่า Sensor ผ่าน `updateReading(...)`

จากนั้นลบ Field เดิมนี้ เพราะอุณหภูมิถูกย้ายไปเก็บใน `SensorReading` แล้ว:

```java
private double temperature;
```

หลังจบส่วนนี้ `Machine` จะเก็บอุณหภูมิและแรงสั่นสะเทือนผ่าน `latestReading` เพียงจุดเดียว ไม่เกิดข้อมูลซ้ำ

### 4.3 เปลี่ยน Method อัปเดตอุณหภูมิ

ลบ Method `updateTemperature(...)` เดิม แล้ววาง Method นี้แทน:

```java
public void updateReading(SensorReading reading) {
    if (reading == null) {
        throw new IllegalArgumentException("reading must not be null");
    }

    this.latestReading = reading;
}
```

ตอนเรียก Method เราจะส่ง SensorReading เข้ามาทั้ง Object แทนการส่งอุณหภูมิเพียงค่าเดียว

### 4.4 เพิ่ม Getter สำหรับ Sensor ล่าสุด

วางต่อจาก `updateReading(...)`:

```java
public SensorReading getLatestReading() {
    return latestReading;
}
```

### 4.5 ปรับ getSummary()

ถ้า `getSummary()` เดิมยังอ่านตัวแปร `temperature` ให้เปลี่ยน Return เป็น:

```java
return "Machine ID: " + id
        + ", Name: " + name
        + ", Temperature: " + latestReading.getTemperature()
        + " C, Vibration: " + latestReading.getVibration() + " mm/s";
```

นี่เป็นเพียงการเปลี่ยนบรรทัด `return` ภายใน Method เดิม ไม่ต้องสร้าง `getSummary()` ซ้ำ

## 5. ทดลองใน ClassObjectDemo.java

เปิด `ClassObjectDemo.java` แล้ววางภายใน `main`:

```java
Machine mixer = new Machine("M-001", "Mixer");

SensorReading reading = new SensorReading(65.5, 3.1);
mixer.updateReading(reading);

System.out.println(mixer.getSummary());
```

สิ่งที่เกิดขึ้นมีสามขั้นตอน:

1. สร้าง Machine
2. สร้าง SensorReading
3. ส่ง SensorReading ให้ Machine เก็บผ่าน `updateReading(...)`

ความสัมพันธ์นี้เรียกว่า Composition หรือ has-a เพราะ Machine **มี** SensorReading อยู่ภายใน

## 6. Compile และ Run

เปิด Terminal ในโฟลเดอร์ที่เก็บไฟล์ Java แล้วรัน:

```powershell
javac -encoding UTF-8 MachineStatus.java SensorReading.java Machine.java ClassObjectDemo.java
java -Dfile.encoding=UTF-8 ClassObjectDemo
```

ตัวอย่างผลลัพธ์:

```text
Machine ID: M-001, Name: Mixer, Temperature: 65.5 C, Vibration: 3.1 mm/s
```

## ตรวจความพร้อมก่อนเข้า EP 2.6

- มีไฟล์ `SensorReading.java`
- `Machine` ไม่มี Field `temperature` ซ้ำกับ SensorReading
- Constructor กำหนดค่าให้ `latestReading` แล้ว
- `Machine` มี `updateReading(...)` และ `getLatestReading()`
- Compile และ Run ได้โดยไม่มี Error

ซอร์สฉบับเต็มสำหรับตรวจคำตอบ:

- [`SensorReading.java`](../../src/main/java/smartfactory/model/SensorReading.java)
- [`Machine.java`](../../src/main/java/smartfactory/model/Machine.java)

## Challenge

เพิ่มเวลาที่บันทึกค่า Sensor โดยยังรักษา Constructor แบบสอง Parameter ไว้:

1. เพิ่ม `import java.time.LocalDateTime;` ไว้บรรทัดแรกของ `SensorReading.java`
2. เพิ่ม Field `private final LocalDateTime recordedAt;`
3. เปลี่ยน Constructor สอง Parameter ให้เรียก `this(temperature, vibration, LocalDateTime.now());`
4. เพิ่ม Constructor อีกแบบที่รับ `LocalDateTime recordedAt`
5. ปฏิเสธ `recordedAt == null` ก่อนกำหนดให้ Field
6. เพิ่ม Getter `getRecordedAt()`

เมื่อเก็บ Constructor สอง Parameter ไว้ โค้ด `new SensorReading(0, 0)` ใน `Machine` จะยังทำงานได้ ส่วนตอนที่ต้องระบุเวลาเองจึงค่อยใช้ Constructor แบบสาม Parameter

ถัดไป: [EP 2.6 — Inheritance และ Abstract Class](ep06-inheritance-abstract.md)
