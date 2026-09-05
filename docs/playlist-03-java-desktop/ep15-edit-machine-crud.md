# EP 3.15 — แก้ไขข้อมูลเครื่องจักรและ Complete CRUD

## สิ่งที่จะทำ

- เลือกเครื่องจักรแล้วนำข้อมูลเข้าสู่ Form
- แก้ไขชื่อและตำแหน่งของเครื่องจักร
- ล็อกรหัสเครื่องจักรระหว่างแก้ไข
- ยกเลิกการแก้ไขและกลับสู่โหมดเพิ่ม
- ตรวจว่าข้อมูล Sensor และ Summary ไม่เปลี่ยนตาม

```mermaid
flowchart LR
    T[เลือกแถวใน TableView] --> F[เติมข้อมูลใน Form]
    F --> C[DashboardController]
    C --> S[SmartFactoryService]
    S --> M[FactoryDevice.updateDetails]
    M --> R[Refresh Table และ Filter]
```

หลังจบ EP นี้ CRUD จะครบทั้ง Create, Read, Update และ Delete

EP นี้แก้ Model, Service, Test, FXML, Controller และ CSS ภายใน `practice/smart-factory-dashboard` โดยแต่ละขั้นระบุไฟล์ให้ตรงกับชั้นที่รับผิดชอบ

## 1. เพิ่มพฤติกรรมแก้ไขใน Model

เปิด `practice/smart-factory-dashboard/src/main/java/smartfactory/model/FactoryDevice.java` แล้วเพิ่ม Method นี้ก่อน `requireText(...)`:

```java
public void updateDetails(String name, String location) {
    String validatedName = requireText(name, "name");
    String validatedLocation = requireText(location, "location");
    this.name = validatedName;
    this.location = validatedLocation;
}
```

ตรวจค่าทั้งสองให้ผ่านก่อนแก้ Field เพื่อป้องกัน Object อยู่ในสภาพที่ชื่อเปลี่ยนแล้วแต่ตำแหน่งไม่ผ่าน Validation

ไม่เพิ่ม Setter สำหรับ `id` เพราะรหัสใช้ระบุตัวเครื่องจักรและควรคงเดิมตลอดอายุของ Object

## 2. เพิ่ม Update ใน Service

เปิด `practice/smart-factory-dashboard/src/main/java/smartfactory/service/SmartFactoryService.java` แล้วเพิ่ม Method นี้ต่อจาก `findRequired(...)` และก่อน `updateSensor(...)`:

```java
public void updateMachineDetails(String id, String name, String location) {
    findRequired(id).updateDetails(name, location);
}
```

Controller จึงไม่ต้องค้นหาและแก้ Object เอง และช่องทางอื่นในอนาคต เช่น REST API สามารถใช้ Method เดียวกันได้

## 3. เพิ่ม Test ก่อนทำหน้าจอ

เปิด `practice/smart-factory-dashboard/src/test/java/smartfactory/SmartFactoryTest.java` ที่เตรียมใน [EP3.12](ep12-thai-package-iot.md#4-เตรียมและรัน-test) แล้วเพิ่มใน `main()`:

```java
testMachineDetailsCanBeUpdated();
```

เพิ่ม Test Method นี้ภายใน Class โดยวางก่อนกลุ่ม Method `assert...` ด้านท้ายไฟล์:

```java
private static void testMachineDetailsCanBeUpdated() {
    SmartFactoryService service = SmartFactoryService.createWithSampleData();
    Machine conveyor = service.findRequired("M-002");
    SensorReading readingBeforeEdit = conveyor.getLatestReading();

    service.updateMachineDetails(
            "M-002",
            " สายพานลำเลียง ",
            " Packing Line "
    );

    assertEquals("M-002", conveyor.getId(), "machine id remains unchanged");
    assertEquals("สายพานลำเลียง", conveyor.getName(), "updated machine name");
    assertEquals("Packing Line", conveyor.getLocation(), "updated machine location");
    assertEquals(readingBeforeEdit, conveyor.getLatestReading(), "sensor reading remains unchanged");
}
```

เปลี่ยนข้อความสรุปท้าย Test เป็น:

```java
System.out.println("PASS: 7 tests");
```

รัน Test ของโปรเจกต์ที่ทำตามคลิปโดยตรง:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml test-compile `
    org.codehaus.mojo:exec-maven-plugin:3.5.0:java `
    "-Dexec.mainClass=smartfactory.SmartFactoryTest" `
    "-Dexec.classpathScope=test"
```

ผลที่ต้องเห็นคือ `PASS: 7 tests` ก่อนเริ่มแก้ FXML และ Controller

## 4. เพิ่มปุ่มของโหมดแก้ไขใน FXML

เปิด `practice/smart-factory-dashboard/src/main/resources/smartfactory/ui/dashboard-view.fxml` หา Label `จัดการ` และปุ่ม `เพิ่มเครื่องจักร` ใน `GridPane` แล้วแทนที่ทั้งสอง Component ด้วย:

```xml
<Label fx:id="formModeLabel" text="จัดการข้อมูล"
       GridPane.columnIndex="3" GridPane.rowIndex="0"/>
<HBox spacing="6" GridPane.columnIndex="3" GridPane.rowIndex="1">
    <Button fx:id="addMachineButton" text="เพิ่ม"
            onAction="#handleAddMachine" styleClass="button-primary"
            maxWidth="Infinity" HBox.hgrow="ALWAYS"/>
    <Button fx:id="editMachineButton" text="บันทึกแก้ไข"
            onAction="#handleUpdateMachine" styleClass="button-success"
            disable="true" maxWidth="Infinity" HBox.hgrow="ALWAYS"/>
    <Button fx:id="cancelEditButton" text="ยกเลิก"
            onAction="#handleCancelEdit" disable="true"
            maxWidth="Infinity" HBox.hgrow="ALWAYS"/>
</HBox>
```

เพิ่มสีของปุ่มบันทึกต่อท้าย `practice/smart-factory-dashboard/src/main/resources/smartfactory/ui/smart-factory.css`:

```css
.button-success {
    -fx-background-color: #148a5a;
}
```

## 5. เชื่อม Field และการเลือกแถว

เปิด `practice/smart-factory-dashboard/src/main/java/smartfactory/ui/DashboardController.java` แล้วเพิ่ม Field เหล่านี้ไว้กับกลุ่ม Field `@FXML` เดิม:

```java
@FXML private Label formModeLabel;
@FXML private Button addMachineButton;
@FXML private Button editMachineButton;
@FXML private Button cancelEditButton;
```

ใน `initialize()` แทนที่ Listener ของ TableView เดิมซึ่งเรียก `fillSensorFields(selected)` ด้วย:

```java
machineTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, previous, selected) -> handleMachineSelection(selected)
);
```

เพิ่ม Method สำหรับสลับ Form เข้าสู่โหมดแก้ไขต่อจาก `initialize()`:

```java
private void handleMachineSelection(Machine machine) {
    fillSensorFields(machine);
    if (machine == null) {
        resetMachineForm();
        return;
    }

    idField.setText(machine.getId());
    nameField.setText(machine.getName());
    locationField.setText(machine.getLocation());
    idField.setEditable(false);
    formModeLabel.setText("กำลังแก้ไข " + machine.getId());
    addMachineButton.setDisable(true);
    editMachineButton.setDisable(false);
    cancelEditButton.setDisable(false);
}
```

เมื่อเลือกแถว รหัสจะอ่านได้แต่แก้ไม่ได้ ส่วนชื่อและตำแหน่งยังแก้ไขได้ตามปกติ

## 6. บันทึกและยกเลิกการแก้ไข

เพิ่ม Event บันทึกไว้กับกลุ่ม Event Handler ใน `DashboardController` โดยวางต่อจาก `handleAddMachine()`:

```java
@FXML
private void handleUpdateMachine() {
    runUiAction(() -> {
        Machine selected = requireSelectedMachine();
        String name = requireText(nameField, "กรุณากรอกชื่อเครื่องจักร");
        String location = requireText(locationField, "กรุณากรอกตำแหน่ง");
        service.updateMachineDetails(selected.getId(), name, location);
        refreshDashboard();
        machineTable.getSelectionModel().clearSelection();
        resetMachineForm();
        showStatus("แก้ไขข้อมูล " + selected.getId() + " แล้ว");
    });
}
```

เพิ่ม Event ยกเลิกต่อจาก `handleUpdateMachine()`:

```java
@FXML
private void handleCancelEdit() {
    machineTable.getSelectionModel().clearSelection();
    resetMachineForm();
    showStatus("ยกเลิกการแก้ไขแล้ว");
}
```

แทนที่ Method `clearMachineForm()` เดิมทั้ง Method ด้วย:

```java
private void resetMachineForm() {
    idField.clear();
    nameField.clear();
    locationField.clear();
    idField.setEditable(true);
    formModeLabel.setText("จัดการข้อมูล");
    addMachineButton.setDisable(false);
    editMachineButton.setDisable(true);
    cancelEditButton.setDisable(true);
}
```

ใน `handleAddMachine()` แทนที่บรรทัด `clearMachineForm();` ด้วย `resetMachineForm();`

## 7. รันและตรวจผล

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ทดลองตามลำดับ:

1. เลือก `M-002` จากตาราง
2. ตรวจว่ารหัส `M-002` แก้ไม่ได้
3. เปลี่ยนชื่อ `สายพาน` เป็น `สายพานลำเลียง`
4. เปลี่ยนตำแหน่งเป็น `Packing Line`
5. กด `บันทึกแก้ไข`
6. ค้นหา `ลำเลียง` ต้องพบ `M-002`
7. ค่า Sensor, สถานะ และจำนวนต้องบำรุงต้องเท่าเดิม
8. เลือกเครื่องอีกครั้งแล้วกด `ยกเลิก` Form ต้องกลับสู่โหมดเพิ่ม

## Challenge

แสดงข้อความก่อนและหลังแก้ไขใน Status Bar เช่น `สายพาน -> สายพานลำเลียง` โดยไม่ย้าย Business Logic มาไว้ใน FXML

ถัดไป: [EP 3.16 Optional — Scene Builder Workflow](ep16-scene-builder-optional.md)

ย้อนกลับ: [EP 3.14 — กรองหลายเงื่อนไขและเรียงข้อมูล](ep14-multi-filter-sort.md)
