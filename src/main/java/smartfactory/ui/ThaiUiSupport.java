package smartfactory.ui;

import javafx.scene.text.Font;

import java.util.List;
import java.util.Locale;

/** เลือก Locale และฟอนต์ที่รองรับภาษาไทยสำหรับ JavaFX */
public final class ThaiUiSupport {
    private static final List<String> PREFERRED_FONTS = List.of(
            "Leelawadee UI",
            "Noto Sans Thai",
            "Tahoma",
            "Arial"
    );

    private ThaiUiSupport() {
    }

    public static void configureLocale() {
        Locale.setDefault(Locale.forLanguageTag("th-TH"));
    }

    public static String findPreferredFontFamily() {
        List<String> installedFonts = Font.getFamilies();
        for (String fontName : PREFERRED_FONTS) {
            if (installedFonts.contains(fontName)) {
                return fontName;
            }
        }
        return Font.getDefault().getFamily();
    }
}
