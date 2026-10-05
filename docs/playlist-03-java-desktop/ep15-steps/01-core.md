# EP3.15 — ขั้นที่ 1: แก้ข้อมูลผ่าน Service

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.15-1 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml test-compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java "-Dexec.mainClass=smartfactory.SmartFactoryTest" "-Dexec.classpathScope=test"
```

ขั้นนี้ตรวจ Model และ Service ใน Console ก่อน ยังไม่ต้องแก้หน้าจอ

## 1. แก้ชื่อและตำแหน่งพร้อมกัน

ใน `src/main/java/smartfactory/model/FactoryDevice.java` เพิ่มหลัง `setLocation()`:

```java
public void updateDetails(String name, String location) {
        String validName = requireText(name, "name");
        String validLocation = requireText(location, "location");
        this.name = validName;
        this.location = validLocation;
    }
```

ตรวจทั้งสองค่าก่อนเปลี่ยน Field เพื่อไม่ให้ชื่อเปลี่ยนไปครึ่งเดียวเมื่อ Location ไม่ผ่าน

ใน `src/main/java/smartfactory/service/SmartFactoryService.java` เพิ่มหลัง `removeMachine()`:

```java
public void updateMachineDetails(String id, String name, String location) {
        findRequired(id).updateDetails(name, location);
    }
```

Service หาเครื่อง ส่วน Model ตรวจและเก็บข้อมูล เช่นเดียวกับงานบำรุงรักษาที่ผ่านมา

## 2. ตรวจโดยยังไม่แก้ UI

ใน `src/test/java/smartfactory/SmartFactoryTest.java` เพิ่ม Method ด้านล่างหลัง `testDuplicateIdIsRejected()`:

<details>
<summary>เปิดโค้ด Test ที่จะเพิ่ม</summary>

```java
private static void testUpdateDetails() {
        SmartFactoryService service = SmartFactoryService.createWithSampleData();
        Machine machine = service.findRequired("M-002");
        SensorReading reading = machine.getLatestReading();
        int hours = machine.getOperatingHours();
        MachineStatus status = machine.getStatus();
        boolean maintenance = machine.requiresMaintenance();
        service.updateMachineDetails("M-002", " สายพานลำเลียง ", " Line B ");
        assertEquals("สายพานลำเลียง", machine.getName(), "trim name");
        assertEquals("Line B", machine.getLocation(), "trim location");
        assertEquals("M-002", machine.getId(), "keep id");
        assertTrue(reading == machine.getLatestReading(), "keep reading");
        assertEquals(hours, machine.getOperatingHours(), "keep hours");
        assertEquals(status, machine.getStatus(), "keep status");
        assertEquals(maintenance, machine.requiresMaintenance(), "keep maintenance");
        try {
            service.updateMachineDetails("M-002", "ไม่ควรถูกบันทึก", " ");
            throw new AssertionError("blank location must fail");
        } catch (IllegalArgumentException expected) {
            assertEquals("สายพานลำเลียง", machine.getName(), "no partial update");
            assertEquals("Line B", machine.getLocation(), "keep location");
        }
    }
```

</details>

ใน `main()` เพิ่ม `testUpdateDetails();` ก่อนบรรทัดแสดงผล แล้วแก้ข้อความจาก `PASS: 6 tests` เป็น `PASS: 7 tests`

รันคำสั่ง Test ด้านบนหลังเพิ่มโค้ดครบแล้ว

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

Console แสดง PASS: 7 tests ตรวจว่าชื่อ/ตำแหน่งเปลี่ยนได้ แต่รหัส ค่า Sensor ชั่วโมง สถานะ และเงื่อนไขบำรุงยังเหมือนเดิม รวมทั้งข้อมูลไม่เปลี่ยนครึ่งเดียวเมื่อค่าไม่ผ่าน

</details>


## ลองทำเอง

หากชื่อถูกต้องแต่ตำแหน่งว่าง ชื่อเดิมควรเปลี่ยนไหม?

<details>
<summary>เฉลย</summary>

ไม่เปลี่ยน เพราะ updateDetails ตรวจชื่อและตำแหน่งครบก่อนกำหนดค่าจริง

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/15a-core-edit/)

</details>

[สารบัญ](../ep15-edit-machine-crud.md) · [ถัดไป](02-select.md)
