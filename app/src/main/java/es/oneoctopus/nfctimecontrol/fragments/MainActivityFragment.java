package es.oneoctopus.nfctimecontrol.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import es.oneoctopus.nfctimecontrol.R;
import es.oneoctopus.nfctimecontrol.other.Constants;


public class MainActivityFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap map;
    private SupportMapFragment mapView;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermissions();
    }

    /**
     * Check if the application has the permission to access the user location since
     * Marshmallow requires each permission to be explicitly approved.
     */
    private void getLocationPermissions() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.PERMISSION_LOCATION);
        } else
            getLastKnownLocation();
    }

    /**
     * Returns the user response about the permissions.
     *
     * @param requestCode the code that identifies the permission request
     * @param permissions the permissions that are referred in the request
     * @param grantResults results of the request to the user
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLastKnownLocation();
            else
                disableLocation();
        }

    }

    private void disableLocation() {

    }

    private void getLastKnownLocation() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
