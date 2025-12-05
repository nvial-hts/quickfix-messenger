package com.jramoyo.fix.model.decode;

import com.jramoyo.qfixmessenger.QFixMessengerConstants;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MessageFormatter {
    public static String formatFixToString(quickfix.Message msg) {
        return msg != null ? formatFixToString(msg.toString()) : null;
    }

    public static String formatFixToString(String line) {
        return line != null ? line.replace(QFixMessengerConstants.SOH, QFixMessengerConstants.PIPE) : null;
    }

    public static String formatStringToFix(String line) {
        return line != null ? line.replace(QFixMessengerConstants.PIPE, QFixMessengerConstants.SOH) : null;
    }

    private static String generateSimpleRandomString()
    {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 100000);
        return timestamp + String.valueOf(random);
    }

    public static String generateUtcTimeStamp()
    {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(QFixMessengerConstants.UTC_DATE_FORMAT));
    }

    public static String formatStringFromCheatCode(String line)
    {
        switch (line)
        {
            case "###RANDOM###":
                return generateSimpleRandomString();

            case "###TIME###":
                return generateUtcTimeStamp();

            default:
                return line;
        }
    }
}
