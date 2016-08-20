package com.github.frapontillo.ncu;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SystemClock implements Clock {

    @Override
    public Date getTodayAtMidnight() {
        GregorianCalendar date = new GregorianCalendar(Locale.UK);

        date.set(GregorianCalendar.HOUR_OF_DAY, 0);
        date.set(GregorianCalendar.MINUTE, 0);
        date.set(GregorianCalendar.SECOND, 0);
        date.set(GregorianCalendar.MILLISECOND, 0);

        return date.getTime();
    }

}
