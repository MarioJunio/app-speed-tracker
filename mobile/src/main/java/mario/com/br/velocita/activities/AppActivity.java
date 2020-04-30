package mario.com.br.velocita.activities;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import mario.com.br.velocita.R;
import mario.com.br.velocita.holder.Data;
import mario.com.br.velocita.utils.Formulas;

public class AppActivity extends FragmentActivity implements OnMapReadyCallback, OnLocationUpdatedListener, LocationListener, Data.OnGpsServiceUpdate {

    private GoogleMap mMap;

    private TextView tvDistanciaPercorrida;
    private TextView tvVelocidade;
    private TextView tvVelocidadeMedia;
    private TextView tvPrecisao;
    private Chronometer chronometer;
    private Button btnStart;
    private Marker markerIni, markerEnd;
    private List<LatLng> points = new ArrayList<>();
    private List<Polyline> tracking = new ArrayList<>();

    private Data data = new Data(this);

    PolylineOptions options = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvDistanciaPercorrida = findViewById(R.id.var_distancia_percorrida);
        tvVelocidade = findViewById(R.id.var_velcidade);
        tvVelocidadeMedia = findViewById(R.id.var_velcidade_media);
        tvPrecisao = findViewById(R.id.var_precisao);
        chronometer = findViewById(R.id.chronometer);
        btnStart = findViewById(R.id.btn_start);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location location = mario.com.br.velocita.utils.SmartLocation.getLastLocation(this);

        // se a localização foi obtida então atualize a view
        if (location != null) {
            data.setCurrentLocation(location);
            data.update();
        }
    }

    public void onCreateMark(View view) {

        // se não está correndo
        if (!data.isProgress()) {

            // limpa a lista
            points.clear();
            clearTracking();
            data.reset();

            // remove a marca inicial
            if (markerIni != null) {
                markerIni.remove();
                markerIni = null;
            }

            // remove a marca final
            if (markerEnd != null) {
                markerEnd.remove();
                markerEnd = null;
            }

            // se o GPS está ativo, inicia o progresso atual
            if (SmartLocation.with(this).location().state().isGpsAvailable()) {
//                SmartLocation.with(this).location().config(LocationParams.NAVIGATION).start(this);
                mario.com.br.velocita.utils.SmartLocation.start(this, this);
//                data.setProgress(true);

                btnStart.setText("Encerrar Progresso");
            } else
                Toast.makeText(this, "GPS inativo", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Encerrando progresso atual", Toast.LENGTH_LONG).show();

            // não está mais em progresso
//            data.setProgress(false);

            // tempo final do progresso
            data.setEndTime(SystemClock.elapsedRealtime());

//            SmartLocation.with(this).location().stop();
            mario.com.br.velocita.utils.SmartLocation.stop();

            // para o chronometro
            chronometer.stop();

            // se a localização atual foi obtida então colocarei a marca fim no ponto atual
            if (data.getCurrentLocation() != null) {
                markerEnd = mMap.addMarker(new MarkerOptions().position(new LatLng(data.getCurrentLocation().getLatitude(), data.getCurrentLocation().getLongitude())).title("Mario Marques - Fim da marca"));
                data.update();
            } else
                Toast.makeText(this, "Localização atual não encontrada", Toast.LENGTH_SHORT).show();

            btnStart.setText("Criar marca");

        }

    }

    @Override
    public void onLocationUpdated(Location location) {

//        if (location.getAccuracy() < 10) {
//
//            newTime = System.currentTimeMillis();
//            newLocation = location;
//
//            atualizarInspetores();
//
//            lastTime = newTime;
//            lastLocation = newLocation;
//
//            markerLocations.add(location);
//        }
//
//        Toast.makeText(this, "Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        // se está em progresso captura essa localização atual
        if (data.isProgress()) {
            data.setCurrentTime(System.currentTimeMillis());
            data.setCurrentLocation(location);

            // se for a primeira vez que está obtendo a localização devemos setar as variáveis para cálculos futuros
            if (data.isFirst()) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                data.setStartTime(data.getCurrentTime());
                data.setLastTime(data.getCurrentTime());
                data.setLastLocation(data.getCurrentLocation());
                data.setFirst(false);
            }

            // calcula a distancia em metros entre os 2 últimos localizações capturadas
            // Calcular a velocidade: (distancia(m) / tempo(s)) = m/s
            double distance = data.getCurrentLocation().distanceTo(data.getLastLocation());

            // se a precisão em metros for menos que a distância
            if (location.getAccuracy() < distance) {
                data.addDistance(distance);
                data.setLastLocation(data.getCurrentLocation());
                data.setLastTime(System.currentTimeMillis());
            }

            // se tem velocidade já calculada iremos usa-la
            if (location.hasSpeed()) {
                data.setSpeed(Formulas.toKMH(location.getSpeed()));
                data.setMaxSpeed((data.getSpeed() > data.getMaxSpeed() ? data.getSpeed() : data.getMaxSpeed()));
            }

            // calcula a velocidade média
            data.setAvgSpeed(Formulas.speedKmH(data.getDistance(), ((data.getEndTime() == 0 ? data.getCurrentTime() : data.getEndTime()) - data.getStartTime()) / 1000d));

            // se a precisão foi informada
            if (location.hasAccuracy())
                data.setAccuracy(location.getAccuracy());

            // atualiza a view
            data.update();
        }

    }

    private void clearTracking() {

        for (Polyline polyline : tracking)
            polyline.remove();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void showOneRoute(Location ini, Location end) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(getClass().getCanonicalName(), String.format("LatLng INI: [%f, %f], Speed: %f, Speed Accuracy: %f", ini.getLatitude(), ini.getLongitude(), ini.getSpeed(), ini.getSpeedAccuracyMetersPerSecond()));
            Log.d(getClass().getCanonicalName(), String.format("LatLng END: [%f, %f], Speed: %f, Speed Accuracy: %f", end.getLatitude(), end.getLongitude(), end.getSpeed(), end.getSpeedAccuracyMetersPerSecond()));
        } else {
            Log.d(getClass().getCanonicalName(), String.format("LatLng INI: [%f, %f], Speed: %f", ini.getLatitude(), ini.getLongitude(), ini.getSpeed()));
            Log.d(getClass().getCanonicalName(), String.format("LatLng END: [%f, %f], Speed: %f", end.getLatitude(), end.getLongitude(), end.getSpeed()));
        }

        Log.d(getClass().getCanonicalName(), String.format("Distance INI-END: %f", ini.distanceTo(end)));
        Log.d(getClass().getCanonicalName(), "----------------------------------------------------------------------------------");

    }

    @Override
    public void update() {

        // ativa cronometro
        if (data.isProgress() && !chronometer.isActivated())
            chronometer.start();

        // se a marca incial não foi definida para este progresso então será definida agora
        if (data.isProgress() && markerIni == null)
            markerIni = mMap.addMarker(new MarkerOptions().position(new LatLng(data.getCurrentLocation().getLatitude(), data.getCurrentLocation().getLongitude())).title("Mario Marques - Inicio da marca"));

        // atualiza a view
        tvDistanciaPercorrida.setText(String.format("%.2f km", data.getDistance()));
        tvVelocidade.setText(String.format("%d km/h", data.getSpeed()));
        tvVelocidadeMedia.setText(String.format("%d km/h", data.getAvgSpeed()));
        tvPrecisao.setText(String.format("%.2f metros", data.getAccuracy()));

        // move camera para o ponto atual
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.getCurrentLocation().getLatitude(), data.getCurrentLocation().getLongitude()), 18f));

        // desenha a rota já percorrida no mapa
        drawPolyline();
    }

    private void drawPolyline() {
        options.add(new LatLng(data.getCurrentLocation().getLatitude(), data.getCurrentLocation().getLongitude()));
        tracking.add(mMap.addPolyline(options));
    }
}
