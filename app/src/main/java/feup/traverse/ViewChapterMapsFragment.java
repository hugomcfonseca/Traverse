package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Handler;

/**
 * @author hugof
 * @date 29/12/2015.
 */

// TODO: If user gets destination, update his progress on local DB and
public class ViewChapterMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GetMyLocation getLocation;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Button btn_viewChapterMaps_getLocation, btn_viewChapterMaps_startTraveling;
    private TextView tv_viewchapterMapsLocalName;

    private Cursor cursor;

    final int phase_number = 1;

    Handler handler_getPosition = new Handler();
    Handler handler_timerToCheckDestination = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = (View) inflater.inflate(R.layout.fragment_viewchapter_maps, null, false);
        cursor = ((ViewChapter) getActivity()).dataBaseAdapter.getCoordinatesAndLocal(
                ((ViewChapter) getActivity()).value, ((ViewChapter) getActivity()).session.getusername());

        btn_viewChapterMaps_getLocation = (Button)view.findViewById(R.id.btn_viewchapter_maps_getlocation);
        btn_viewChapterMaps_startTraveling = (Button)view.findViewById(R.id.btn_viewchapter_map_start);
        tv_viewchapterMapsLocalName = (TextView)view.findViewById(R.id.tv_maps_place);

        tv_viewchapterMapsLocalName.setText(cursor.getString(cursor.getColumnIndex("local")));

        initializeMap();
        getLocation = GetMyLocation.getInstance();
        getLocation.canGetLocation();

        btn_viewChapterMaps_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handler_getPosition.post(getting_userPosition);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            SupportMapFragment fragment = (SupportMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(
                            R.id.map);
            if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();

            handler_getPosition.removeCallbacks(getting_userPosition);
            getLocation.stopUsingGPS();

        } catch (IllegalStateException e) {
            //handle this situation because you are necessary will get
            //an exception here :-(
        }
    }

    private void initializeMap() {
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }
        if (mSupportMapFragment != null)
        {
            mSupportMapFragment.getMapAsync(this);
            if (mMap != null)
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(LatLng point)
                    {
                        //TODO: your onclick stuffs
                    }
                });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Cursor cursor = ((ViewChapter) getActivity()).dataBaseAdapter.getCoordinatesAndLocal(
                        ((ViewChapter) getActivity()).value,((ViewChapter) getActivity()).session.getusername());
        mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(cursor.getDouble(cursor.getColumnIndex("latitude")), cursor.getDouble(cursor.getColumnIndex("longitude"))))
                        .title(cursor.getString(cursor.getColumnIndex("local"))));
    }

    private boolean checkUserPosition (double req_lat, double req_long, double my_lat, double my_long) {

        float[] results = new float[1];
        Location.distanceBetween(req_lat, req_long,
                my_lat, my_long, results);

        if (results[0] <= 30){
            handler_getPosition.removeCallbacks(getting_userPosition);
            return true;
        } else
           return false;
    }

    private Runnable getting_userPosition = new Runnable() {
        @Override
        public void run() {

            handler_getPosition.postDelayed(getting_userPosition, 5*1000*60);

            getLocation.getLocation(getContext());
            mMap.clear();

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(cursor.getDouble(cursor.getColumnIndex("latitude")), cursor.getDouble(cursor.getColumnIndex("longitude"))))
                    .title(cursor.getString(cursor.getColumnIndex("local"))));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(getLocation.getLatitude(), getLocation.getLongitude()))
                    .title("You are here!"));

            if (checkUserPosition(cursor.getDouble(cursor.getColumnIndex("latitude")), cursor.getDouble(cursor.getColumnIndex("longitude")),
                    getLocation.getLatitude(), getLocation.getLongitude())) {

                alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Phase " + phase_number + " finished!");
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ViewChapter) getActivity()).flag = 1;
                                ((ViewChapter) getActivity()).finished = 1;
                                ((ViewChapter) getActivity()).mPagerAdapter = new ViewChapterTextPagerAdapter(getActivity().getSupportFragmentManager(),1);
                                ((ViewChapter) getActivity()).mPager.setAdapter(((ViewChapter) getActivity()).mPagerAdapter);
                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
                alertDialog.show();
            }
        }
    };

    private Runnable timer_CheckDestination = new Runnable() {
        @Override
        public void run() {

        }
    };

}
