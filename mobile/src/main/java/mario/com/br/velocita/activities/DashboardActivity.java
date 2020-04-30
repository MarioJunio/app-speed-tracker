package mario.com.br.velocita.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.clustering.ClusterManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mario.com.br.velocita.R;
import mario.com.br.velocita.adapters.InfoWindowStatisticsMarkerAdapter;
import mario.com.br.velocita.cluster.ProgressClusterItem;
import mario.com.br.velocita.cluster.ProgressClusterRenderer;
import mario.com.br.velocita.holder.Data;
import mario.com.br.velocita.holder.Indicator;
import mario.com.br.velocita.holder.data.TimeObserverBus;
import mario.com.br.velocita.holder.view.DashboardView;
import mario.com.br.velocita.holder.view.ToolbarIndicatorNormal;
import mario.com.br.velocita.holder.view.ToolbarIndicatorProgress;
import mario.com.br.velocita.services.GpsService;
import mario.com.br.velocita.services.Permissions;
import mario.com.br.velocita.services.ProgressTimer;
import mario.com.br.velocita.services.TimeObserver;
import mario.com.br.velocita.utils.Dates;
import mario.com.br.velocita.utils.Maps;
import mario.com.br.velocita.utils.SmartLocation;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback, Data.OnGpsServiceUpdate {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ToolbarIndicatorNormal toolbarIndicatorNormal;
    private ToolbarIndicatorProgress toolbarIndicatorProgress;
    private DashboardView dashboardView;
    public static Data data;

    private ClusterManager<ProgressClusterItem> clusterManager;
    private InfoWindowStatisticsMarkerAdapter statisticsMarkerAdapter;
    private ProgressClusterRenderer progressClusterRenderer;

    private EventBus eventBus = EventBus.getDefault();
    private TimeObserver timeObserver = new TimeObserver();

    private List<Polyline> progressRoute = new ArrayList<>();
    private ProgressTimer progressTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        data = new Data(this);

        // create adapters
        statisticsMarkerAdapter = new InfoWindowStatisticsMarkerAdapter(this);

        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // cria holder para manipular as informaçãoes do Toolbar
        toolbarIndicatorNormal = new ToolbarIndicatorNormal(findViewById(R.id.view_indicator_normal));
        toolbarIndicatorProgress = new ToolbarIndicatorProgress(findViewById(R.id.view_indicator_progress));
        dashboardView = new DashboardView(findViewById(R.id.view_map_cockpit));

        // carrega o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // instancia o cronometro
        progressTimer = ProgressTimer.newInstance(toolbarIndicatorProgress);

        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * Ao tenta criar um novo progresso
     *
     * @param view
     */
    public void onStartProgress(View view) {
        //TODO: Aguarda GPS ser iniciado e obter a primeira localização
        //TODO: Após isso iniciar contador de 5 ou 3 segundos para iniciar o progresso

        data.setCurrentIndicator(Indicator.PROGRESS);

        // se a permissão já foi concedida então o progresso será iniciado
        if (Permissions.checkAndRequestFineLocation(this))
            enableProgress();
    }

    /**
     * Ao tentar encerrar o progresso atual
     *
     * @param view
     */
    public void onCancelProgress(View view) {
        cancelProgress();
    }

    /**
     * Ao terminar o progresso os gadgets serão pausados e será exibido um botão para o usuário postar seu progresso no mapa
     *
     * @param view
     */
    public void onDoneProgress(View view) {
        if (data.isProgress()) {
            data.setCurrentIndicator(Indicator.PENDING_PROGRESS);

            stopService(new Intent(getBaseContext(), GpsService.class));

            // tempo de finalização do progresso
            data.setEndTime(System.currentTimeMillis());

            data.setEndMarker(createEndProgressMarker(data.getCurrentLocation(), false, getResources().getColor(R.color.hue_azure)));
            clusterManager.cluster();

            // atualiza a perspectiva do mapa
            Maps.moveCamera(dashboardView.gMap, Maps.toLatLng(data.getLastLocation()), data.getBearing(), Maps.BROWSER_TILT);

            dashboardView.showPostProgressGadgets();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableProgress() {

        // se ainda não estiver em progresso
        if (data.isProgress()) {

            dashboardView.gMap.setMyLocationEnabled(true);

            // ativa GPS se não estiver ativo
            SmartLocation.enableGPS(this);

            // se o GPS está ativo inicie, se não solicite a ativação
            if (SmartLocation.isGPSEnabled(getBaseContext())) {

                // remove marcadores atuais
                clearPolylines();

                // inicia serviço de GPS
                startService(new Intent(getBaseContext(), GpsService.class));

                // atualiza a view para modo progresso
                toolbarIndicatorNormal.hide();
                toolbarIndicatorProgress.show();
                dashboardView.startProgressState();

                // inicia cronometro
                progressTimer.start();

                // tempo de inicio do progresso
                data.setStartTime(data.getCurrentTime());
            }
        }
    }

    private void clearPolylines() {

        for (Polyline p : progressRoute)
            p.remove();

        progressRoute.clear();
    }

    private void cancelProgress() {

        // será executado apenas se estiver em progresso
        if (data.isProgress() || data.isPendingProgress()) {

            // para o serviço de GPS
            stopService(new Intent(getBaseContext(), GpsService.class));

            // apaga a rota que o usuário fez
            clearPolylines();

            // se algum marcador foi posicionado remove-o
            if (data.getStartMarker() != null)
                clusterManager.removeItem(data.getStartMarker());

            // se o marcador final foi posicionado remova-o
            if (data.getEndMarker() != null)
                clusterManager.removeItem(data.getEndMarker());

            // atualiza os clusters
            clusterManager.cluster();

            // atualiza a perspectiva do mapa se tiver sido alterada
            if (data.getLastLocation() != null)
                Maps.moveCamera(dashboardView.gMap, Maps.toLatLng(data.getLastLocation()), data.getBearing(), Maps.BROWSER_TILT);

            // esconder os indicadores do progresso
            toolbarIndicatorProgress.hide();
            toolbarIndicatorNormal.show();
            dashboardView.hideProgressGadgets();

            // reinicia o modulo de dados do progresso atual
            data.reset();
        }

    }

    public void onShowPerfil(View view) {
        Toast.makeText(this, "Exibir perfil", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_map:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        eventBus.register(this);

        dashboardView.setupGoogleMap(googleMap);
        dashboardView.updateMapStyle(this, !Dates.isNight() ? TimeObserver.TimeHour.DAY : TimeObserver.TimeHour.NIGHT);

        // executa tarefa assincrona para monitorar se é dia ou noite
        timeObserver.execute();

        // create cluster manager
        clusterManager = new ClusterManager<>(this, googleMap);
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(statisticsMarkerAdapter);

        // create cluster renderer
        progressClusterRenderer = new ProgressClusterRenderer(this, googleMap, clusterManager);

        clusterManager.setRenderer(progressClusterRenderer);
        clusterManager.setOnClusterItemClickListener(progressClusterRenderer);

        // set info window adapter on maps
        dashboardView.gMap.setInfoWindowAdapter(clusterManager.getMarkerManager());

        // set camera idle listener
        dashboardView.gMap.setOnCameraIdleListener(clusterManager);

        // set marker click listener
        dashboardView.gMap.setOnMarkerClickListener(clusterManager);

        if (Permissions.hasFineLocation(this)) {
            dashboardView.gMap.setMyLocationEnabled(true);
            Maps.moveCamera(googleMap, Maps.toLatLng(SmartLocation.getLastLocation(this)), data.getBearing(), Maps.BROWSER_TILT);
        }
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        timeObserver.cancel(true);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(final TimeObserverBus timeObserverBus) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dashboardView.updateMapStyle(getApplicationContext(), timeObserverBus.getTimeHour());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Permissions.PERMISSION_ACCESS_FINE_LOCATION: {

                // se a permissão foi concedida
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enableProgress();
            }

        }
    }

    @Override
    public void update() {
        // se estiver em progresso atualiza os indicadores
        if (data.isProgress()) {
            dashboardView.speedometer.speedTo(data.getSpeed(), 0);
            dashboardView.progressDistance.countAnimation(Integer.valueOf(dashboardView.progressDistance.getText().toString()), (int) data.getDistance());

            // atualiza marcador
            if (!data.hasStartMarker()) {
                data.setStartMarker(createStartProgressMarker(data.getCurrentLocation(), getResources().getColor(R.color.hue_azure)));
                clusterManager.cluster();
            } else
                progressRoute.add(Maps.addProgressPolyline(dashboardView.gMap, Maps.toLatLng(data.getLastLocation()), Maps.toLatLng(data.getCurrentLocation()), getResources().getColor(R.color.hue_azure)));

            // atualiza camera para o local atual do gps
            Maps.moveCamera(dashboardView.gMap, Maps.toLatLng(data.getCurrentLocation()), data.getBearing(), Maps.NAVIGATION_TILT);

            if (data.getDistance() > 5)
                dashboardView.btnDoneProgress.setVisibility(View.VISIBLE);
        }

    }

    private ProgressClusterItem createStartProgressMarker(Location location, int color) {
        ProgressClusterItem item = new ProgressClusterItem(new ProgressClusterItem.Data(Maps.toLatLng(location), false, color), ProgressClusterItem.Type.START);
        clusterManager.addItem(item);

        return item;
    }

    private ProgressClusterItem createEndProgressMarker(Location location, boolean hasPicture, int res) {
        ProgressClusterItem item = new ProgressClusterItem(new ProgressClusterItem.Data(Maps.toLatLng(location), hasPicture, res), ProgressClusterItem.Type.END);
        item.data.username = "Mario Marques"; //TODO: Substituir
        item.data.distanciaPercorrida = data.getDistance();
        item.data.tempoGasto = data.getTotalElapsedTime();
        item.data.velocidadeMaxima = data.getMaxSpeed();
        item.data.unidadeVelocidade = "km/h"; //TODO: Substituir
        item.data.mensagemStatus = "rota finalizada"; //TODO: Substituir

        clusterManager.addItem(item);

        return item;
    }

}
