# EP3.11 — ขั้นที่ 1: เปิดหน้าต่าง FXML

เป้าหมาย: เปิดหน้าต่างได้

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-1 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository หลังเพิ่มโค้ดครบขั้น:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

## 1. เพิ่มตัวอ่าน FXML

ใน `pom.xml` เพิ่ม Dependency นี้ต่อจาก `javafx-controls` ภายใน `dependencies`:

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>${javafx.version}</version>
</dependency>
```

FXML เป็นไฟล์หน้าจอ ส่วน `javafx-fxml` อ่านไฟล์นั้นให้กลายเป็น Object ของ JavaFX

## 2. สร้างหน้าจอเล็ก

สร้าง `src/main/resources/smartfactory/ui/dashboard-view.fxml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<BorderPane xmlns:fx="http://javafx.com/fxml/1">
    <padding><Insets top="20" right="20" bottom="20" left="20"/></padding>
    <center><Label text="หน้าจอจาก FXML"/></center>
</BorderPane>
```

`import` บอกชนิด Control ที่ใช้ ส่วน `center` คือพื้นที่กลางของ BorderPane ที่เคยเรียนแล้ว

## 3. เปิดหน้าจอนี้

สร้าง `src/main/java/smartfactory/ui/DesktopApp.java`:

```java
package smartfactory.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesktopApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);
        stage.setTitle("KOPE SOLUTION - Smart Factory Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

`loader.load()` อ่าน FXML แล้วส่งหน้าจอให้ Scene

กลับไปที่ `pom.xml` เปลี่ยนเฉพาะ `mainClass` ใน Plugin `javafx-maven-plugin`:

```xml
<mainClass>smartfactory.ui.DesktopApp</mainClass>
```

ไฟล์ Dashboard เดิมยังอยู่ แต่คำสั่งรันจะเปิดหน้าจอใหม่นี้

**ก่อนรัน:** ข้อความกลางหน้าต่างมาจากไฟล์ไหน?

รันด้วยคำสั่งด้านบน แล้วเปิดหน้าต่าง

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

เห็น “หน้าจอจาก FXML” หน้าต่างยังไม่มีตาราง ซึ่งจะนำกลับมาในขั้นที่ 3

</details>


## ลองทำเอง

เปลี่ยนข้อความเป็น “Smart Factory Dashboard” แล้วรันอีกครั้ง

<details>
<summary>เฉลย</summary>

แก้เฉพาะค่า `text` ของ Label ใน FXML ไม่ต้องแก้ DesktopApp

</details>



<details>
<summary>ดูโค้ดครบขั้นเพื่อเทียบ</summary>

[ชุดจบขั้นที่ 1](../../../lesson-resources/ep3-11-steps/01-window/) เป็นผลจากขั้นนี้ ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](../ep10d-auto-sensor-timeline.md) · [สารบัญ EP3.11](../ep11-fxml-controller.md) · [ถัดไป](02-button.md)
