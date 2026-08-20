# EP 3.6 — JTable และ TableModel

## เป้าหมาย

- แยก View ของตารางออกจากข้อมูลใน TableModel
- เพิ่มและล้างแถว
- ปิดการแก้ข้อมูลตรง Cell

```java
String[] columns = {"รหัส", "ชื่อ", "ตำแหน่ง", "สถานะ"};

DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};

JTable table = new JTable(tableModel);
table.setRowHeight(30);
JScrollPane scrollPane = new JScrollPane(table);
```

เพิ่มแถว:

```java
tableModel.addRow(new Object[]{
        "M-001", "เครื่องผสม", "Line A", "RUNNING"
});
```

ล้างข้อมูลก่อน refresh:

```java
tableModel.setRowCount(0);
```

## Challenge

เพิ่มคอลัมน์อุณหภูมิ แรงสั่น ชั่วโมงทำงาน และควรบำรุงรักษา

ถัดไป: [EP 3.7 — Renderer และ Summary Card](ep07-renderer-summary.md)

