package com.omninos.petrowagon.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omninos.petrowagon.ModelClass.CheckDistanceModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.CommonUtils;
import com.omninos.petrowagon.SharePreference.PinAddress;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import bolts.Task;

public class PickupLocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    GoogleMap map;
    SupportMapFragment picupLocationMap;
    final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private ImageView pin;
    private TextView address_Et;
    private LatLng mCenterLatLong;
    private String pinLocation_1;
    private LocationManager locationManager;
    private GoogleApiClient client;
    private RelativeLayout currenLocationBtn;
    private String address;
    private double lati, longi;
    private Button BtnOkLocation, BtnOkDistance;
    private OrderViewModel viewModel;
    private Activity activity;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_location);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        findIds();
        picupLocationMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.picupLocationMap);
        picupLocationMap.getMapAsync(this);
    }

    private void findIds() {

        activity = PickupLocationActivity.this;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pin = findViewById(R.id.pin);
        address_Et = findViewById(R.id.address_Et);
        BtnOkLocation = findViewById(R.id.BtnOkLocation);
        BtnOkLocation.setOnClickListener(this);
        address_Et.setOnClickListener(this);
        currenLocationBtn = findViewById(R.id.currenLocationBtn);
        currenLocationBtn.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        GetLocation();

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mCenterLatLong = cameraPosition.target;
                map.clear();

                try {

                    Location mLocation = new Location("");
                    address_Et.setText(PinAddress.getAddress(PickupLocationActivity.this, mCenterLatLong.latitude, mCenterLatLong.longitude));

                    lati = mLocation.getLatitude();
                    longi = mLocation.getLongitude();
                    address = (String) address_Et.getText();
//                    pinLocation_1 = address_Et.getText().toString().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();

                App.getSinltonPojo().setCurrentLat(String.valueOf(latLng.latitude));
                App.getSinltonPojo().setCurrentLong(String.valueOf(latLng.longitude));
                lati = latLng.latitude;
                longi = latLng.longitude;
                address = place.getAddress();
                gotoLocation(lati, longi);
                address_Et.setText(address);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                        String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    App.getSinltonPojo().setmCity(city);
                    App.getSinltonPojo().setmState(state);
                    App.getSinltonPojo().setmCountry(country);
                    App.getSinltonPojo().setmZipCode(postalCode);

                    address = addresses.get(0).getAddressLine(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("Place Name", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Status", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void checkDistance() {
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();

        viewModel.checkDistance(activity, String.valueOf(App.getSinltonPojo().getCurrentLat()), String.valueOf(App.getSinltonPojo().getCurrentLong()), userId, address).observe(PickupLocationActivity.this, new Observer<CheckDistanceModel>() {
            @Override
            public void onChanged(CheckDistanceModel checkDistanceModel) {
                if (checkDistanceModel.getSuccess().equalsIgnoreCase("1")) {
                    address_Et.setText(address);
                    App.getSinltonPojo().setCurrentLat(String.valueOf(lati));
                    App.getSinltonPojo().setCurrentLong(String.valueOf(longi));
                    address = (String) address_Et.getText();
                    App.getSinltonPojo().setDropAddress(address);
                    onBackPressed();
                    finish();
                } else {
                    DistanceDialog();
                    address_Et.setText(address);
                   }
            }
        });
    }


    private void DistanceDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.distance_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(PickupLocationActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(true);
        dailogbox.setView(confirmdailog);
        BtnOkDistance = confirmdailog.findViewById(R.id.BtnOkDistance);
        BtnOkDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnOkDistance.setBackgroundResource(R.drawable.black_bg);
                BtnOkDistance.setTextColor(getResources().getColor(R.color.white));
                dailogbox.dismiss();
            }
        });

        dailogbox.show();
    }

    private void GetLocation() {

        CommonUtils.showProgress(PickupLocationActivity.this, "");

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    CommonUtils.dismissProgress();

                    //

                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());

                    App.getSinltonPojo().setCurrentLat(latitude);
                    App.getSinltonPojo().setCurrentLong(longitude);

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        App.getSinltonPojo().setmCity(city);
                        App.getSinltonPojo().setmState(state);
                        App.getSinltonPojo().setmCountry(country);
                        App.getSinltonPojo().setmZipCode(postalCode);

//                        gotoLocation(location.getLatitude(), location.getLongitude());

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        map.animateCamera(cameraUpdate);
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        //  address = addresses.get(0).getAddressLine(0);
//                        drop_location_et.setText(address);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    CommonUtils.dismissProgress();
                }

                @Override
                public void onProviderEnabled(String provider) {
                    CommonUtils.dismissProgress();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    CommonUtils.dismissProgress();
                }
            });
        } else if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    CommonUtils.dismissProgress();

                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());

                    App.getSinltonPojo().setCurrentLat(latitude);
                    App.getSinltonPojo().setCurrentLong(longitude);

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {

                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        App.getSinltonPojo().setmCity(city);
                        App.getSinltonPojo().setmState(state);
                        App.getSinltonPojo().setmCountry(country);
                        App.getSinltonPojo().setmZipCode(postalCode);
//                        gotoLocation(location.getLatitude(), location.getLongitude());

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        map.animateCamera(cameraUpdate);
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    CommonUtils.dismissProgress();

                }

                @Override
                public void onProviderEnabled(String provider) {
                    CommonUtils.dismissProgress();

                }

                @Override
                public void onProviderDisabled(String provider) {
                    CommonUtils.dismissProgress();

                }
            });
        }
        client = new GoogleApiClient.Builder(getApplicationContext()).addApi(AppIndex.API).build();
    }

    private void gotoLocation(double lat, double lng) {

        LatLng latLng = new LatLng(lat, lng);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        map.animateCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BtnOkLocation:
                checkDistance();
                break;

            case R.id.address_Et:
                Places.initialize(this.getApplicationContext(), this.getResources().getString(R.string.api_key));
                PlacesClient placesClient = Places.createClient(this);

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(PickupLocationActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;

            case R.id.currenLocationBtn:
                GetLocation();
                break;
        }
    }
}
