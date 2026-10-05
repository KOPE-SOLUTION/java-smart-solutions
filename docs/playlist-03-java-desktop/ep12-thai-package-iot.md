# EP 3.12 — ภาษาไทยและ Runtime Image

เป้าหมาย: ตรวจแอปเดิม แล้วเปิดจากชุดที่มี Java Runtime รวมอยู่ด้วย

ทำต่อจากบทก่อนใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เพิ่มโค้ดครบหนึ่งขั้นแล้วรันตรวจผลก่อนทำขั้นถัดไป เปิดแอปใหม่และยังไม่เปิด Auto เมื่อทดสอบจำนวนจากข้อมูลเริ่มต้น

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.12 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../lesson-resources/ep3-12-16-steps/README.md)

</details>

## 1. ตรวจภาษาไทยและกฎของเครื่องจักร

รันแอปก่อน เลือก M-003 แล้วกดบำรุง ต้องเห็นชั่วโมง 0 และต้องบำรุงเหลือ 1 ลองกดเพิ่มโดยเว้นข้อมูลว่าง ต้องอ่าน Alert ภาษาไทยได้

หากตัวอักษรเพี้ยน ตรวจว่าไฟล์ Java, FXML, CSS บันทึกเป็น UTF-8 หากเป็นสี่เหลี่ยมให้ตรวจ Font ไทยบนเครื่อง โดย CSS เดิมใช้ Leelawadee UI และ Tahoma เป็นตัวเลือก

นำ Test ที่ใช้กับ OOP Core เดิมกลับมารันด้วย จากโฟลเดอร์หลัก Repository:

```powershell
New-Item -ItemType Directory -Force .\practice\smart-factory-dashboard\src\test\java\smartfactory
Copy-Item .\lesson-resources\ep3-9-oop-core\tests\SmartFactoryTest.java .\practice\smart-factory-dashboard\src\test\java\smartfactory\SmartFactoryTest.java
```

ถ้ามีไฟล์ Test นี้และแก้เองไว้แล้ว ให้เทียบเนื้อหาก่อน ไม่ต้องคัดลอกทับ

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml test-compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java "-Dexec.mainClass=smartfactory.SmartFactoryTest" "-Dexec.classpathScope=test"
```

<details>
<summary>ตรวจผลขั้นที่ 1</summary>

Console แสดง `PASS: 6 tests` ส่วนหน้าต่างตรวจภาษาไทยและบำรุงรักษาได้ตามข้างต้น

</details>


## 2. เปิดแอปแบบ Module

Module ระบุว่าแอปต้องใช้ส่วนใดของ JavaFX ก่อนนำส่วนที่ใช้ไปจัดชุด Runtime

สร้าง `src/main/java/module-info.java`:

```java
module smartfactory.dashboard {
    requires javafx.controls;
    requires javafx.fxml;
    exports smartfactory.model;
    exports smartfactory.service;
    exports smartfactory.ui;
    opens smartfactory.ui to javafx.fxml;
}
```

`requires` ระบุสิ่งที่ใช้ ส่วน `opens ... to javafx.fxml` อนุญาตให้ตัวโหลดเชื่อม Field ที่มี `@FXML`

ใน `pom.xml` ภายใน Plugin `javafx-maven-plugin` เปลี่ยนบรรทัด `mainClass` และเพิ่มค่าต่อไปนี้ใน `configuration` เดียวกัน:

```xml
<mainClass>smartfactory.dashboard/smartfactory.ui.DesktopApp</mainClass>
<launcher>smart-factory</launcher>
<jlinkImageName>smart-factory</jlinkImageName>
<stripDebug>true</stripDebug>
<noHeaderFiles>true</noHeaderFiles>
<noManPages>true</noManPages>
```

ชื่อก่อน `/` คือ Module ชื่อหลังคือคลาสเริ่มแอป

**ก่อนรัน:** เปลี่ยนเป็น Module แล้วหน้าตาควรเปลี่ยนไหม?

รันด้วยคำสั่งด้านบน แล้วลองเพิ่ม M-004

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

หน้าจอและปุ่มเดิมทำงานเหมือนเดิม ทั้งหมดเพิ่มจาก 3 เป็น 4

</details>


## 3. สร้าง Runtime Image

ปิดแอป แล้วรันจากโฟลเดอร์หลัก Repository บน Windows:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml clean javafx:jlink
.\practice\smart-factory-dashboard\target\smart-factory\bin\smart-factory.bat
```

คำสั่งแรกสร้างชุด Runtime คำสั่งที่สองเปิดแอปจากชุดนั้น ไม่ใช่ Installer .exe และไม่ใช่ชุดเดียวสำหรับทุกระบบปฏิบัติการ

<details>
<summary>ตรวจผลขั้นที่ 3</summary>

แอปเปิดได้โดยไม่เรียก Maven; ต้องส่งทั้งโฟลเดอร์ `target/smart-factory` ไม่ใช่เฉพาะไฟล์ .bat ทดลองเพิ่ม/ลบ/บำรุง แล้วปิดแอป ส่วนการทดสอบบนอีกเครื่องต้องใช้ระบบและสถาปัตยกรรมที่ตรงกับชุดที่สร้าง

</details>


## ลองทำเอง

เพราะเหตุใดจึงส่งให้เพื่อนเฉพาะ smart-factory.bat ไม่ได้?

<details>
<summary>เฉลย</summary>

ไฟล์นี้เป็นตัวเปิดแอป ต้องใช้ Runtime และ Module ที่อยู่ในโฟลเดอร์เดียวกันด้วย

</details>

<details>
<summary>ต่อยอด IoT ภายหลัง</summary>

เริ่มจากรับข้อมูลหนึ่งรายการด้วย Simulator → ส่งให้ Service → ตรวจผลบน Dashboard ก่อนเชื่อมอุปกรณ์จริง ดู[แนวทาง Integration](../FUTURE_ROADMAP.md#ขอบเขต-integration-และการใช้งานจริง) ถ้ายังไม่มีอุปกรณ์ไม่ต้องทำส่วนนี้เพื่อผ่าน EP3.12

</details>

<details>
<summary>โค้ดเทียบและอ้างอิง</summary>

[จบขั้น Test](../../lesson-resources/ep3-12-16-steps/12a-tests/) · [จบขั้น Module](../../lesson-resources/ep3-12-16-steps/12b-module/) · [JavaFX Maven Plugin](https://github.com/openjfx/javafx-maven-plugin)

</details>

ถัดไป: [EP3.13 — Search](ep13-search-filter.md)
