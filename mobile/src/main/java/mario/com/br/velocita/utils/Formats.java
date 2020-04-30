package mario.com.br.velocita.utils;

public class Formats {

    public static String distanciaPercorrida(double v) {
        return formatFloatingPoint(v, 2);
    }

    public static String tempoGasto(double v) {
        return formatFloatingPoint(v, 2);
    }

    public static String velocidade(int v) {
        return String.format("%d", v);
    }

    private static String formatFloatingPoint(double v, int decimals) {
        return String.format("%." + decimals + "f", v);
    }
}
