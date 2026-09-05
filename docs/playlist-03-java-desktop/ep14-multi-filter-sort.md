# EP 3.14 — กรองหลายเงื่อนไขและเรียงข้อมูล

## สิ่งที่จะทำ

- กรองตามสถานะของเครื่องจักร
- กรองตามกำหนดบำรุงรักษา
- ใช้ตัวกรองร่วมกับข้อความค้นหา
- ล้างทุกเงื่อนไขด้วยปุ่มเดียว
- เรียงผลลัพธ์ด้วยการกดหัวตาราง

EP นี้แก้ `dashboard-view.fxml`, `DashboardController.java` และ `smart-factory.css` ภายใน `practice/smart-factory-dashboard/src/main`

```mermaid
flowchart LR
    O[ObservableList] --> F[FilteredList]
    F --> S[SortedList]
    S --> T[TableView]
    Q[ข้อความค้นหา] --> P[Predicate แบบ AND]
    ST[สถานะ] --> P
    M[การบำรุง] --> P
    P --> F
```

## 1. เพิ่ม ComboBox และปุ่มล้าง

เพิ่ม Import ด้านบน `dashboard-view.fxml`:

```xml
<?import javafx.scene.control.ComboBox?>
```

ใน `filter-row` ของ EP 3.13 เพิ่ม Component ต่อจากช่องค้นหาและก่อน `filterResultLabel`:

```xml
<ComboBox fx:id="statusFilter" promptText="ทุกสถานะ" prefWidth="180"/>
<ComboBox fx:id="maintenanceFilter" promptText="ทุกเงื่อนไขบำรุงรักษา" prefWidth="210"/>
<Button text="ล้างตัวกรอง" onAction="#handleClearFilters"/>
```

## 2. เตรียมค่าของตัวกรอง

เพิ่ม Import ใน `DashboardController.java`:

```java
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
```

เพิ่ม Constant ใต้บรรทัดประกาศ Class:

```java
private static final String ALL_STATUSES = "ทุกสถานะ";
private static final String ALL_MAINTENANCE = "ทุกเงื่อนไขบำรุงรักษา";
private static final String MAINTENANCE_REQUIRED = "ต้องบำรุง";
private static final String MAINTENANCE_NORMAL = "ปกติ";
```

เพิ่ม `sortedMachines` ภายใน Class ต่อจาก `filteredMachines` และเพิ่ม Field `@FXML` ทั้งสองไว้กับกลุ่ม Field ของ Form:

```java
private final SortedList<Machine> sortedMachines =
        new SortedList<>(filteredMachines);

@FXML private ComboBox<String> statusFilter;
@FXML private ComboBox<String> maintenanceFilter;
```

## 3. ตั้งค่าตัวเลือกและ Listener

เพิ่ม Method นี้ภายใน `DashboardController` โดยวางต่อจาก `initialize()`:

```java
private void configureFilters() {
    statusFilter.getItems().add(ALL_STATUSES);
    for (MachineStatus status : MachineStatus.values()) {
        statusFilter.getItems().add(status.getDisplayName());
    }
    statusFilter.setValue(ALL_STATUSES);

    maintenanceFilter.getItems().setAll(
            ALL_MAINTENANCE,
            MAINTENANCE_REQUIRED,
            MAINTENANCE_NORMAL
    );
    maintenanceFilter.setValue(ALL_MAINTENANCE);

    searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    maintenanceFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
}
```

ใน `initialize()` เพิ่มคำสั่งนี้ต่อจาก `configureTable();`:

```java
configureFilters();
```

จากนั้นแทนที่ `machineTable.setItems(filteredMachines);` ด้วย:

```java
sortedMachines.comparatorProperty().bind(machineTable.comparatorProperty());
machineTable.setItems(sortedMachines);
```

ลบเฉพาะ Listener ที่เรียก `applySearch()` จาก EP 3.13 เพราะ `configureFilters()` สร้าง Listener ชุดใหม่ให้แล้ว ส่วนคำสั่งอื่นใน `initialize()` ให้เก็บไว้ตามเดิม

## 4. รวมเงื่อนไขใน Predicate เดียว

เปลี่ยน `applySearch()` เป็น `applyFilters()`:

```java
private void applyFilters() {
    String keyword = normalize(searchField.getText());
    String selectedStatus = statusFilter.getValue();
    String selectedMaintenance = maintenanceFilter.getValue();

    filteredMachines.setPredicate(machine -> {
        boolean matchesKeyword = keyword.isBlank()
                || normalize(machine.getId()).contains(keyword)
                || normalize(machine.getName()).contains(keyword)
                || normalize(machine.getLocation()).contains(keyword);
        boolean matchesStatus = selectedStatus == null
                || ALL_STATUSES.equals(selectedStatus)
                || machine.getStatus().getDisplayName().equals(selectedStatus);
        boolean matchesMaintenance = switch (selectedMaintenance == null
                ? ALL_MAINTENANCE
                : selectedMaintenance) {
            case MAINTENANCE_REQUIRED -> machine.requiresMaintenance();
            case MAINTENANCE_NORMAL -> !machine.requiresMaintenance();
            default -> true;
        };
        return matchesKeyword && matchesStatus && matchesMaintenance;
    });

    filterResultLabel.setText(
            "แสดง " + filteredMachines.size() + " จาก " + machineItems.size() + " เครื่อง"
    );
}
```

เงื่อนไขใช้ `&&` จึงทำงานแบบ `AND` เครื่องจักรต้องผ่านทุกเงื่อนไขที่เลือกจึงจะแสดงในตาราง

การกรองบำรุงรักษาเรียก `requiresMaintenance()` จาก Model โดยตรง จึงไม่ต้องเขียนกฎ 500 ชั่วโมงซ้ำใน Controller

ใน `refreshDashboard()` เปลี่ยน `applySearch()` เป็น:

```java
applyFilters();
```

## 5. เพิ่ม Event ล้างตัวกรอง

เพิ่ม Method นี้ภายใน `DashboardController` ไว้กับกลุ่ม Event Handler อื่น ปุ่มใน FXML จะเรียกผ่าน `onAction="#handleClearFilters"`:

```java
@FXML
private void handleClearFilters() {
    searchField.clear();
    statusFilter.setValue(ALL_STATUSES);
    maintenanceFilter.setValue(ALL_MAINTENANCE);
    applyFilters();
    showStatus("ล้างการค้นหาและตัวกรองแล้ว");
}
```

## 6. เพิ่ม CSS ให้ ComboBox

เพิ่มชุดนี้ต่อท้ายไฟล์ `smart-factory.css`:

```css
.combo-box {
    -fx-background-color: #f7fbff;
    -fx-background-radius: 6;
}

.combo-box .list-cell {
    -fx-background-color: #f7fbff;
    -fx-text-fill: #102033;
}

.combo-box-popup .list-cell:hover,
.combo-box-popup .list-cell:selected {
    -fx-background-color: #dbeafe;
}
```

## 7. รันและตรวจผล

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ทดลองตามลำดับ:

1. เลือกสถานะ `Sensor ผิดปกติ` ต้องเหลือ `M-002`
2. ล้างตัวกรอง แล้วเลือก `ต้องบำรุง` ต้องพบ `M-002` และ `M-003`
3. ค้นหา `utility` ร่วมกับ `ต้องบำรุง` ต้องเหลือ `M-003`
4. เลือก `ปกติ` ในตัวกรองบำรุงรักษา ต้องเหลือ `M-001`
5. กดหัวคอลัมน์รหัสหรือชั่วโมงเพื่อเรียงข้อมูล
6. กด `ล้างตัวกรอง` ต้องกลับมาแสดง `3 จาก 3 เครื่อง`

Summary Card ยังคงนับเครื่องจักรทั้งหมด ส่วนข้อความ `แสดง x จาก y เครื่อง` เป็นจำนวนผลลัพธ์ในตาราง

## Challenge

เพิ่มตัวเลือก `ชั่วโมงตั้งแต่ 500` โดยตรวจจาก `getOperatingHours()` แยกจากกฎ `requiresMaintenance()` แล้วอธิบายว่าตัวกรองสองแบบให้ผลต่างกันเมื่อ Sensor ผิดปกติแต่ชั่วโมงยังไม่ถึง 500

ถัดไป: [EP 3.15 — แก้ไขข้อมูลเครื่องจักรและ Complete CRUD](ep15-edit-machine-crud.md)

ย้อนกลับ: [EP 3.13 — ค้นหาแบบทันทีด้วย FilteredList](ep13-search-filter.md)
