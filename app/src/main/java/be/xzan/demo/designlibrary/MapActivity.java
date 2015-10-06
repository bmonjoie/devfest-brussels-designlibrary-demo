package be.xzan.demo.designlibrary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import be.xzan.demo.designlibrary.common.activities.DrawerActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created on 29/09/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public class MapActivity extends DrawerActivity implements OnMapReadyCallback {

    public static final double FAMOCO_LATITUDE = 50.8400214;
    public static final double FAMOCO_LONGITUDE = 4.3467262;
    public static final int DEFAULT_ZOOM = 13;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private GoogleMap mMap;
    private MapFragment mFragment;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setCheckedItem(R.id.menu_map);
        mFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_map;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        setupMap();
    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (mFragment != null && mFragment.getView() != null) {
                    Snackbar.make(mFragment.getView(), R.string.permission_location_explanation, Snackbar.LENGTH_LONG)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermission();
                                }
                            }).show();
                }
            } else {
                requestPermission();
            }

        } else {
            LatLng latLng = new LatLng(FAMOCO_LATITUDE, FAMOCO_LONGITUDE);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Famoco/Ateliers des Tanneurs")
            );
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_CODE && resultCode == RESULT_OK) {
            setupMap();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
