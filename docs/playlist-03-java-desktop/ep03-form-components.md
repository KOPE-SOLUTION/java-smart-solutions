# EP 3.3 — JLabel, JTextField และ JButton

## เป้าหมาย

- สร้าง Form รับรหัส ชื่อ และตำแหน่ง
- จัดช่องกรอกด้วย `GridLayout`
- เพิ่มระยะห่างรอบ Form
- เตรียมตัวแปร Component สำหรับใช้ใน Event

ทำงานต่อใน Method `createWindow()` ของ `FirstWindow.java` โดยใช้ตัวแปร `content` จาก EP3.2

## 1. เพิ่ม Import

วางรวมกับ Import เดิมเหนือ Class:

```java
import javax.swing.BorderFactory;
import javax.swing.JTextField;
```

## 2. สร้างช่องกรอกและปุ่ม Save

วางภายใน `createWindow()` หลังส่วน Summary:

```java
JTextField idField = new JTextField(15);
JTextField nameField = new JTextField(15);
JTextField locationField = new JTextField(15);
JButton saveButton = new JButton("บันทึก");
```

เก็บ Component ไว้ในตัวแปรเพื่อให้ Lambda ใน EP3.4 อ่านและเปลี่ยนค่าได้

## 3. สร้าง Form Panel

วางต่อจากการสร้าง Component:

```java
JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
form.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
```

- `4` คือจำนวนแถว
- `2` คือจำนวนคอลัมน์
- `10, 10` คือระยะห่างแนวนอนและแนวตั้ง

## 4. เพิ่มข้อมูลทีละแถว

วางต่อจากการสร้าง `form`:

```java
form.add(new JLabel("รหัส:"));
form.add(idField);

form.add(new JLabel("ชื่อเครื่องจักร:"));
form.add(nameField);

form.add(new JLabel("ตำแหน่ง:"));
form.add(locationField);

form.add(new JLabel());
form.add(saveButton);
```

แถวสุดท้ายใช้ Label ว่างเพื่อดันปุ่มไปอยู่คอลัมน์ขวา

## 5. นำ Form ไปแสดง

วางต่อจากโค้ด Form:

```java
content.add(form, BorderLayout.CENTER);
```

อย่าใช้ `root.add(form, BorderLayout.CENTER)` เพราะ EP3.2 นำ `content` ไปวางตรงกลางของ Root ไว้แล้ว

## 6. Compile และ Run

```powershell
javac -encoding UTF-8 FirstWindow.java
java -Dfile.encoding=UTF-8 FirstWindow
```

ผลที่ต้องเห็นคือ Form สามช่องและปุ่มบันทึกอยู่ใต้ Summary ตอนนี้ปุ่มยังไม่ตอบสนอง เพราะเราจะผูก Event ใน EP3.4

## Component แต่ละชนิด

- `JLabel` แสดงข้อความ
- `JTextField` รับข้อความหนึ่งบรรทัด
- `JButton` ให้ผู้ใช้เริ่ม Action
- `JPanel` จัดกลุ่ม Component
- `BorderFactory` สร้างระยะห่างหรือเส้นขอบ

## ตรวจความพร้อมก่อนเข้า EP 3.4

- มีตัวแปร `idField`, `nameField` และ `locationField`
- มีตัวแปร `saveButton`
- Form ใช้ `GridLayout(4, 2, 10, 10)`
- Form ถูกเพิ่มลงใน `content`
- เปิดหน้าต่างแล้วเห็น Component ครบ

## Challenge

เพิ่มช่องอุณหภูมิ:

1. สร้าง `JTextField temperatureField`
2. เปลี่ยน Grid เป็น `new GridLayout(5, 2, 10, 10)`
3. เพิ่ม Label และ TextField ก่อนแถวปุ่ม

ถัดไป: [EP 3.4 — Event และ Listener](ep04-event-listener.md)
