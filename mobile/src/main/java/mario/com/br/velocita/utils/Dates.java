package mario.com.br.velocita.utils;

import java.util.Calendar;

public class Dates {

    /**
     * Retorna a hora do dia no formato 0-24
     *
     * @return
     */
    public static int hourOfDay() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isNight() {
        return !(Dates.hourOfDay() >= 7 && Dates.hourOfDay() < 18);
    }
}
