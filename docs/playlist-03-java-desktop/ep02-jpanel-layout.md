# EP 3.2 — JPanel และ Layout Manager

## เป้าหมาย

- แบ่ง UI เป็น Panel ย่อย
- วาง Panel ด้วย Layout Manager
- หลีกเลี่ยงการกำหนดตำแหน่งด้วย Pixel

ทำงานต่อใน Method `createWindow()` จาก EP 3.1 โดยลบ `JLabel` ตัวเดิมออกก่อน และวางโค้ดทุกส่วนก่อน `frame.setVisible(true);`

## 1. สร้าง Root Panel

```java
JPanel root = new JPanel(new BorderLayout(10, 10));
frame.add(root);
```

`BorderLayout` แบ่งพื้นที่เป็นด้านบน ล่าง ซ้าย ขวา และตรงกลาง

## 2. เพิ่ม Header

```java
JPanel header = new JPanel();
header.add(new JLabel("SMART FACTORY"));
root.add(header, BorderLayout.NORTH);
```

## 3. เพิ่ม Summary

```java
JPanel summary = new JPanel(new GridLayout(1, 3, 10, 0));
summary.add(new JLabel("Total: 3"));
summary.add(new JLabel("Running: 2"));
summary.add(new JLabel("Warning: 1"));
root.add(summary, BorderLayout.CENTER);
```

`GridLayout(1, 3)` จัด Component เป็นหนึ่งแถวสามช่องขนาดเท่ากัน

## 4. เพิ่มแถวปุ่ม

```java
JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
actions.add(new JButton("Add"));
actions.add(new JButton("Update"));
root.add(actions, BorderLayout.SOUTH);
```

เพิ่ม Import ที่ต้องใช้เหนือ Class:

```java
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
```

## Challenge

เพิ่ม Summary ช่องที่สี่เป็น `Maintenance: 0` และเพิ่มปุ่ม `Delete`

ถัดไป: [EP 3.3 — Form Components](ep03-form-components.md)
