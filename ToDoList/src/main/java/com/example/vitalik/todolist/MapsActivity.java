package com.example.vitalik.todolist;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.vitalik.myapplication.R;
import com.example.vitalik.todolist.database.DBContract;
import com.example.vitalik.todolist.database.DBShow;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DBShow shower = new DBShow();
        Cursor cursor = shower.getAllData(MainActivity.mSqLiteDatabase);

        String title;
        double lat, lng;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            title = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Columns.TITLE));
            lat = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DBContract.Columns.LAT));
            lng = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DBContract.Columns.LNG));

            LatLng current = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(current).title(title));
            cursor.moveToNext();
        }
    }
}
