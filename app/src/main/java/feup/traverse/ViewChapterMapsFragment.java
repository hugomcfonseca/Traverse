package feup.traverse;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterMapsFragment extends SupportMapFragment implements OnMapReadyCallback{


    private GoogleMap mMap;

    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = (View) inflater.inflate(R.layout.fragment_viewchapter_maps, null, false);
        initializeMap();

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

        } catch (IllegalStateException e) {
            //handle this situation because you are necessary will get
            //an exception here :-(
        }
    }

    private void initializeMap()
    {
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

    }
}
