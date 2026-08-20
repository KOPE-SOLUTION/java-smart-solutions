# EP 3.2 — JPanel และ Layout Manager

## เป้าหมาย

- แบ่ง UI เป็น Panel ย่อย
- ใช้ BorderLayout, GridLayout และ FlowLayout
- หลีกเลี่ยงการกำหนดตำแหน่งด้วย Pixel

```java
JPanel root = new JPanel(new BorderLayout(10, 10));

JPanel header = new JPanel();
header.add(new JLabel("SMART FACTORY"));
root.add(header, BorderLayout.NORTH);

JPanel summary = new JPanel(new GridLayout(1, 3, 10, 0));
summary.add(new JLabel("Total: 3"));
summary.add(new JLabel("Running: 2"));
summary.add(new JLabel("Warning: 1"));
root.add(summary, BorderLayout.CENTER);

JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
actions.add(new JButton("Add"));
actions.add(new JButton("Update"));
root.add(actions, BorderLayout.SOUTH);
```

| Layout | เหมาะกับ |
|---|---|
| `BorderLayout` | พื้นที่หลัก North/South/Center/East/West |
| `GridLayout` | Card หรือ Form ที่ขนาดเท่ากัน |
| `FlowLayout` | แถวปุ่มหรือ Component ต่อเนื่อง |

## Challenge

สร้าง Panel ที่มี Header ด้านบน Summary Card 4 ช่องตรงกลาง และปุ่ม 3 ปุ่มด้านล่าง

ถัดไป: [EP 3.3 — Form Components](ep03-form-components.md)

