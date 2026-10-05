# EP3.14 — ขั้นที่ 1: กรองสถานะ

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.14-1 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## เพิ่มช่องสถานะ

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มก่อน Label `filterResultLabel`:

```xml
<ComboBox fx:id="statusFilter" promptText="สถานะ"/>
```

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Import `javafx.scene.control.ComboBox` แล้วเพิ่ม Field หลัง `filteredMachines`:

```java
@FXML private ComboBox<String> statusFilter;
```

เพิ่ม Method ต่อจาก `applySearch()`:

```java
private void configureStatusFilter() {
        statusFilter.getItems().add("ทุกสถานะ");
        for (MachineStatus status : MachineStatus.values()) {
            statusFilter.getItems().add(status.getDisplayName());
        }
        statusFilter.setValue("ทุกสถานะ");
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> applySearch());
    }
```

ComboBox คือช่องเลือกค่า ส่วนลูปนำสถานะที่มีใน Enum มาใส่เป็นตัวเลือก

เพิ่ม `configureStatusFilter();` ก่อน `configureTable();` ใน `initialize()` แล้วแทนที่ `applySearch()`:

```java
private void applySearch() {
        String keyword = searchField.getText().trim().toLowerCase(Locale.ROOT);
        filteredMachines.setPredicate(machine -> {
            boolean matchesText = machine.getId().toLowerCase(Locale.ROOT).contains(keyword)
                    || machine.getName().toLowerCase(Locale.ROOT).contains(keyword)
                    || machine.getLocation().toLowerCase(Locale.ROOT).contains(keyword);
            boolean matchesStatus = "ทุกสถานะ".equals(statusFilter.getValue())
                    || machine.getStatus().getDisplayName().equals(statusFilter.getValue());
            return matchesText && matchesStatus;
        });
        filterResultLabel.setText("พบ " + filteredMachines.size() + " / " + machines.size() + " เครื่อง");
    }
```

ภายในกลุ่มข้อความใช้ `||` แต่ระหว่างข้อความกับสถานะใช้ `&&` เพราะต้องผ่านทั้งสองเงื่อนไข

**ก่อนรัน:** เลือก Sensor ผิดปกติ และค้นหา utility จะเหลือแถวไหม?

รันด้วยคำสั่งด้านบน แล้วลองเลือกสถานะอย่างเดียว แล้วเพิ่มคำค้น utility

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

สถานะอย่างเดียวพบ M-002; เพิ่ม utility แล้วไม่พบ เพราะ M-003 ไม่ได้มี Sensor ผิดปกติ

</details>


## ลองทำเอง

ต้องการเห็นทั้งสามเครื่องอีกครั้งทำอย่างไร?

<details>
<summary>เฉลย</summary>

ล้างข้อความและเลือก “ทุกสถานะ”

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/14a-status/)

</details>

[สารบัญ](../ep14-multi-filter-sort.md) · [ถัดไป](02-maintenance.md)
