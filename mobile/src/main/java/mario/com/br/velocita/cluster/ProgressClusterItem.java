package mario.com.br.velocita.cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

public class ProgressClusterItem implements ClusterItem {

    public Data data;
    public Type type;

    public ProgressClusterItem(Data data, Type type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public LatLng getPosition() {
        return data.position;
    }

    public enum Type {
        START, END
    }

    public static class Data {
        public Marker marker;
        public LatLng position;
        public boolean hasImage;
        public int color;
        public String username;
        public double distanciaPercorrida;
        public long tempoGasto;
        public int velocidadeMaxima;
        public String unidadeVelocidade;
        public String mensagemStatus;


        public Data(LatLng position) {
            this(position, false, 0);
        }

        public Data(LatLng position, boolean hasImage, int color) {
            this.position = position;
            this.hasImage = hasImage;
            this.color = color;
        }
    }
}
