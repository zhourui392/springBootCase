package cn.zz.user.date;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

/**
 * @author : zhourui
 * @version: 2019-07-31 11:51
 **/
public class LocalDateTest {

    @Test
    public void testLocalDateAndLocalTime(){
        LocalDate today = LocalDate.now();
        System.out.println("today is " + today);
        LocalDate specifiedDay = LocalDate.of(2016, Month.JUNE, 1);
        System.out.println("specific date is " + specifiedDay);
        LocalDate fifthIn2016 = LocalDate.ofYearDay(2016, 5);
        System.out.println("5th day of 2016 is " + fifthIn2016);
        // 默认使用 DateTimeFormatter.ISO_LOCAL_DATE
        LocalDate parsedDay = LocalDate.parse("2016-08-31");
        System.out.println("parsed day is " + parsedDay);
        // 内置 yyyyMMdd
        DateTimeFormatter basicIsoDate = DateTimeFormatter.BASIC_ISO_DATE;
        // 内置 yyyy-MM-dd
        DateTimeFormatter isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE;
        // 自定义 yyyy:MM:dd
        DateTimeFormatter customDate = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        // 转换
        LocalDate formattedDay = LocalDate.parse("20160821", basicIsoDate);
        System.out.println("isoLocalDate day is " + formattedDay.format(isoLocalDate));
        System.out.println("customDate day is " + formattedDay.format(customDate));
        // 时间操作
        LocalTime now = LocalTime.now();
        LocalTime later = now.plus(5, ChronoUnit.HOURS);
        // 日期操作
        today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        LocalDate afterOneMonth = today.plusMonths(1);
        LocalDate beforeOneMonth = today.minusMonths(1);
    }
}
