package mario.com.br.velocita.holder.data;

public class InfoWindowStatisticsMarkerData {

    private boolean hasProfileImage;
    private String username;
    private double distanciaPercorrida;
    private long tempoGasto;
    private int velocidadeMaxima;
    private String unidadeVelocidade;
    private String mensagemStatus;

    public InfoWindowStatisticsMarkerData() {
    }

    public InfoWindowStatisticsMarkerData(String username, double distanciaPercorrida, long tempoGasto, int velocidadeMaxima, String unidadeVelocidade, String mensagemStatus) {
        this.username = username;
        this.distanciaPercorrida = distanciaPercorrida;
        this.tempoGasto = tempoGasto;
        this.velocidadeMaxima = velocidadeMaxima;
        this.unidadeVelocidade = unidadeVelocidade;
        this.mensagemStatus = mensagemStatus;
    }

    public boolean isHasProfileImage() {
        return hasProfileImage;
    }

    public void setHasProfileImage(boolean hasProfileImage) {
        this.hasProfileImage = hasProfileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getDistanciaPercorrida() {
        return distanciaPercorrida;
    }

    public void setDistanciaPercorrida(double distanciaPercorrida) {
        this.distanciaPercorrida = distanciaPercorrida;
    }

    public long getTempoGasto() {
        return tempoGasto;
    }

    public void setTempoGasto(long tempoGasto) {
        this.tempoGasto = tempoGasto;
    }

    public int getVelocidadeMaxima() {
        return velocidadeMaxima;
    }

    public void setVelocidadeMaxima(int velocidadeMaxima) {
        this.velocidadeMaxima = velocidadeMaxima;
    }

    public String getUnidadeVelocidade() {
        return unidadeVelocidade;
    }

    public void setUnidadeVelocidade(String unidadeVelocidade) {
        this.unidadeVelocidade = unidadeVelocidade;
    }

    public String getMensagemStatus() {
        return mensagemStatus;
    }

    public void setMensagemStatus(String mensagemStatus) {
        this.mensagemStatus = mensagemStatus;
    }
}
