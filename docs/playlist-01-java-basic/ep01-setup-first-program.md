# EP 1.1 — ติดตั้ง JDK และโปรแกรม Java แรก

## เป้าหมาย

- ตรวจว่าเครื่องมีคำสั่ง `java` และ `javac`
- สร้างไฟล์ `.java` แล้วคอมไพล์เป็น `.class`
- แสดงข้อความจากโปรแกรม Java

## 1. ตรวจเครื่องมือ

```powershell
java -version
javac -version
```

ทั้งสองคำสั่งควรแสดงเวอร์ชัน 17 ขึ้นไป ถ้าพบ `java` แต่ไม่พบ `javac` ให้ตรวจว่าติดตั้ง JDK ไม่ใช่เฉพาะ Runtime

## 2. สร้างโครงไฟล์

สร้างไฟล์ `HelloFactory.java` แล้ววางโค้ดนี้:

```java
public class HelloFactory {
    public static void main(String[] args) {
        // เพิ่มโค้ดในส่วนถัดไปตรงนี้
    }
}
```

ชื่อไฟล์ต้องตรงกับชื่อ `public class` รวมถึงตัวพิมพ์เล็กและใหญ่

## 3. เพิ่มคำสั่งแสดงผล

วางสองบรรทัดนี้ภายใน `main`:

```java
System.out.println("Smart Factory Monitor");
System.out.println("Java is ready!");
```

## 4. Compile และ Run

```powershell
javac -encoding UTF-8 HelloFactory.java
java -Dfile.encoding=UTF-8 HelloFactory
```

ผลลัพธ์:

```text
Smart Factory Monitor
Java is ready!
```

`javac` สร้าง `HelloFactory.class` ส่วน `java` สั่ง JVM ให้เริ่มทำงานที่ method `main`

## Checkpoint

- เปลี่ยนข้อความบรรทัดที่สองเป็นชื่อโรงงานของคุณ
- ลบ semicolon หนึ่งตำแหน่ง แล้วอ่านเลขบรรทัดจาก Compile Error
- แก้กลับจนคอมไพล์ผ่าน

## Challenge

เพิ่มอีกหนึ่งบรรทัดให้ผลลัพธ์มีชื่อโรงงาน ชื่อเครื่องจักร และสถานะเริ่มต้น เช่น `KOPES Factory`, `Mixer M-001` และ `Status: READY`

ถัดไป: [EP 1.2 — Variable และ Data Type](ep02-variables-data-types.md)
