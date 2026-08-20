# EP 3.4 — Event-driven Programming และ Listener

## เป้าหมาย

- เข้าใจว่า Desktop App รอ Event แทนการทำงานแล้วจบทันที
- ผูก ActionListener กับ Button
- แยก Event Handler เป็น Method

```java
saveButton.addActionListener(event -> saveMachine());
```

```java
private void saveMachine() {
    String id = idField.getText().trim();
    String name = nameField.getText().trim();
    statusLabel.setText("บันทึก " + id + " - " + name);
}
```

Lambda `event -> saveMachine()` คือ implementation แบบสั้นของ `ActionListener`

Event อื่นที่ทดลองได้:

- กดปุ่ม
- เลือกแถวในตาราง
- กด Enter ใน TextField
- Timer ครบเวลา
- ปิดหน้าต่าง

## Challenge

เพิ่มปุ่ม Clear ที่ล้างทุก TextField และย้าย logic ล้าง Form ไป method `clearForm()`

ถัดไป: [EP 3.5 — JOptionPane และ Validation](ep05-dialog-validation.md)

