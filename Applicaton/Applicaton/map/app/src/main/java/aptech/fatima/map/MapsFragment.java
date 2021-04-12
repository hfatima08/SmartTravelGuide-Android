package aptech.fatima.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MapsFragment extends Fragment {
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    TextView text;
    private GoogleMap map;
    SearchView searchView;
    LatLng source,destination;
    Marker marker;
    Polyline polyline1;
    String sdest;
   public String loc;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            client = LocationServices.getFusedLocationProviderClient(getActivity());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                sdest =getArguments().getString("data");
                if(sdest!=null) {
                    getDestination(sdest);
                    getDistance(source,destination);
                }
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        loc = searchView.getQuery().toString();
                        getDestination(loc);
                        if (source != null && destination != null) {
                            getDistance(source, destination);
                        } else {
                            Toast.makeText(getContext(), " Invalid Current Location or Destination", Toast.LENGTH_SHORT).show();
                        }


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
    }};



    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Open your device location and reopen the app", Toast.LENGTH_SHORT).show();
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    source = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions options = new MarkerOptions();
                    options.position(source).title("Your here!");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 10));
                    map.addMarker(options);

                }
            }
        });

    }
        public void getDestination(String dest){
        List<Address> addressList = null;
        if (dest != null || !dest.equals("")) {
        Intent intent = new Intent("pass_data").putExtra("desti", dest);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        Geocoder geocoder = new Geocoder(getContext());
        try {
        addressList = geocoder.getFromLocationName(dest, 1);
        } catch (IOException e) {
        e.printStackTrace();
        }
        if (!addressList.isEmpty()) {
        Address address = addressList.get(0);
        destination = new LatLng(address.getLatitude(), address.getLongitude());
        if (marker == null) {
        marker = map.addMarker(new MarkerOptions().position(destination).title(dest));
        } else if (marker != null) {
        marker.remove();
        marker = map.addMarker(new MarkerOptions().position(destination).title(dest));
        }
        map.animateCamera(CameraUpdateFactory.newLatLng(destination));
        }  } else {
            Toast.makeText(getContext(), "Enter a Country/City Name", Toast.LENGTH_SHORT).show();
        }
        }

    public void getDistance(LatLng source, LatLng destination) {
        if(polyline1== null ) {
            polyline1 = map.addPolyline(new PolylineOptions()
                    .clickable(false)
                    .add(source,destination));
            polyline1.setWidth(6);
            polyline1.setColor(0xff000000);}
        else if(polyline1!=null){
            polyline1.remove();
            polyline1 = map.addPolyline(new PolylineOptions()
                    .clickable(false)
                    .add(source,destination));
            polyline1.setWidth(6);
            polyline1.setColor(0xff000000);}

        Location l1 = new Location("One");
        l1.setLatitude(source.latitude);
        l1.setLongitude(source.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(destination.latitude);
        l2.setLongitude(destination.longitude);

        float distance = l1.distanceTo(l2);
        String dist = distance + " M";

        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = distance + " Km";
            text.setVisibility(View.VISIBLE);
            text.setText(" Total Distance:\n"+dist);
        }
    }

//   ********* Direction/Route Code Only Implements on a Paid Api ************************
//    private void DisplayTrack(LatLng source, LatLng destination) {
//        try{
//            Uri uri=Uri.parse("https://www.google.co.in/maps/dur/"+source+"/"+destination);
//            Intent intent =new Intent(Intent.ACTION_VIEW,uri);
//            intent.setPackage("com.google.android.apps.maps");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//
//
//        } catch (ActivityNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "not worked", Toast.LENGTH_SHORT).show();
//        }
//   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        searchView=view.findViewById(R.id.search);
        text=view.findViewById(R.id.dist);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}