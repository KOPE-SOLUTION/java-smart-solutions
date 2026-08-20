package smartfactory.ui;

import smartfactory.service.SmartFactoryService;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Entry point ของ Desktop Window App */
public class DesktopApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setSystemLookAndFeel();
            ThaiUiSupport.configure();
            SmartFactoryService service = SmartFactoryService.createWithSampleData();
            new SmartFactoryFrame(service).setVisible(true);
        });
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // ใช้ค่าเริ่มต้นของ Swing หากระบบไม่รองรับ look and feel นี้
        }
    }
}
