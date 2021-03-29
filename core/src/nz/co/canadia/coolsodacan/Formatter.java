package nz.co.canadia.coolsodacan;

import java.util.Locale;

public interface Formatter {
    String commaPrint(int score);

    String zeroPadTime(int i, Locale locale);
}
