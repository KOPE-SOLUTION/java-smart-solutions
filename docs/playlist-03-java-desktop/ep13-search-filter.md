# EP 3.13 — Search และ FilteredList

เป้าหมาย: ค้นหารหัสก่อน แล้วขยายเป็นชื่อและตำแหน่ง

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
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.13 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../lesson-resources/ep3-12-16-steps/README.md)

</details>

## 1. ค้นหาจากรหัส

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` ภายใน VBox `content-area` เพิ่มก่อน TableView:

```xml
<TextField fx:id="searchField" promptText="ค้นหารหัสเครื่องจักร"/>
                <Label fx:id="filterResultLabel"/>
```

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Import:

```java
import javafx.collections.transformation.FilteredList;
```

เพิ่ม Field หลัง `machines` ซึ่งเป็นรายการต้นทาง:

```java
@FXML private TextField searchField;
    @FXML private Label filterResultLabel;
    private final FilteredList<Machine> filteredMachines =
            new FilteredList<>(machines, machine -> true);
```

FilteredList เป็นมุมมองที่แสดงเฉพาะรายการผ่านเงื่อนไข ไม่ได้ลบข้อมูลออกจาก Service

ท้าย `configureTable()` แทนที่ `machineTable.setItems(machines);`:

```java
machineTable.setItems(filteredMachines);
```

เพิ่มหลัง `configureTable();` ใน `initialize()`:

```java
searchField.textProperty().addListener((observable, oldValue, newValue) -> applySearch());
```

เพิ่ม Method หลัง `refreshDashboard()`:

```java
private void applySearch() {
        String keyword = searchField.getText().trim().toLowerCase(Locale.ROOT);
        filteredMachines.setPredicate(machine ->
                machine.getId().toLowerCase(Locale.ROOT).contains(keyword));
        filterResultLabel.setText("พบ " + filteredMachines.size() + " / " + machines.size() + " เครื่อง");
    }
```

Predicate คือเงื่อนไขที่คืน true สำหรับแถวที่ต้องการแสดง

ภายใน `refreshDashboard()` เพิ่ม `applySearch();` หลัง `machines.setAll(service.getMachines());` เพื่อคำนวณผลและจำนวนอีกครั้งเมื่อข้อมูลเปลี่ยน

**ก่อนรัน:** ค้นหา M-002 แล้ว Summary ทั้งหมดควรเหลือ 1 หรือ 3?

รันด้วยคำสั่งด้านบน แล้วพิมพ์ M-002 แล้วล้างข้อความ

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ค้นหาแล้วตารางเหลือ M-002, พบ 1 / 3 เครื่อง แต่ Summary ทั้งหมด 3; ล้างแล้วกลับมา 3 แถว

</details>


## 2. ค้นหาชื่อและตำแหน่งด้วย

แทนที่ `applySearch()`:

```java
private void applySearch() {
        String keyword = searchField.getText().trim().toLowerCase(Locale.ROOT);
        filteredMachines.setPredicate(machine ->
                machine.getId().toLowerCase(Locale.ROOT).contains(keyword)
                || machine.getName().toLowerCase(Locale.ROOT).contains(keyword)
                || machine.getLocation().toLowerCase(Locale.ROOT).contains(keyword));
        filterResultLabel.setText("พบ " + filteredMachines.size() + " / " + machines.size() + " เครื่อง");
    }
```

`||` หมายถึงตรงอย่างน้อยหนึ่งช่อง ส่วน `toLowerCase` ทำให้ไม่แยกตัวพิมพ์ใหญ่/เล็ก

กลับไปที่ FXML เปลี่ยน `promptText` ของ searchField เป็น “ค้นหารหัส ชื่อ หรือตำแหน่ง”

**ก่อนรัน:** คำว่า line a น่าจะตรงกับกี่เครื่อง?

รันด้วยคำสั่งด้านบน แล้วลอง line a, utility, คำที่ไม่มีข้อมูล และล้างคำค้น

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

line a พบ 2; utility พบ M-003; คำไม่ตรงพบ 0; ล้างแล้วพบ 3

</details>


คงคำค้น `Line B` เพิ่ม M-004 / Packaging Robot / Line B แล้วตรวจว่าปรากฏทันทีและพบ 1 / 4 เครื่อง จากนั้นเลือก M-004 แล้วลบ ต้องกลับเป็น 0 / 3 เครื่อง


## ลองทำเอง

ให้ค้นหาได้จากข้อความสถานะด้วย

<details>
<summary>เฉลย</summary>

```java
|| machine.getStatus().getDisplayName().toLowerCase(Locale.ROOT).contains(keyword)
```
เพิ่มเงื่อนไขนี้ก่อนปิด Predicate ทดลองแล้วนำออกก่อน EP3.14 ซึ่งจะมีช่องเลือกสถานะแยก

</details>

<details>
<summary>โค้ดเทียบ</summary>

[จบค้นหารหัส](../../lesson-resources/ep3-12-16-steps/13a-search-id/) · [จบค้นหาหลายช่อง](../../lesson-resources/ep3-12-16-steps/13b-search-all/)

</details>

ถัดไป: [EP3.14 — หลายเงื่อนไขและการเรียง](ep14-multi-filter-sort.md)
