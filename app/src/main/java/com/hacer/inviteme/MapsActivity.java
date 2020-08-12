package com.hacer.inviteme;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hacer.inviteme.Helper.GPSTracker;
import com.hacer.inviteme.Helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    double lat,lon;
    @BindView(R.id.btnShareLocation)
    Button btnShare;


    private double radiusMeters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);

        //Check internet connection
        if (!Utils.isNetworkConnected(MapsActivity.this)) {
            new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Bağlantı hatası")
                    .setContentText("İnternet bağlantısı yok, lütfen telefonunuzun aktif bir internet bağlantısı olduğunu kontrol ediniz")
                    .show();
            return;
        }
        //Ask access location permission
        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Get Location
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        Location location = gpsTracker.getLocation();
        if(location != null){
            Toast.makeText(getApplicationContext(),"Latitude: "+location.getLatitude()+ ", Longitude:  "+location.getLongitude(),Toast.LENGTH_LONG).show();
            // Move the camera to the location
             LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
             mMap.addMarker(new MarkerOptions().position(loc).title("Marker in currenct location"));
             mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
             lat = location.getLatitude();
             lon = location.getLongitude();
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                LatLng loc = new LatLng(arg0.latitude, arg0.longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in selected location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                lat = arg0.latitude;
                lon = arg0.longitude;
            }
        });
    }

    @OnClick(R.id.btnShareLocation)  void shareLocation(){
        String whatsAppMessage = "http://maps.google.com/maps?saddr=" + lat + "," + lon;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }


}