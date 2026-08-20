# EP 1.1 — ติดตั้ง JDK และโปรแกรม Java แรก

## เป้าหมาย

- แยกความหมายของ JDK, `javac` และ `java`
- สร้างไฟล์ `.java` แล้วคอมไพล์เป็น `.class`
- รันโปรแกรมแรกในบริบท Smart Factory

## ตรวจเครื่องมือ

```powershell
java -version
javac -version
```

ทั้งสองคำสั่งควรแสดงเวอร์ชัน 17 ขึ้นไป ถ้าพบเฉพาะ `java` แต่ไม่พบ `javac` ให้ตรวจว่าติดตั้ง JDK ไม่ใช่เฉพาะ Runtime

## สร้างไฟล์ `HelloFactory.java`

```java
public class HelloFactory {
    public static void main(String[] args) {
        System.out.println("Smart Factory Monitor");
        System.out.println("Java is ready!");
    }
}
```

ชื่อไฟล์ต้องตรงกับชื่อ `public class` รวมถึงตัวพิมพ์เล็กและใหญ่

## Compile และ Run

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
- ลบ semicolon หนึ่งตำแหน่ง รัน `javac` และอ่านเลขบรรทัดจาก error
- แก้กลับจนคอมไพล์ผ่าน

## Challenge

พิมพ์ข้อความสามบรรทัด: ชื่อโรงงาน, ชื่อเครื่องจักร และสถานะเริ่มต้น

ถัดไป: [EP 1.2 — Variable และ Data Type](ep02-variables-data-types.md)

