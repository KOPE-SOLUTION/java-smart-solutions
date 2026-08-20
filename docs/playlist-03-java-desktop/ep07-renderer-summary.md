# EP 3.7 — Cell Renderer และ Summary Card

## เป้าหมาย

- จัดข้อความสถานะให้อยู่กึ่งกลาง
- เปลี่ยนสีตัวอักษรตามสถานะ
- อัปเดตตัวเลขสรุปด้านบน Dashboard

## 1. จัดข้อความกึ่งกลาง

สร้าง Renderer แบบพื้นฐาน:

```java
DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
statusRenderer.setHorizontalAlignment(SwingConstants.CENTER);
```

กำหนดให้คอลัมน์สถานะ ซึ่งอยู่ที่ Index 3:

```java
table.getColumnModel().getColumn(3).setCellRenderer(statusRenderer);
```

รันดูผลส่วนนี้ก่อนเพิ่มสี

## 2. เปลี่ยนสีตามสถานะ

แทนที่ Renderer เดิมด้วย:

```java
DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean selected,
            boolean focus, int row, int column) {

        Component component = super.getTableCellRendererComponent(
                table, value, selected, focus, row, column);

        if (!selected) {
            boolean warning = "WARNING".equals(value);
            component.setForeground(warning ? Color.RED : new Color(31, 122, 75));
        }

        return component;
    }
};
```

จากนั้นกำหนดกึ่งกลางและผูกกับคอลัมน์เหมือนส่วนแรก

Renderer เปลี่ยนเฉพาะการแสดงผล ไม่ได้เปลี่ยนข้อมูลจริงใน TableModel

## 3. อัปเดต Summary

สร้าง Label แยกสำหรับค่าที่ต้องเปลี่ยน:

```java
JLabel totalLabel = new JLabel("Total: 0");
JLabel warningLabel = new JLabel("Warning: 0");
```

เมื่อข้อมูลเปลี่ยน ให้อ่านจำนวนจาก Service แล้วใช้ `setText(...)` อัปเดต Label

ซอร์สฉบับเต็ม: ดู `StatusCellRenderer` และ `buildSummaryPanel` ใน [`SmartFactoryFrame.java`](../../src/main/java/smartfactory/ui/SmartFactoryFrame.java)

## Challenge

เพิ่มสีส้มสำหรับ `MAINTENANCE` โดยเริ่มจากเพิ่มเงื่อนไขอีกหนึ่งสถานะใน Renderer

ถัดไป: [EP 3.8 — เชื่อม Service และ CRUD](ep08-service-crud.md)
