package nz.co.canadia.coolsodacan;

import java.text.NumberFormat;
import java.util.Locale;

public class AndroidFormatter implements Formatter {
    @Override
    public String commaPrint(int score) {
        return NumberFormat.getIntegerInstance().format(score);
    }

    @Override
    public String zeroPadTime(int i, Locale locale) {
        return String.format(locale, "%02d", i);
    }
}
