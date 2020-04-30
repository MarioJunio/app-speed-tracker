package mario.com.br.velocita.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class Permissions {

    public static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    public static boolean hasFineLocation(AppCompatActivity activityCompat) {
        return ActivityCompat.checkSelfPermission(activityCompat, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkAndRequestFineLocation(AppCompatActivity activityCompat) {
        boolean hasPermission = hasFineLocation(activityCompat);

        // se não tiver essa permissão ela será requisitada
        if (!hasPermission)
            ActivityCompat.requestPermissions(activityCompat, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);

            /*ActivityCompat.shouldShowRequestPermissionRationale(activityCompat, Manifest.permission.ACCESS_FINE_LOCATION);

            final AlertDialog.Builder builder = new AlertDialog.Builder(activityCompat);
            builder.setCancelable(false);
            builder.setMessage(R.string.request_progress_location);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();*/

        return hasPermission;
    }


}
