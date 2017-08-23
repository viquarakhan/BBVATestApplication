package viquar.com.bbvatestapplication.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import viquar.com.bbvatestapplication.BuildConfig;
import viquar.com.bbvatestapplication.R;
import viquar.com.bbvatestapplication.activities.DetailsActivity;
import viquar.com.bbvatestapplication.model.BankLocation;
import viquar.com.bbvatestapplication.utils.Communicator;
import viquar.com.bbvatestapplication.utils.HttpHandler;

public class MapFragment extends Fragment implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback
        ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    View itemView;
    Intent intent;
    Communicator communicator;
    ArrayList<Marker> markerArrayList;
    public static final String RESULTS = "results";
    public static final String ADDRESS = "formatted_address";
    public static final String GEOMETRY = "geometry";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String NAME = "name";
    //public static String url = "http://10.0.2.2/bbva.txt";
    public static String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=BBVA+Compass&location=MY_LAT,MY_LONG&radius=10000&key=AIzaSyAvKrEb_--kZrgKo21BgO-twAxG1VBFw4I";
    ArrayList<BankLocation> bankLocationsArrayList = new ArrayList<>();
    private GoogleMap mMap;
    private MapView mMapView;
    private SupportMapFragment mSupportMapFragment;
    private BankLocation bankLocation;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private double mLatitude,mLongitude;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView=inflater.inflate(R.layout.fragment_map, container, false);
        mMapView=itemView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        new JSONParser(getContext()).execute();


        return itemView;
    }// End of onCreate();
    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            mGoogleApiClient.connect();
        }
        Log.i("TAG","onstart");
    }
    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
                mPermission);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        requestPermissions(new String[]{mPermission},REQUEST_CODE_PERMISSION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        mPermission);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("TAG", "Displaying permission rationale to provide additional context.");

            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage(R.string.permission_denied_explanation);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           startLocationPermissionRequest();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            Log.i("TAG", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("TAG", "onRequestPermissionResult");
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("TAG", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Log.i("TAG", "Granted");
                mMap.setMyLocationEnabled(true);
                //getLocation();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage(R.string.permission_denied_explanation);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {


        communicator.passList(bankLocationsArrayList);
        Log.i("TAG","onConnected");
    }

    public void populateMap(){
        if (mMap != null) {
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
            }
            Log.i("TAG","1");
            LatLng currentLocation = new LatLng(mLatitude,mLongitude);
            mMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,7f));
            markerArrayList=new ArrayList<>();
            for(int i=0;i<bankLocationsArrayList.size();i++){
                LatLng object = new LatLng(bankLocationsArrayList.get(i).getLatitude(), bankLocationsArrayList.get(i).getLongitude());
                Marker m = mMap.addMarker(new MarkerOptions().position(object)
                        .title(bankLocationsArrayList.get(i).getName())
                        .snippet("Click for more information")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bankmarker)));
                markerArrayList.add(m);
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(object));
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Activity activity = getActivity();
                    if (isAdded() && activity != null) {
                        intent = new Intent(getActivity(), DetailsActivity.class);
                        for (int i = 0; i < markerArrayList.size(); i++) {
                            if (marker.equals(markerArrayList.get(i))) {
                                Bundle object = new Bundle();
                                object.putParcelableArrayList("mapkey",bankLocationsArrayList);
                                object.putInt("key", i);
                                intent.putExtras(object);
                            }
                        }
                        startActivity(intent);
                    }
                }
            });
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TAG","onConnectionSuspended");

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       Log.i("TAG","onConnectionFailed");
    }
    public class JSONParser extends AsyncTask<Void,Void,Void> {
        Context context;
        public JSONParser(Context context){
            this.context=context;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = httpHandler.makeServiceCall(url);

            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray(RESULTS);
                    Log.e("TAG", "results array: " + results.toString());

                    // looping through the List Array
                    for (int i = 0; i < results.length(); i++) {
                        bankLocation=new BankLocation();
                        JSONObject centralList = results.getJSONObject(i);
                        Log.e("TAG", "centralList: " + centralList.toString());
                        //JSON object address
                        String address = centralList.getString(ADDRESS);
                        bankLocation.setAddress(address);
                        Log.e("TAG","address"+address);
                        //JSON object name
                        String name = centralList.getString(NAME);
                        bankLocation.setName(name);
                        // JSON object Geometry
                        JSONObject geometry = centralList.getJSONObject(GEOMETRY);
                        JSONObject location = geometry.getJSONObject(LOCATION);
                        double latitude=location.getDouble(LATITUDE);
                        bankLocation.setLatitude(latitude);
                        double longitude=location.getDouble(LONGITUDE);
                        bankLocation.setLongitude(longitude);

                        bankLocationsArrayList.add(bankLocation);
                    }
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e("TAG", "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           /* Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("bankkey",bankLocationsArrayList);
            fragment_map.setArguments(bundle);*/
            populateMap();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator= (Communicator) getActivity();
    }
}
