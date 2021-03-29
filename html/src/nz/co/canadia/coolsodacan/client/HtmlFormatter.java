package nz.co.canadia.coolsodacan.client;

import com.google.gwt.i18n.client.NumberFormat;

import java.util.Locale;

import nz.co.canadia.coolsodacan.Formatter;

public class HtmlFormatter implements Formatter {
    @Override
    public String commaPrint(int score) {
        return NumberFormat.getDecimalFormat().format(score);
    }

    @Override
    public String zeroPadTime(int i, Locale locale) {
        return NumberFormat.getFormat("#,#00").format(i);
    }
}
