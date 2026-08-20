# EP 3.5 — JOptionPane และ Validation

## เป้าหมาย

- เปิด Popup ด้วย `JOptionPane`
- แยกการกด OK ออกจาก Cancel
- แจ้งเมื่อผู้ใช้ไม่กรอกรหัส

## 1. เปิด Confirm Dialog

```java
int answer = JOptionPane.showConfirmDialog(
        frame,
        form,
        "เพิ่มเครื่องจักร",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
);
```

ค่าที่คืนมาจะบอกว่าผู้ใช้กดปุ่มใด

## 2. ตรวจการกด OK

```java
if (answer == JOptionPane.OK_OPTION) {
    System.out.println("User selected OK");
}
```

กด Cancel แล้วโค้ดภายใน `if` จะไม่ทำงาน

## 3. ตรวจช่องรหัส

แทนที่ `println` ในส่วนเดิมด้วย:

```java
String id = idField.getText().trim();

if (id.isBlank()) {
    JOptionPane.showMessageDialog(
            frame,
            "กรุณากรอกรหัสเครื่องจักร",
            "ข้อมูลไม่ถูกต้อง",
            JOptionPane.ERROR_MESSAGE
    );
}
```

UI Validation ช่วยให้ผู้ใช้แก้ข้อมูลได้ทันที ส่วน Model และ Service ยังต้องตรวจ Business Rule ซ้ำเพื่อป้องกันการเรียกจาก Console, Test หรือ API

## Challenge

รับอุณหภูมิเป็น String แล้วใช้ `try/catch` แสดงข้อความเมื่อเกิด `NumberFormatException`

ถัดไป: [EP 3.6 — JTable และ TableModel](ep06-jtable.md)
