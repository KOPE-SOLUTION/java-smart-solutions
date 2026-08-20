# EP 3.5 — JOptionPane และ Validation

## เป้าหมาย

- สร้าง Popup รับและยืนยันข้อมูล
- แยก OK กับ Cancel
- แสดง Error โดยไม่ทำให้โปรแกรมปิด

```java
int answer = JOptionPane.showConfirmDialog(
        frame,
        form,
        "เพิ่มเครื่องจักร",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
);

if (answer == JOptionPane.OK_OPTION) {
    String id = idField.getText().trim();
    if (id.isBlank()) {
        JOptionPane.showMessageDialog(
                frame,
                "กรุณากรอกรหัสเครื่องจักร",
                "ข้อมูลไม่ถูกต้อง",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
```

Validation สองชั้น:

1. UI ตรวจรูปแบบที่ช่วยผู้ใช้แก้ไขได้ทันที
2. Model/Service ตรวจ Business Rule เพื่อป้องกันทุกช่องทาง

ห้ามพึ่ง UI Validation เพียงชั้นเดียว เพราะ Console, Test หรือ API อาจเรียก Service โดยไม่ผ่านหน้าจอ

## Challenge

รับอุณหภูมิเป็น String แล้วจัดการ `NumberFormatException` ด้วยข้อความที่อ่านเข้าใจง่าย

ถัดไป: [EP 3.6 — JTable และ TableModel](ep06-jtable.md)

