# EP 3.16 — Scene Builder (Optional)

เป้าหมาย: ปรับระยะและขนาดฟอร์มเดิม โดยไม่แก้กฎของเครื่องจักร

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
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.16 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../lesson-resources/ep3-12-16-steps/README.md)

</details>

Scene Builder ช่วยจัด FXML ด้วยเมาส์ ส่วน Controller และ Service ยังใช้ชุดเดิม ข้ามบทนี้ได้หากต้องการเขียน FXML ต่อด้วยมือ

## 1. เปิดฟอร์มเดิมแล้วขยับระยะ

ติดตั้งจาก[เว็บไซต์ Gluon](https://gluonhq.com/products/scene-builder/) แล้วเปิด `src/main/resources/smartfactory/ui/dashboard-view.fxml` ของโปรเจกต์ที่ทำต่อเนื่อง

ใน Hierarchy เลือก `BorderPane → center → SplitPane → GridPane` ที่มี fx:id เป็น `machineForm`

เปลี่ยน Hgap เป็น `12` และ Vgap เป็น `14` ใน Layout จากนั้น Preview แล้วบันทึก

เปิด FXML ดูเฉพาะค่าที่เปลี่ยน:

```xml
<GridPane fx:id="machineForm" hgap="12" vgap="14">
```


**ก่อนรัน:** ปรับระยะแล้วปุ่มบันทึกควรเปลี่ยนหน้าที่ไหม?

รันด้วยคำสั่งด้านบน แล้วเปิดแอป เลือก M-002 แล้วกดยกเลิก

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ช่องต่าง ๆ ห่างขึ้น การเลือกและยกเลิกยังทำงานเหมือนเดิม

</details>


## 2. ให้ช่องกรอกขยายตามพื้นที่

เลือก GridPane เดิม ตั้ง Column Constraints:

- คอลัมน์ 0: Min Width `90`
- คอลัมน์ 1: Hgrow `ALWAYS`

เลือก TextField ทั้งสาม ตั้ง Max Width เป็น `Infinity` และ GridPane Hgrow เป็น `ALWAYS` บันทึกแล้วตรวจค่าที่เทียบเท่า:

```xml
<columnConstraints>
    <ColumnConstraints minWidth="90"/>
    <ColumnConstraints hgrow="ALWAYS"/>
</columnConstraints>
```

บล็อกด้านบนอยู่ภายใน GridPane; TextField แต่ละตัวต้องมี `maxWidth="Infinity"` และ `GridPane.hgrow="ALWAYS"` โดยคง fx:id และตำแหน่งแถวเดิม

**ก่อนรัน:** อะไรจะกว้างขึ้นเมื่อเลื่อนเส้นแบ่งตารางกับฟอร์ม?

รันด้วยคำสั่งด้านบน แล้วเลื่อนเส้นแบ่งให้ฟอร์มมีพื้นที่เพิ่ม แล้วลองแก้ชื่อและบันทึก

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ช่องกรอกขยายตามคอลัมน์ ไม่ทับ Label และบันทึกชื่อได้เหมือนเดิม

</details>


<details>
<summary>ถ้า Preview ได้ แต่แอปกดปุ่มไม่ได้</summary>

Preview ใช้ตรวจหน้าตา ไม่ใช่ผลการทำงาน ตรวจว่า fx:controller ยังเป็น smartfactory.ui.DashboardController และ fx:id/onAction ของ Control เดิมไม่ถูกลบ แล้วทดสอบด้วยคำสั่งรันแอปอีกครั้ง

</details>


## ลองทำเอง

เปลี่ยนระยะระหว่างปุ่มเป็น 12 โดยไม่แก้ Java

<details>
<summary>เฉลย</summary>

เลือก VBox ที่มี fx:id เป็น actionButtons แล้วตั้ง Spacing เป็น 12 หรือแก้ `spacing="12"` ใน FXML

</details>

<details>
<summary>โค้ดเทียบ</summary>

[จบขั้นระยะห่าง](../../lesson-resources/ep3-12-16-steps/16a-spacing/) · [จบขั้นขยายช่อง](../../lesson-resources/ep3-12-16-steps/16b-layout/)

</details>

กลับไปที่ [สารบัญ Playlist](README.md)
