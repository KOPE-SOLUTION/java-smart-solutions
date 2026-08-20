# EP 3.1 — Swing, JFrame และ Event Dispatch Thread

## เป้าหมาย

- เปิดและปิดหน้าต่าง `JFrame`
- กำหนดชื่อ ขนาด และตำแหน่งหน้าต่าง
- สร้าง UI บน Event Dispatch Thread

## 1. สร้างไฟล์และ Import

สร้างไฟล์ `FirstWindow.java`:

```java
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
```

## 2. สร้างจุดเริ่มโปรแกรม

วางต่อจาก Import:

```java
public class FirstWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createWindow());
    }

    // เพิ่ม Method createWindow ในส่วนถัดไป
}
```

`SwingUtilities.invokeLater(...)` ส่งงานสร้างหน้าต่างไปยัง Event Dispatch Thread หรือ EDT

## 3. สร้างหน้าต่าง

วาง Method นี้ภายใน Class แต่นอก `main`:

```java
private static void createWindow() {
    JFrame frame = new JFrame("Smart Factory");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 400);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}
```

Compile และ Run:

```powershell
javac -encoding UTF-8 FirstWindow.java
java -Dfile.encoding=UTF-8 FirstWindow
```

## 4. เพิ่มข้อความตรงกลาง

เพิ่มบรรทัดนี้ก่อน `frame.setVisible(true);`:

```java
frame.add(new JLabel("Machine Monitor", SwingConstants.CENTER));
```

Swing Component ควรถูกสร้างและแก้ไขบน EDT ส่วนงานที่ใช้เวลานานไม่ควรขวาง Thread นี้

## Challenge

เปลี่ยนชื่อหน้าต่าง ขนาด และข้อความตรงกลาง จากนั้นลองเปลี่ยน `EXIT_ON_CLOSE` เป็น `DISPOSE_ON_CLOSE`

ถัดไป: [EP 3.2 — JPanel และ Layout Manager](ep02-jpanel-layout.md)
