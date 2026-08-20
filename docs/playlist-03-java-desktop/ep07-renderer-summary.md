# EP 3.7 — Cell Renderer และ Summary Card

## เป้าหมาย

- เปลี่ยนการแสดงผล Cell โดยไม่เปลี่ยนข้อมูลจริง
- ใช้สีช่วยอ่านสถานะ
- สรุปจำนวนเครื่องจักรด้านบน Dashboard

```java
table.getColumnModel().getColumn(3)
        .setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean selected,
                    boolean focus, int row, int column) {
                Component component = super.getTableCellRendererComponent(
                        table, value, selected, focus, row, column);
                if (!selected) {
                    component.setForeground(
                            "WARNING".equals(value) ? Color.RED : new Color(31, 122, 75));
                }
                return component;
            }
        });
```

Summary Card ใช้ `JLabel` แสดงค่า Total, Running, Warning และ Maintenance แล้วอัปเดตจาก Service ทุกครั้งที่ข้อมูลเปลี่ยน

ซอร์สจริง: ดู `StatusCellRenderer` และ `buildSummaryPanel` ใน [`SmartFactoryFrame.java`](../../src/main/java/smartfactory/ui/SmartFactoryFrame.java)

## Challenge

เพิ่มสีส้มสำหรับ Maintenance และจัดข้อความสถานะให้อยู่กึ่งกลาง Cell

ถัดไป: [EP 3.8 — เชื่อม Service และ CRUD](ep08-service-crud.md)

