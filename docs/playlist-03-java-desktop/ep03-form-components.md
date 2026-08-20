# EP 3.3 — JLabel, JTextField และ JButton

## เป้าหมาย

- สร้าง Form รับรหัส ชื่อ และตำแหน่ง
- จัดช่องกรอกด้วย GridLayout
- เพิ่มระยะห่างรอบ Form

## 1. สร้างช่องกรอกและปุ่ม

เพิ่ม Import ที่ยังไม่มีเหนือ Class:

```java
import javax.swing.BorderFactory;
import javax.swing.JTextField;
```

วางใน Method ที่สร้าง Form:

```java
JTextField idField = new JTextField(15);
JTextField nameField = new JTextField(15);
JTextField locationField = new JTextField(15);
JButton saveButton = new JButton("บันทึก");
```

## 2. สร้าง Panel

```java
JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
form.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
```

## 3. เพิ่ม Label และช่องกรอกทีละแถว

```java
form.add(new JLabel("รหัส:"));
form.add(idField);

form.add(new JLabel("ชื่อเครื่องจักร:"));
form.add(nameField);

form.add(new JLabel("ตำแหน่ง:"));
form.add(locationField);
```

เพิ่มแถวปุ่ม:

```java
form.add(new JLabel());
form.add(saveButton);
```

Component แต่ละชนิดมีหน้าที่ต่างกัน:

- `JLabel` แสดงข้อความ
- `JTextField` รับข้อความหนึ่งบรรทัด
- `JButton` ให้ผู้ใช้เริ่ม Action
- `JPanel` จัดกลุ่ม Component

## Challenge

เพิ่มช่องอุณหภูมิหนึ่งแถว แล้วเปลี่ยนจำนวนแถวของ `GridLayout` ให้ตรงกับ Component

ถัดไป: [EP 3.4 — Event และ Listener](ep04-event-listener.md)
