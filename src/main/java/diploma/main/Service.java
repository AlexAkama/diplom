package diploma.main;

import java.util.Date;
import java.util.Random;

public class Service {

    public static String dateToSqlDate(Date date) {
        return new java.sql.Timestamp(date.getTime()).toString();
    }

    public static long dateToFront(Date date) {
        return date.getTime()/1000;
    }

    public static String randomString(int length) {
        char[] chars = "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx1234567890".toCharArray();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }
}
