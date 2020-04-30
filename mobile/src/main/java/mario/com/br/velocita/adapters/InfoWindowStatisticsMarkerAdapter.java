package mario.com.br.velocita.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import de.hdodenhof.circleimageview.CircleImageView;
import mario.com.br.velocita.R;
import mario.com.br.velocita.holder.data.InfoWindowStatisticsMarkerData;
import mario.com.br.velocita.holder.data.Time;
import mario.com.br.velocita.utils.Converters;
import mario.com.br.velocita.utils.Formats;

public class InfoWindowStatisticsMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public InfoWindowStatisticsMarkerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.view_info_window_statistics_marker, null);
        boolean hasTag = marker.getTag() != null;

        if (hasTag) {

            if (marker.getTag() instanceof InfoWindowStatisticsMarkerData) {

                InfoWindowStatisticsMarkerData data = (InfoWindowStatisticsMarkerData) marker.getTag();

                CircleImageView profileImage = view.findViewById(R.id.profile_image);
                TextView tvUsername = view.findViewById(R.id.var_username);
                TextView tvDistPercorrida = view.findViewById(R.id.var_distancia_percorrida);
                TextView tvTempoGasto = view.findViewById(R.id.var_tempo_gasto);
                TextView tvVelocidadeMaxima = view.findViewById(R.id.var_velocidade_maxima);
                TextView tvUnidadeVelocidade = view.findViewById(R.id.var_unidade_velocidade);
                TextView tvMensagemStatus = view.findViewById(R.id.var_mensagem_status);

                profileImage.setVisibility(data.isHasProfileImage() ? View.VISIBLE : View.GONE);
                tvUsername.setText(data.getUsername());
                tvDistPercorrida.setText(previewDistanciaPercorrida(data.getDistanciaPercorrida()));
                tvTempoGasto.setText(previewTempoGasto(data.getTempoGasto()));
                tvVelocidadeMaxima.setText(Formats.velocidade(data.getVelocidadeMaxima()));
                tvUnidadeVelocidade.setText(data.getUnidadeVelocidade());
                tvMensagemStatus.setText(data.getMensagemStatus());
            }
        }

        return hasTag ? view : null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private String previewDistanciaPercorrida(double distancia) {
        int km = (int) (distancia / Converters.METROS_IN_KM);
        int metros = (int) (distancia % Converters.METROS_IN_KM);

        String label = "";

        if (km > 0 && metros > 0)
            label = String.format("%d km e %d %s", km, metros, (metros > 1 ? "metros" : "metro"));
        else if (km > 0)
            label = String.format("%d km", km);
        else if (metros > 0)
            label = String.format("%d %s", metros, (metros > 1 ? "metros" : "metro"));

        return label + " ";

    }

    private String previewTempoGasto(long tempoGasto) {
        Time time = Converters.millisToTime(tempoGasto);

        if (time.getDays() > 0)
            return String.format("%d %s e %02d:%02d:%02d", time.getDays(), (time.getDays() > 1 ? "dias" : "dia"), time.getHours(), time.getMinutes(), time.getSeconds());
        else if (time.getHours() > 0)
            return String.format("%02d %s e %02d:02d", time.getHours(), (time.getHours() > 1 ? "horas" : "hora"), time.getMinutes(), time.getSeconds());
        else if (time.getMinutes() > 0)
            return String.format("%02d %s e %02d %s", time.getMinutes(), (time.getMinutes() > 1 ? "minutos" : "minuto"), time.getSeconds(), (time.getSeconds() > 1 ? "segundos" : "segundo"));
        else
            return String.format("%02d %s", time.getSeconds(), time.getSeconds() > 1 ? "segundos" : "segundo");

    }
}
