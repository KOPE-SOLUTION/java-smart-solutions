# EP 2.6 — Inheritance และ Abstract Class

## เป้าหมาย

- ใช้ความสัมพันธ์แบบ is-a
- ย้ายข้อมูลร่วมไปไว้ใน Parent Class
- เรียก Constructor ของ Parent ด้วย `super(...)`
- บังคับ Class ลูกด้วย Abstract Method

EP นี้ทำต่อจาก EP 2.5 และจะแก้สามไฟล์ตามลำดับ:

1. สร้าง `FactoryDevice.java`
2. แก้ `Machine.java` ให้สืบทอด Parent
3. แก้การสร้าง Machine ใน `ClassObjectDemo.java`

## 1. สร้างไฟล์ FactoryDevice.java

สร้างไฟล์ใหม่ชื่อ `FactoryDevice.java` ในโฟลเดอร์เดียวกับ `Machine.java` แล้ววางโครง Class:

```java
public abstract class FactoryDevice {
    private final String id;
    private String name;
    private String location;
}
```

`abstract` หมายถึง Class นี้ใช้เป็นแบบกลางของอุปกรณ์ในโรงงาน จึงไม่สร้าง Object ด้วย `new FactoryDevice(...)` โดยตรง

## 2. เพิ่ม Constructor ของ Parent

วาง Constructor ภายใน `FactoryDevice` ต่อจาก Field:

```java
protected FactoryDevice(String id, String name, String location) {
    if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("id must not be blank");
    }
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("name must not be blank");
    }
    if (location == null || location.isBlank()) {
        throw new IllegalArgumentException("location must not be blank");
    }

    this.id = id.trim();
    this.name = name.trim();
    this.location = location.trim();
}
```

ใช้ `protected` เพื่อให้ Constructor นี้ถูกเรียกจาก Class ลูกผ่าน `super(...)`

## 3. เพิ่ม Getter และ Abstract Method

วางต่อจาก Constructor ภายใน `FactoryDevice`:

```java
public String getId() {
    return id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("name must not be blank");
    }
    this.name = name.trim();
}

public String getLocation() {
    return location;
}

public abstract String getDeviceType();
```

Field ของ Parent เป็น `private` ดังนั้น Class ลูกต้องอ่านค่าผ่าน Getter ส่วน `getDeviceType()` ยังไม่มีคำตอบ เพราะอุปกรณ์แต่ละชนิดตอบไม่เหมือนกัน

## 4. ให้ Machine สืบทอด FactoryDevice

เปิด `Machine.java` แล้วแก้ทีละส่วน

### 4.1 เปลี่ยนส่วนประกาศ Class

เปลี่ยนบรรทัดแรกของ Class จาก:

```java
public class Machine {
```

เป็น:

```java
public class Machine extends FactoryDevice {
```

### 4.2 ลบ Field ที่ Parent เก็บให้แล้ว

ลบเฉพาะ Field สองตัวนี้ออกจาก `Machine`:

```java
private final String id;
private String name;
```

อย่าลบ `latestReading` เพราะเป็นข้อมูลเฉพาะของ Machine และยังต้องใช้ต่อ

`location` เป็นข้อมูลใหม่ใน EP นี้และอยู่ใน `FactoryDevice` อยู่แล้ว จึงไม่ต้องเพิ่มซ้ำใน `Machine`

ถ้า `Machine` ยังมี `getName()` และ `setName(...)` จาก EP ก่อนหน้า ให้ลบสอง Method นั้นออกด้วย เพราะตอนนี้ Parent มี Method ทั้งสองให้ใช้งานแล้ว หากเก็บไว้ Method เดิมจะยังอ้างถึง Field `name` ที่ถูกลบและทำให้ Compile ไม่ผ่าน

### 4.3 เปลี่ยน Constructor ของ Machine

แทนที่ Constructor เดิมของ `Machine` ด้วย:

```java
public Machine(String id, String name, String location) {
    super(id, name, location);
    this.latestReading = new SensorReading(0, 0);
}
```

บรรทัด `super(...)` ต้องเป็นคำสั่งแรกของ Constructor เพื่อส่ง `id`, `name` และ `location` ให้ Parent เป็นผู้ตรวจและเก็บค่า

อุณหภูมิเริ่มต้นไม่รับผ่าน Constructor แล้ว ให้เริ่มจาก `0` และอัปเดตภายหลังด้วย `updateReading(...)`

### 4.4 Override Method ที่ Parent บังคับ

วางภายใน `Machine` ต่อจาก Constructor:

```java
@Override
public String getDeviceType() {
    return "Machine";
}
```

ถ้ายังไม่เพิ่ม Method นี้ Java จะ Compile ไม่ผ่าน เพราะ `Machine` ไม่ใช่ Abstract Class และต้องตอบ `getDeviceType()` ให้ครบ

### 4.5 ปรับ getSummary()

เมื่อ `id` และ `name` ย้ายไปอยู่ใน Parent แล้ว ให้เปลี่ยนบรรทัด `return` ของ `getSummary()` เป็น:

```java
return getId() + " | " + getName() + " | " + getLocation()
        + " | " + latestReading.getTemperature() + " C"
        + " | " + latestReading.getVibration() + " mm/s";
```

อย่าอ้าง `id` หรือ `name` โดยตรงจาก `Machine` เพราะ Field ของ Parent เป็น `private`

## 5. แก้ ClassObjectDemo.java

เปลี่ยนการสร้าง Machine ให้ส่งตำแหน่งเป็นค่าที่สาม แล้วค่อยอัปเดต Sensor:

```java
Machine mixer = new Machine("M-001", "Mixer", "Line A");
mixer.updateReading(new SensorReading(65.5, 3.1));

System.out.println(mixer.getDeviceType());
System.out.println(mixer.getSummary());
```

ตอนนี้ความสัมพันธ์มีสองแบบ:

- Machine **เป็น** FactoryDevice — is-a หรือ Inheritance
- Machine **มี** SensorReading — has-a หรือ Composition

## 6. Compile และ Run

```powershell
javac -encoding UTF-8 MachineStatus.java SensorReading.java FactoryDevice.java Machine.java ClassObjectDemo.java
java -Dfile.encoding=UTF-8 ClassObjectDemo
```

ตัวอย่างผลลัพธ์:

```text
Machine
M-001 | Mixer | Line A | 65.5 C | 3.1 mm/s
```

## ตรวจความพร้อมก่อนเข้า EP 2.7

- มีไฟล์ `FactoryDevice.java`
- `Machine extends FactoryDevice`
- `Machine` ไม่มี Field `id` และ `name` ซ้ำกับ Parent
- Constructor ของ `Machine` เรียก `super(...)`
- `Machine` Override `getDeviceType()` แล้ว
- Compile และ Run ได้โดยไม่มี Error

ซอร์สฉบับเต็มสำหรับตรวจคำตอบ:

- [`FactoryDevice.java`](../../src/main/java/smartfactory/model/FactoryDevice.java)
- [`Machine.java`](../../src/main/java/smartfactory/model/Machine.java)

## Challenge

สร้างไฟล์ `EnergyMeter.java` แล้วให้ Class สืบทอด `FactoryDevice`:

```java
public class EnergyMeter extends FactoryDevice {
    public EnergyMeter(String id, String name, String location) {
        super(id, name, location);
    }

    @Override
    public String getDeviceType() {
        return "Energy Meter";
    }
}
```

จากนั้นสร้าง Object ใน `ClassObjectDemo.java` แล้วเรียก `getDeviceType()` เพื่อตรวจผล

ถัดไป: [EP 2.7 — Interface](ep07-interface.md)
