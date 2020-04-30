package mario.com.br.velocita.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import mario.com.br.velocita.R;

public class Maps {

    public static float BROWSER_TILT = 0;
    public static float NAVIGATION_TILT = 90;

    public static LatLng toLatLng(Location location) {

        if (location != null)
            return new LatLng(location.getLatitude(), location.getLongitude());

        return null;
    }

    public static Bitmap createBitmapDefaultMarker(Context context, int colorRes) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_marker_default, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setColorFilter(colorRes);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();

        if (drawable != null)
            drawable.draw(canvas);

        customMarkerView.draw(canvas);

        return returnedBitmap;
    }

    public static Bitmap createBitmapProfilePictureMarker(Context context, @DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_marker_profile_picture, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();

        if (drawable != null)
            drawable.draw(canvas);

        customMarkerView.draw(canvas);

        return returnedBitmap;
    }

    public static Bitmap createBitmapDefaultMarkerProfile(Context context, int colorRes) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_marker_default_profile_picture, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setColorFilter(colorRes);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();

        if (drawable != null)
            drawable.draw(canvas);

        customMarkerView.draw(canvas);

        return returnedBitmap;
    }

    public static Polyline addProgressPolyline(GoogleMap gMap, LatLng p1, LatLng p2, int color) {
        return gMap.addPolyline(new PolylineOptions().add(p1, p2).geodesic(true).width(12).color(color));
    }

    public static void moveCamera(GoogleMap gMap, LatLng latLng, float bearing, float tilt) {
        if (latLng != null)
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 20, tilt, bearing)));
    }

}
