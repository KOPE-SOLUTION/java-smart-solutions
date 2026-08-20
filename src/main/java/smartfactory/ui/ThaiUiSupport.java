package smartfactory.ui;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** กำหนด Locale, ข้อความ และฟอนต์ให้ Swing แสดงภาษาไทยได้สม่ำเสมอ */
public final class ThaiUiSupport {
    private static final String THAI_SAMPLE = "ภาษาไทย เครื่องจักร ต้องตรวจสอบ ตกลง ยกเลิก";
    private static final String[] PREFERRED_FONTS = {
            "Leelawadee UI",
            "Tahoma",
            "Noto Sans Thai",
            "Arial",
            Font.DIALOG
    };
    private static final String[] REQUIRED_FONT_KEYS = {
            "Label.font",
            "Button.font",
            "TextField.font",
            "Table.font",
            "TableHeader.font"
    };
    private static final String[] OPTIONAL_FONT_KEYS = {
            "OptionPane.messageFont",
            "OptionPane.buttonFont"
    };

    private ThaiUiSupport() {
    }

    public static void configure() {
        Locale thaiLocale = Locale.forLanguageTag("th-TH");
        Locale.setDefault(thaiLocale);
        JComponent.setDefaultLocale(thaiLocale);

        Font thaiFont = findThaiFont();
        applyFontToSwingDefaults(thaiFont);
        applyThaiOptionPaneText();
    }

    public static boolean defaultFontsCanDisplayThai() {
        for (String fontKey : REQUIRED_FONT_KEYS) {
            Font font = UIManager.getFont(fontKey);
            if (font == null || font.canDisplayUpTo(THAI_SAMPLE) != -1) {
                return false;
            }
        }
        for (String fontKey : OPTIONAL_FONT_KEYS) {
            Font font = UIManager.getFont(fontKey);
            if (font != null && font.canDisplayUpTo(THAI_SAMPLE) != -1) {
                return false;
            }
        }
        return true;
    }

    private static Font findThaiFont() {
        Set<String> installedFonts = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()
        ));

        for (String fontName : PREFERRED_FONTS) {
            if (!installedFonts.contains(fontName) && !Font.DIALOG.equals(fontName)) {
                continue;
            }
            Font candidate = new Font(fontName, Font.PLAIN, 13);
            if (candidate.canDisplayUpTo(THAI_SAMPLE) == -1) {
                return candidate;
            }
        }

        // Dialog เป็น logical font ของ Java และใช้ font fallback ของระบบปฏิบัติการ
        return new Font(Font.DIALOG, Font.PLAIN, 13);
    }

    private static void applyFontToSwingDefaults(Font thaiFont) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font currentFont) {
                int fontSize = Math.max(currentFont.getSize(), 13);
                UIManager.put(key, new FontUIResource(
                        thaiFont.getFamily(), currentFont.getStyle(), fontSize
                ));
            }
        }
    }

    private static void applyThaiOptionPaneText() {
        UIManager.put("OptionPane.okButtonText", "ตกลง");
        UIManager.put("OptionPane.cancelButtonText", "ยกเลิก");
        UIManager.put("OptionPane.yesButtonText", "ใช่");
        UIManager.put("OptionPane.noButtonText", "ไม่");
    }
}
