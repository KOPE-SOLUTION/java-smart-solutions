# EP 3.6 — JTable และ TableModel

## เป้าหมาย

- สร้างตารางจาก TableModel
- เพิ่มข้อมูลหนึ่งแถว
- ปิดการแก้ไขข้อมูลตรง Cell

## 1. กำหนดคอลัมน์

```java
String[] columns = {"รหัส", "ชื่อ", "ตำแหน่ง", "สถานะ"};
DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
```

เลข `0` หมายถึงเริ่มต้นโดยยังไม่มีแถวข้อมูล

## 2. สร้าง JTable

```java
JTable table = new JTable(tableModel);
table.setRowHeight(30);

JScrollPane scrollPane = new JScrollPane(table);
```

นำ `scrollPane` ไปเพิ่มใน Panel แทนการเพิ่ม `table` โดยตรง เพื่อให้มีหัวตารางและ Scrollbar

## 3. เพิ่มข้อมูลหนึ่งแถว

```java
tableModel.addRow(new Object[]{"M-001", "เครื่องผสม", "Line A", "RUNNING"});
```

## 4. ปิดการแก้ Cell

เปลี่ยนการสร้าง `tableModel` เป็น:

```java
DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};
```

เมื่อต้อง Refresh ตาราง ให้ล้างแถวเดิมก่อน:

```java
tableModel.setRowCount(0);
```

## Challenge

เพิ่มคอลัมน์อุณหภูมิและแรงสั่น แล้วเพิ่มค่าของทั้งสองคอลัมน์ใน `addRow(...)`

ถัดไป: [EP 3.7 — Renderer และ Summary Card](ep07-renderer-summary.md)
