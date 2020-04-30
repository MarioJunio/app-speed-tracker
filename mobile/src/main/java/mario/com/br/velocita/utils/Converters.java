package mario.com.br.velocita.utils;

import mario.com.br.velocita.holder.data.Time;

public class Converters {

    // DISTANCE
    public static final int METROS_IN_KM = 1000;

    // TIME
    public static final int MILLI_IN_SECOND = 1000;
    public static final int SECOND_IN_MINUTE = 60;
    public static final int MINUTE_IN_HOUR = 60;
    public static final int HOUR_IN_DAY = 24;

    public static Time millisToTime(long millis) {

        long elapsedTime = millis / MILLI_IN_SECOND;
        long seconds = elapsedTime % SECOND_IN_MINUTE;
        long minutes = (elapsedTime / SECOND_IN_MINUTE) % MINUTE_IN_HOUR;
        long hours = (elapsedTime / (SECOND_IN_MINUTE * SECOND_IN_MINUTE)) % HOUR_IN_DAY;
        long days = (elapsedTime / (SECOND_IN_MINUTE * SECOND_IN_MINUTE)) / HOUR_IN_DAY;

        return new Time(seconds, minutes, hours, days);
    }
}
