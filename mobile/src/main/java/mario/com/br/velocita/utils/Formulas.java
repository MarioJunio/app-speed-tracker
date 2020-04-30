package mario.com.br.velocita.utils;

public class Formulas {

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
    private static final double KM_H = 3.6d;

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    /**
     * @param distance em metros
     * @param time     em segundos
     * @return velocidade em m/s
     */
    public static double speed(double distance, double time) {
        return distance / time;
    }

    /**
     * @param distance em metros
     * @param time     em segundos
     * @return velocidade em m/s
     */
    public static int speedKmH(double distance, double time) {
        return (int) Math.round(((distance / time) * KM_H));
    }

    public static int toKMH(float speed) {
        return (int) Math.round(speed * KM_H);
    }
}
