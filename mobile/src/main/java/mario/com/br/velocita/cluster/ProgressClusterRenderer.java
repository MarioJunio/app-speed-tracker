package mario.com.br.velocita.cluster;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import mario.com.br.velocita.holder.data.InfoWindowStatisticsMarkerData;
import mario.com.br.velocita.utils.Maps;

public class ProgressClusterRenderer extends DefaultClusterRenderer<ProgressClusterItem> implements ClusterManager.OnClusterItemClickListener<ProgressClusterItem> {

    private GoogleMap googleMap;
    private Context context;

    public ProgressClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.googleMap = map;
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(ProgressClusterItem item, MarkerOptions markerOptions) {

        if (item.type == ProgressClusterItem.Type.START) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Maps.createBitmapDefaultMarker(this.context, item.data.color)));
        } else if (item.type == ProgressClusterItem.Type.END) {
            if (item.data.hasImage)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Maps.createBitmapProfilePictureMarker(this.context, item.data.color)));
            else
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Maps.createBitmapDefaultMarkerProfile(this.context, item.data.color)));
        }
    }

    @Override
    protected void onClusterItemRendered(ProgressClusterItem clusterItem, Marker marker) {

        // adiciona dados para a info window somente se for um ponto de termino do progresso
        if (clusterItem.type == ProgressClusterItem.Type.END)
            marker.setTag(createInfoWindowStatisticsData(clusterItem.data));

        clusterItem.data.marker = marker;
    }

    @Override
    public boolean onClusterItemClick(ProgressClusterItem progressClusterItem) {
        Marker marker = progressClusterItem.data.marker;

        // mostra a info window somente se for um ponto de termino do progresso
        if (progressClusterItem.type == ProgressClusterItem.Type.END) {

            if (marker != null) {
                Object tag = marker.getTag();

                if (tag != null) {
                    Maps.moveCamera(googleMap, marker.getPosition(), 0, Maps.BROWSER_TILT);
                    marker.showInfoWindow();
                }

            }

        } else
            Maps.moveCamera(googleMap, marker.getPosition(), 0, Maps.BROWSER_TILT);

        return true;
    }

    private InfoWindowStatisticsMarkerData createInfoWindowStatisticsData(ProgressClusterItem.Data data) {
        InfoWindowStatisticsMarkerData infoWindowStatisticsMarkerData = new InfoWindowStatisticsMarkerData();
        infoWindowStatisticsMarkerData.setHasProfileImage(false);
        infoWindowStatisticsMarkerData.setUsername(data.username);
        infoWindowStatisticsMarkerData.setDistanciaPercorrida(data.distanciaPercorrida);
        infoWindowStatisticsMarkerData.setTempoGasto(data.tempoGasto);
        infoWindowStatisticsMarkerData.setVelocidadeMaxima(data.velocidadeMaxima);
        infoWindowStatisticsMarkerData.setUnidadeVelocidade(data.unidadeVelocidade);
        infoWindowStatisticsMarkerData.setMensagemStatus(data.mensagemStatus);

        return infoWindowStatisticsMarkerData;
    }
}
