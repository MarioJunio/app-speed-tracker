package mario.com.br.velocita.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import mario.com.br.velocita.dialog.AppDialog;

public class SmartLocation {

    private static LocationManager locationManager;
    private static LocationListener locationListener;

    public static boolean isGPSEnabled(Context context) {
        initLocationManager(context);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void enableGPS(final AppCompatActivity context) {

        if (!isGPSEnabled(context)) {
            AppDialog.showDialog(context, null, "Para continuar, permita que o dispositivo ative a localização utilizando o GPS.", "Ativar", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivityForResult(intent, 1);
                }
            }, null);
        }
    }

    public static void disableGPS(Context context) {

        if (isGPSEnabled(context)) {
        }
    }

    @SuppressLint("MissingPermission")
    public static Location getLastLocation(Context context) {
        initLocationManager(context);

        return locationManager.getLastKnownLocation(locationManager.getBestProvider(createCriteria(), false));
    }

    @SuppressLint("MissingPermission")
    public static void start(Context context, LocationListener listener) {
        initLocationManager(context);

        locationManager.requestLocationUpdates(500l, 0f, createCriteria(), listener, context.getMainLooper());

        locationListener = listener;
    }

    public static void stop() {
        locationManager.removeUpdates(locationListener);
    }

    private static Criteria createCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(true);

        return criteria;
    }

    private static void initLocationManager(Context context) {

        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

}
