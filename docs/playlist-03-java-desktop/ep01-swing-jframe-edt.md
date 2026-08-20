# EP 3.1 — Swing, JFrame และ Event Dispatch Thread

## เป้าหมาย

- เข้าใจโครงสร้างพื้นฐานของ Swing
- เปิดและปิด `JFrame` อย่างถูกต้อง
- สร้าง UI บน Event Dispatch Thread

## สร้างไฟล์ `FirstWindow.java`

```java
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class FirstWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Smart Factory");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.add(new JLabel("Machine Monitor", SwingConstants.CENTER));
            frame.setVisible(true);
        });
    }
}
```

```powershell
javac -encoding UTF-8 FirstWindow.java
java -Dfile.encoding=UTF-8 FirstWindow
```

Swing component ควรถูกสร้างและแก้ไขบน Event Dispatch Thread (EDT) ส่วนงานที่ใช้เวลานานไม่ควรขวาง Thread นี้

## Challenge

เปลี่ยนชื่อหน้าต่าง ขนาด และข้อความตรงกลาง จากนั้นลองเปลี่ยน `EXIT_ON_CLOSE` เป็น `DISPOSE_ON_CLOSE`

ถัดไป: [EP 3.2 — JPanel และ Layout Manager](ep02-jpanel-layout.md)

