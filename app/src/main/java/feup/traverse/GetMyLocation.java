package feup.traverse;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * @author hugof
 * @date 30/12/2015.
 */


public class GetMyLocation extends Service implements LocationListener {
    /**
     * ATTRIBUTES
     */
    private static final String TAG = GetMyLocation.class.getSimpleName();
    private Context context;
    private static GetMyLocation instance;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;                                // meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 20000;                    // 20seconds
    // Declaring a Location Manager
    protected LocationManager locationManager;
    /**
     * PUBLIC ATTRIBUTES
     */
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    public Location location;
    double latitude;
    double longitude;

    public GetMyLocation() {

        context = null;
    }

    public static GetMyLocation getInstance() {

        if (instance == null) {
            instance = new GetMyLocation();
        }
        return instance;
    }

    public void setContext(Context context) {

        this.context = context;
    }

    public Location getLocation(Context context) {

        this.context = context;
        try {
            locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                showSettingsAlert("Location Settings", "Location Services are disable. Please, enable them.");
            }
            else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

            }
        }
        catch (Exception e) {
            // do nothing
        }
        return location;
    }

    public void stopUsingGPS() {

        if (locationManager != null) {
            locationManager.removeUpdates(GetMyLocation.this);
        }
    }

    public double getLatitude() {

        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {

        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {

        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert(String title, String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        // Settings button
        alertDialog.setPositiveButton("Change It", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        // cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        // show
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }
}