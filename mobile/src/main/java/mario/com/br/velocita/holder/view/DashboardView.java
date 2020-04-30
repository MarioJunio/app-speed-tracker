package mario.com.br.velocita.holder.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

import mario.com.br.velocita.R;
import mario.com.br.velocita.adapters.InfoWindowStatisticsMarkerAdapter;
import mario.com.br.velocita.services.Permissions;
import mario.com.br.velocita.services.TimeObserver;

public class DashboardView {

    public View view;
    public GoogleMap gMap;

    public View boxProgressMeters;
    public CountAnimationTextView progressDistance;
    public TextView progressDistanceUnit;
    public PointerSpeedometer speedometer;
    public FloatingActionButton btnStartProgress;
    public FloatingActionButton btnDoneProgress;
    public FloatingActionButton btnPostProgress;
    public FloatingActionButton btnCancelProgress;

    public DashboardView(View view) {
        this.view = view;
        this.boxProgressMeters = view.findViewById(R.id.box_progress_meters);
        this.progressDistance = view.findViewById(R.id.var_progress_distance);
        this.progressDistanceUnit = view.findViewById(R.id.var_progress_distance_unit);
        this.speedometer = view.findViewById(R.id.var_speedometer);
        this.btnStartProgress = view.findViewById(R.id.btn_start_progress);
        this.btnDoneProgress = view.findViewById(R.id.btn_done_progress);
        this.btnPostProgress = view.findViewById(R.id.btn_post_progress);
        this.btnCancelProgress = view.findViewById(R.id.btn_cancel_progress);

        this.progressDistance.setAnimationDuration(0);
    }

    public void startProgressState() {
        this.progressDistance.setText("0");
        this.progressDistanceUnit.setText("metros");
        this.speedometer.speedTo(0, 0);

        this.boxProgressMeters.setVisibility(View.VISIBLE);
        this.speedometer.setVisibility(View.VISIBLE);
        this.btnCancelProgress.setVisibility(View.VISIBLE);
        this.btnStartProgress.setVisibility(View.GONE);
        this.btnDoneProgress.setVisibility(View.GONE);
        this.btnPostProgress.setVisibility(View.GONE);

    }

    public void hideProgressGadgets() {
        this.boxProgressMeters.setVisibility(View.GONE);
        this.speedometer.setVisibility(View.GONE);
        this.btnCancelProgress.setVisibility(View.GONE);
        this.btnDoneProgress.setVisibility(View.GONE);
        this.btnStartProgress.setVisibility(View.VISIBLE);
        this.btnPostProgress.setVisibility(View.GONE);
    }

    public void showPostProgressGadgets() {
        this.btnStartProgress.setVisibility(View.GONE);
        this.btnDoneProgress.setVisibility(View.GONE);
        this.btnPostProgress.setVisibility(View.VISIBLE);
    }

    @SuppressLint("MissingPermission")
    public void setupGoogleMap(GoogleMap gMap) {
        this.gMap = gMap;
        this.gMap.getUiSettings().setMapToolbarEnabled(false);
        this.gMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.gMap.getUiSettings().setCompassEnabled(false);
    }

    public void updateMapStyle(Context context, TimeObserver.TimeHour timeHour) {
        // se o mapa já tiver sido carregado previamente
        if (this.gMap != null) {

            // O sol nasce por volta das 7 horas da manhã e se poi por volta das 18 horas da tarde
            // no horário de verão é necessário adicionar uma hora a mais
            if (timeHour == TimeObserver.TimeHour.DAY)
                this.gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_retro));
            else
                this.gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_night));

        }

    }

    public void updateMapInfoWindowAdapter(InfoWindowStatisticsMarkerAdapter statisticsMarkerAdapter) {
        this.gMap.setInfoWindowAdapter(statisticsMarkerAdapter);
    }

    /*public void setClusterManager(ClusterManager<ProgressClusterItem> clusterManager) {
        this.gMap.setOnCameraIdleListener(clusterManager);
    }*/
}
