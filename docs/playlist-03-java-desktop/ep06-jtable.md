# EP 3.6 — JTable และ TableModel

## เป้าหมาย

- สร้างตารางจาก `DefaultTableModel`
- ปิดการแก้ไขข้อมูลตรง Cell
- วาง Form และ Table ในหน้าต่างเดียวกัน
- เพิ่มและล้างแถวข้อมูล

ทำงานต่อใน `FirstWindow.java` โดยยังใช้ Form จาก EP3.3 และ Dialog จาก EP3.5

## 1. เพิ่ม Import

วางรวมกับ Import เดิมเหนือ Class:

```java
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
```

## 2. กำหนดคอลัมน์และ TableModel

วางภายใน `createWindow()` หลังสร้าง `form`:

```java
String[] columns = {"รหัส", "ชื่อ", "ตำแหน่ง", "สถานะ"};

DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};
```

- เลข `0` หมายถึงเริ่มต้นโดยยังไม่มีแถว
- Override `isCellEditable(...)` เพื่อป้องกันผู้ใช้แก้ Business Data ตรง Cell

## 3. สร้าง JTable และ JScrollPane

วางต่อจาก `tableModel`:

```java
JTable table = new JTable(tableModel);
table.setRowHeight(30);

JScrollPane scrollPane = new JScrollPane(table);
```

ต้องนำ `scrollPane` ไปแสดงแทน `table` โดยตรง เพื่อให้มีหัวตารางและ Scrollbar

## 4. จัด Form และ Table ให้อยู่ด้วยกัน

ลบบรรทัดเดิมจาก EP3.3:

```java
content.add(form, BorderLayout.CENTER);
```

แล้ววางโค้ดนี้แทนหลังสร้าง `scrollPane`:

```java
JPanel workspace = new JPanel(new BorderLayout(10, 10));
workspace.add(form, BorderLayout.NORTH);
workspace.add(scrollPane, BorderLayout.CENTER);
content.add(workspace, BorderLayout.CENTER);
```

ตอนนี้ Summary อยู่ด้านบนของ `content` ส่วน Form และ Table อยู่ใน `workspace`

## 5. เพิ่มข้อมูลทดลองหนึ่งแถว

วางหลังสร้าง `tableModel` และก่อน `frame.setVisible(true);`:

```java
tableModel.addRow(
        new Object[]{"M-001", "เครื่องผสม", "Line A", "RUNNING"}
);
```

จำนวนค่าใน `Object[]` ต้องเท่ากับจำนวนคอลัมน์

## 6. ทดลองล้างและเพิ่มข้อมูลใหม่

คำสั่งล้างทุกแถวคือ:

```java
tableModel.setRowCount(0);
```

ยังไม่ต้องใส่บรรทัดนี้ถาวรใน `createWindow()` เพราะแถวทดลองจะหาย เราจะนำไปใช้ใน Method Refresh ตั้งแต่ EP3.7

## 7. Compile และ Run

```powershell
javac -encoding UTF-8 FirstWindow.java
java -Dfile.encoding=UTF-8 FirstWindow
```

ผลที่ต้องเห็น:

- Form อยู่เหนือ Table
- Table มีหัวคอลัมน์ครบ
- มีข้อมูล M-001 หนึ่งแถว
- Double-click ที่ Cell แล้วแก้ค่าไม่ได้

## จุดที่มักสับสน

- `DefaultTableModel` เก็บข้อมูล ส่วน `JTable` แสดงข้อมูล
- เพิ่ม `scrollPane` ลง Panel ไม่ใช่เพิ่ม `table` ซ้ำ
- อย่าวางทั้ง `form` และ `workspace` ใน `BorderLayout.CENTER` พร้อมกัน
- Column Index เริ่มจาก `0` ดังนั้นคอลัมน์สถานะคือ Index `3`

## Challenge

เพิ่มคอลัมน์อุณหภูมิและแรงสั่น:

```java
String[] columns = {
        "รหัส", "ชื่อ", "ตำแหน่ง", "สถานะ",
        "อุณหภูมิ (°C)", "การสั่น (mm/s)"
};
```

จากนั้นเพิ่มค่า `65.5` และ `3.1` ใน `Object[]` ของแถวทดลองให้ครบหกคอลัมน์

ถัดไป: [EP 3.7 — Renderer และ Summary Card](ep07-renderer-summary.md)
