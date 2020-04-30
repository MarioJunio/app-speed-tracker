package mario.com.br.velocita.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import mario.com.br.velocita.activities.DashboardActivity;
import mario.com.br.velocita.holder.Data;
import mario.com.br.velocita.utils.Formulas;
import mario.com.br.velocita.utils.SmartLocation;

public class GpsService extends Service implements LocationListener {

    @Override
    public void onCreate() {
        SmartLocation.start(this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Data data = DashboardActivity.data;

        // se está em progresso captura essa localização atual
        if (data.isProgress()) {
            data.setCurrentTime(System.currentTimeMillis());
            data.setCurrentLocation(location);

            // se for a primeira vez que está obtendo a localização devemos setar as variáveis para cálculos futuros
            if (data.isFirst()) {
                data.setStartTime(data.getCurrentTime());
                data.setLastLocation(data.getCurrentLocation());
                data.setFirst(false);
            } else
                // calcula o tempo que se passou entre os 2 pontos
                data.elapsedTime();

            // ultimo tempo será igual ao tempo atual
            data.setLastTime(data.getCurrentTime());

            // calcula a distancia em metros entre os 2 últimos localizações capturadas
            // Calcular a velocidade: (distancia(m) / tempo(s)) = m/s
            double distance = data.getLastLocation().distanceTo(data.getCurrentLocation());

            // rotação entre as 2 últimas localizações
            data.setBearing(data.getLastLocation().bearingTo(data.getCurrentLocation()));

            // se a precisão em metros for menos que a distância
//            if (location.getAccuracy() < distance) {
                data.addDistance(distance);
//                data.setLastLocation(data.getCurrentLocation());
//                data.setLastTime(System.currentTimeMillis());
//            }

            // se tem velocidade já calculada iremos usa-la, se não iremos calcula-lá
            if (location.hasSpeed()) {
                data.setSpeed(Formulas.toKMH(location.getSpeed()));
                data.setMaxSpeed((data.getSpeed() > data.getMaxSpeed() ? data.getSpeed() : data.getMaxSpeed()));
            } else
                data.setSpeed(Formulas.speedKmH(distance, data.getElapsedTime() / 1000d));

            // calcula a velocidade média
            data.setAvgSpeed(Formulas.speedKmH(data.getDistance(), ((data.getEndTime() == 0 ? data.getCurrentTime() : data.getEndTime()) - data.getStartTime()) / 1000d));

            // se a precisão foi informada
            if (location.hasAccuracy())
                data.setAccuracy(location.getAccuracy());

            // atualiza a view
            data.update();

            data.setLastLocation(data.getCurrentLocation());
            data.setLastTime(System.currentTimeMillis());
        }
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
}
