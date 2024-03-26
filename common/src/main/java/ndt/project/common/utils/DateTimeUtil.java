package ndt.project.common.utils;

import ndt.project.common.constants.DateTimeFormat;
import ndt.project.common.constants.RegexConstants;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static ndt.project.common.constants.RegexConstants.DATE_REGEX;


public class DateTimeUtil {
    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        if (Objects.isNull(dateToConvert)) return null;
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String getDateByFormat(String dtFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);
        return simpleDateFormat.format(new Date());
    }

    public static String getTimeByFormat(String dtFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);
        return simpleDateFormat.format(new Date());
    }

    public static LocalDate convertStringToLocalDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.MMDDYYYY);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalDate convertStringYYYYMMDDToLocalDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.YYYYMMDD);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalDate convertStringYYYYMMDDHyphenToLocalDate(String dateStr) {
        if (StringUtils.isBlank(dateStr) || isNotFormatDateYYYYMMDDHyphen(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.YYYYMMDD_HYPHEN);
        return LocalDate.parse(dateStr, formatter);
    }

    public static String convertInstantToYYYYMMDDHHMMSS(Instant instant) {
        if (Objects.isNull(instant)) return StringUtils.EMPTY;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateTimeFormat.YYYYMMDDHHMMSS);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dtf.format(zonedDateTime);
    }

    public static String convertInstantToDDMMYYYY(Instant instant) {
        if (Objects.isNull(instant)) return StringUtils.EMPTY;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateTimeFormat.YYYYMMDD_HYPHEN);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dtf.format(zonedDateTime);
    }

    public static boolean isTimeBetween(String timeCheck, LocalDate dateFrom, LocalDate dateTo) {
        return isTimeBetween(convertStringYYYYMMDDHyphenToLocalDate(timeCheck), dateFrom, dateTo);
    }

    public static boolean isTimeBetween(LocalDate timeCheck, LocalDate dateFrom, LocalDate dateTo) {
        if (Objects.isNull(timeCheck)) return false;
        if (Objects.isNull(dateTo) || dateTo.isAfter(LocalDate.now())) dateTo = LocalDate.now();
        if (Objects.isNull(dateFrom)) dateFrom = LocalDate.MIN;
        return !(timeCheck.isBefore(dateFrom) || timeCheck.isAfter(dateTo));
    }

    public static boolean isTimeBetween(Instant timeCheck, LocalDate dateFrom, LocalDate dateTo) {
        return isTimeBetween(timeCheck.atZone(ZoneId.systemDefault()).toLocalDate(), dateFrom, dateTo);
    }

    public static LocalDate convertStringDDMMYYToLocalDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.DDMMYYYY);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalDateTime convertStringYYYYMMDDHHMMSSToLocalDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.YYYYMMDDHHMMSS);
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static boolean checkFormatDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return false;

        Pattern pattern = Pattern.compile(DATE_REGEX);
        return pattern.matcher(dateStr).matches();
    }

    public static boolean isFormatDateYYYYMMDDHyphen(String input) {
        if (StringUtils.isBlank(input)) return false;
        Pattern pattern = Pattern.compile(RegexConstants.YYYYMMDD_HYPHEN_REGEX);
        return pattern.matcher(input).matches();
    }

    public static boolean isFormatDateYYYYMMDDHHMMSS(String input) {
        if (StringUtils.isBlank(input)) return false;
        Pattern pattern = Pattern.compile(RegexConstants.YYYYMMDD_HHMMSS_REGEX);
        return pattern.matcher(input).matches();
    }

    public static boolean isNotFormatDateYYYYMMDDHyphen(String input) {
        return !isFormatDateYYYYMMDDHyphen(input);
    }
}