package spider.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hello world on 2017/1/9.
 */
public class DateUtil {
    /**
     * String To String 日期格式字符串之间的转换
     *
     * @param str      转换的字符串
     * @param pattern1 转换的字符串的日期格式，例如 yyyy-MM-dd HH:mm:ss
     * @param pattern2 转换目的字符串的日期格式，例如 yyyy-MM-dd
     * @return String
     */
    public static String formatStr(String str, String pattern1, String pattern2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern1);
            Date date = sdf.parse(str);
            return new SimpleDateFormat(pattern2).format(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("don't format type");
        }
    }

    /**
     * Date To String 日期转字符串
     *
     * @param date    日期
     * @param pattern 目的字符串的日期格式，例如 yyyy-MM-dd
     * @return String
     */
    public static String formatDate(Date date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * String To Date 字符串转日期格式
     *
     * @param date    日期格式的字符串
     * @param pattern 日期格式，例如 yyyy-MM-dd
     * @return Date
     */
    public static Date parseDate(String date, String pattern) {
        date=date.replace(".","-");
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("YYYY").parse(date);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * long类型转date类型
     *
     * @param date long类型date
     * @return Date
     */
    public static Date getDateByLong(long date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = new Date(date);
        String dateStr = sdf.format(d);
        return parseDate(dateStr, pattern);
    }

    /**
     * 计算两个日期的相差天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date startDate, Date endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = sdf.parse(sdf.format(startDate));
        endDate = sdf.parse(sdf.format(endDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long startTime = cal.getTimeInMillis();
        cal.setTime(endDate);
        long endTime = cal.getTimeInMillis();
        long betweenDays = (endTime - startTime) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 给当前日期添加天数
     *
     * @param date 日期
     * @param day  天数
     * @return Date
     */
    public static Date addDateDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        date = c.getTime();
        return date;
    }

    /**
     * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
     *
     * @param dateString 需要转换为timestamp的字符串
     * @return dataTime timestamp
     */
    public final static java.sql.Timestamp string2Time(String dateString)
            throws java.text.ParseException {
        DateFormat dateFormat;
//    dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",  
//        Locale.ENGLISH);// 设定格式  
        dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        dateFormat.setLenient(false);
        java.util.Date timeDate = dateFormat.parse(dateString);// util类型
        java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
        return dateTime;
    }

    /**
     * method 将字符串类型的日期按照转换为一个timestamp（时间戳记java.sql.Timestamp）
     *
     * @param dateString     需要转换为timestamp的字符串
     * @param formaterString dateString字符串的解析格式
     * @return
     * @throws java.text.ParseException
     */
    public final static java.sql.Timestamp string2Time(String dateString,
                                                       String formaterString) throws java.text.ParseException {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat(formaterString);// 设定格式
        // dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        dateFormat.setLenient(false);
        java.sql.Timestamp dateTime =null;
        try {
            java.util.Date timeDate = dateFormat.parse(dateString);// util类型
            dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型

        }catch (Exception e){
            e.printStackTrace();
        }

        return dateTime;
    }

    //util.date转化成sql.date
    public static java.sql.Date toSqlDate(Date date){
        return new java.sql.Date(date.getTime());
    }
}
