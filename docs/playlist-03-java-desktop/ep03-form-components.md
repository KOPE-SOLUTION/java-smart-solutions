# EP 3.3 — JLabel, JTextField และ JButton

## เป้าหมาย

- สร้าง Form รับรหัส ชื่อ และตำแหน่ง
- อ่านค่าจาก TextField
- ตั้งค่า Font, Border และระยะห่างเบื้องต้น

```java
JTextField idField = new JTextField(15);
JTextField nameField = new JTextField(15);
JTextField locationField = new JTextField(15);
JButton saveButton = new JButton("บันทึก");

JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
form.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
form.add(new JLabel("รหัส:"));
form.add(idField);
form.add(new JLabel("ชื่อเครื่องจักร:"));
form.add(nameField);
form.add(new JLabel("ตำแหน่ง:"));
form.add(locationField);
form.add(new JLabel());
form.add(saveButton);
```

Component ทำหน้าที่ต่างกัน:

- `JLabel` แสดงข้อความ
- `JTextField` รับข้อความหนึ่งบรรทัด
- `JButton` สร้าง Action เมื่อผู้ใช้กด
- `JPanel` จัดกลุ่มและจัดตำแหน่ง Component

## Challenge

เพิ่มช่องอุณหภูมิและแรงสั่น แล้วกำหนดความกว้างของ TextField ให้เหมาะสม

ถัดไป: [EP 3.4 — Event และ Listener](ep04-event-listener.md)

