package feup.traverse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterMapsFragment extends SupportMapFragment implements OnMapReadyCallback {


    private GoogleMap mMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("onCreateView");

        View view = super.onCreateView(inflater, container, savedInstanceState);

        getMapAsync(this);


        return view;
    }


    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //SEARCH FOR COORD
                for(int i=0; i<sampleMatrix.size(); i++){
                    if(sampleMatrix.get(i).get(0).coord.toString().equals( marker.getPosition().toString() )) {
                        ((ViewData) getActivity()).selectMarker(i, true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        break;
                    }
                }
                return true;
            }
        });*/

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ((ViewData) getActivity()).unselectMarker();
                return;
            }
        });*/



        /*if(sampleMatrix!=null && sampleMatrix.size()!=0 && markerList!=null) {
            for (int i = 0; i < sampleMatrix.size(); i++) {
                switch (sampleMatrix.get(i).get(sampleMatrix.get(i).size() - 1).warning_level) {
                    case 0:
                        color = BitmapDescriptorFactory.HUE_GREEN;
                        break;
                    case 1:
                        color = BitmapDescriptorFactory.HUE_YELLOW;
                        break;
                    case 2:
                        color = BitmapDescriptorFactory.HUE_RED;
                        break;
                }
                markerList.add(mMap.addMarker(new MarkerOptions().position(sampleMatrix.get(i).get(0).coord).title("Selected").icon(BitmapDescriptorFactory.defaultMarker(color))));
            }

            mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sampleMatrix.get(0).get(0).coord));

            //((ViewData) getActivity()).restoreMarker();*/

    }

}
