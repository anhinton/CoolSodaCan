package nz.co.canadia.coolsodacan.desktop;

import java.text.NumberFormat;
import java.util.Locale;

import nz.co.canadia.coolsodacan.Formatter;

public class DesktopFormatter implements Formatter {

    @Override
    public String commaPrint(int score) {
        return NumberFormat.getIntegerInstance().format(score);
    }

    @Override
    public String zeroPadTime(int i, Locale locale) {
        return String.format(locale, "%02d", i);
    }
}
