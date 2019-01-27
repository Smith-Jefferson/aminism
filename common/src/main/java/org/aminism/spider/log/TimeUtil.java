//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.aminism.spider.log;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    private static final String GENERAL_TIMEREGULAR = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal threadLocal = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    public static SimpleDateFormat sdf;

    public TimeUtil() {
    }

    public static String toString(Calendar c1) {
        return toString(c1.getTime());
    }

    public static double getHourGap(Calendar c1, Calendar c2) {
        return (double)(c1.getTimeInMillis() - c2.getTimeInMillis()) / 3600000.0D;
    }

    public static boolean isToday(Calendar takeoff) {
        LocalDate today = LocalDate.now();
        LocalDate takeoffday = LocalDate.of(takeoff.get(1), takeoff.get(2) + 1, takeoff.get(5));
        return today.isEqual(takeoffday);
    }

    public static String toString(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date tryParsetime(String timeStr) {
        return tryParsetime(timeStr, sdf);
    }

    public static Date tryParsetime(String timeStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return tryParsetime(timeStr, sdf);
    }

    public static Date tryParsetime(String timeStr, SimpleDateFormat format) {
        Date d = null;

        try {
            d = format.parse(timeStr);
        } catch (Exception var4) {

        }

        return d;
    }

    public static String tryFormateMonthDate(String timeStr) {
        Date temp = tryParsetime(timeStr);
        if (temp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(temp);
            tryFormateMonthDate(calendar);
        }

        return "";
    }

    public static String tryFormateMonthDate(Calendar calendar) {
        return MessageFormat.format("{0}月{1}日 {2}点{3}分", calendar.get(2) + 1, calendar.get(5), calendar.get(11), calendar.get(12));
    }

    public static boolean isDefaultTime(Calendar calendar) {
        return calendar == null || isCSharpDefault(calendar.getTime());
    }

    private static boolean isCSharpDefault(Date date) {
        String mytime = sdf.format(date);
        return mytime.contains("0001-01");
    }

    public static boolean isDefaultTime(Timestamp timestamp) {
        return timestamp == null || isCSharpDefault(timestamp);
    }

    public static Calendar getUtcNow() {
        Calendar calendar = Calendar.getInstance();
        int zoneoffset = calendar.get(15);
        int dstoffset = calendar.get(16);
        calendar.add(14, -(zoneoffset + dstoffset));
        return calendar;
    }

    public static Calendar addDays(Calendar t, int num) {
        if (t == null) {
            return null;
        } else {
            Calendar tt = (Calendar)t.clone();
            tt.add(5, num);
            return tt;
        }
    }

    public static Calendar addHours(Calendar t, int num) {
        if (t == null) {
            return null;
        } else {
            Calendar tt = (Calendar)t.clone();
            tt.add(11, num);
            return tt;
        }
    }

    public static Calendar toCalendar(Timestamp t) {
        if (t == null) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(t.getTime()));
            return c;
        }
    }

    public static Timestamp toTimeStamp(Calendar c) {
        return c == null ? null : new Timestamp(c.getTimeInMillis());
    }

    public static Timestamp dateTimeNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp dateTime(Calendar calendar, int num) {
        Calendar c1 = addHours(calendar, num);
        return toTimeStamp(c1);
    }

    public static Timestamp dateTime(int num) {
        return dateTime(Calendar.getInstance(), num);
    }

    static {
        sdf = (SimpleDateFormat)threadLocal.get();
    }
}
