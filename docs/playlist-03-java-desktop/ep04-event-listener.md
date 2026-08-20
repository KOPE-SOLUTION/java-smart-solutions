# EP 3.4 — Event-driven Programming และ Listener

## เป้าหมาย

- ให้ปุ่มตอบสนองเมื่อผู้ใช้กด
- อ่านค่าจาก TextField
- เปลี่ยนข้อความบนหน้าจอหลังบันทึก

Desktop App รอ Event จากผู้ใช้ ไม่ได้ทำงานจากบนลงล่างแล้วปิดเหมือนโปรแกรม Console

## 1. เพิ่ม Label แสดงผล

วางหลังสร้าง `saveButton`:

```java
JLabel statusLabel = new JLabel("ยังไม่มีข้อมูล");
actions.add(statusLabel);
```

## 2. ผูก Event กับปุ่ม

วางใน Method เดียวกับที่สร้าง `idField`, `nameField` และ `saveButton` เพื่อให้ Lambda เข้าถึง Component เหล่านี้ได้:

```java
saveButton.addActionListener(event -> {
    String id = idField.getText().trim();
    String name = nameField.getText().trim();
    statusLabel.setText("บันทึก " + id + " - " + name);
});
```

คำสั่งภายใน Lambda จะทำงานเมื่อผู้ใช้กดปุ่ม Save

## 3. ทดลอง Event อื่น

Event ที่พบได้บ่อย ได้แก่ กดปุ่ม เลือกแถวในตาราง กด Enter ใน TextField และปิดหน้าต่าง เริ่มจากปุ่มหนึ่งตัวให้ทำงานก่อนเพิ่ม Event ชนิดอื่น

## Challenge

เพิ่มปุ่ม Clear แล้วผูก Event ให้ล้าง TextField ทีละช่องด้วย `setText("")`

ถัดไป: [EP 3.5 — JOptionPane และ Validation](ep05-dialog-validation.md)
