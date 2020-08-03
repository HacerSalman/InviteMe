package com.hacer.inviteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.hacer.inviteme.Helper.GPSTracker;
import com.hacer.inviteme.Helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnGetLocation)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnGetLocation) void getLocation() {
        //Check internet connection
        if (!Utils.isNetworkConnected(MainActivity.this)) {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Bağlantı hatası")
                    .setContentText("İnternet bağlantısı yok, lütfen telefonunuzun aktif bir internet bağlantısı olduğunu kontrol ediniz")
                    .show();
            return;
        }
        //Ask access location permission
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        //Get Location
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        Location location = gpsTracker.getLocation();
        if(location != null){
            Toast.makeText(getApplicationContext(),"Latitude: "+location.getLatitude()+ ", Longitude:  "+location.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }
}