package com.omninos.petrowagon.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.omninos.petrowagon.ModelClass.CheckDistanceModel;
import com.omninos.petrowagon.ModelClass.DeliveryTimeModel;
import com.omninos.petrowagon.ModelClass.MinimumValueModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.CommonUtils;
import com.omninos.petrowagon.ViewModel.DescriptionViewModel;
import com.omninos.petrowagon.ViewModel.OrderViewModel;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlaceOrderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private Button btn_place_order, BtnOkDistance;
    private TextView tv_head;
    private EditText et_quantity_place_order, drop_location_et, et_schedule_place_order, et_schedule_time, et_quantity_with_price;
    private String quantity = "", location, scheduleDate, scheduleTime, minPriceText, minQuantityText;
    private ImageView img_back, drop_icon;
    private Button unChange[];
    private RadioButton productQuantityRadio, productPriceRadio;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private GoogleApiClient client;
    private Activity activity;
    private OrderViewModel viewModel;
    private DescriptionViewModel timeViewModel;
    private String address;
    private String format = "";
    private LayoutInflater layoutInflater;
    private RelativeLayout placeOrderMain;
    int changedMinutes = 0;
    int changedHours = 0;
    private String currentDate = "", SelectedDate = "", currentTime = "", SelectedTime = "";
    private Date dateCurrent, dateSelected, timeSelected, timeCurrent;
    int day, month, year;
    private GoogleApiClient googleApiClient;
    private String loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        timeViewModel = ViewModelProviders.of(this).get(DescriptionViewModel.class);

        initIds();

        App.getSinltonPojo().setDropAddress(null);
        drop_location_et.setText("");
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateCurrent = dateFormat.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_head.setText(App.getSinltonPojo().getProductName());
    }

    private void initIds() {
        activity = PlaceOrderActivity.this;
        //  Places.initialize(this.getApplicationContext(), this.getResources().getString(R.string.api_key));
        //  PlacesClient placesClient = Places.createClient(this);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        btn_place_order = findViewById(R.id.btn_place_order);
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        et_quantity_place_order = findViewById(R.id.et_quantity_place_order);
        drop_location_et = findViewById(R.id.drop_location_et);
        et_schedule_place_order = findViewById(R.id.et_schedule_place_order);
        et_schedule_time = findViewById(R.id.et_schedule_time);
        productPriceRadio = findViewById(R.id.productPriceRadio);
        productQuantityRadio = findViewById(R.id.productQuantityRadio);
        drop_icon = findViewById(R.id.drop_icon);
        placeOrderMain = findViewById(R.id.placeOrderMain);
//        et_quantity_with_price = findViewById(R.id.et_quantity_with_price);
        btn_place_order.setOnClickListener(this);
        img_back.setOnClickListener(this);
        drop_location_et.setOnClickListener(this);
        et_schedule_time.setOnClickListener(this);
        et_schedule_place_order.setOnClickListener(this);
        drop_icon.setOnClickListener(this);
        minQuantityText = App.getSinltonPojo().getMinQuantityText();
        minPriceText = App.getSinltonPojo().getMinPriceText();
        et_quantity_place_order.setHint(minQuantityText);
        productQuantityRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    et_quantity_place_order.setHint(minQuantityText);
                }
            }
        });

        productPriceRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_quantity_place_order.setHint(minPriceText);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_place_order:
                changeButtonBackground(btn_place_order, null);
                validate();
                break;

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.et_schedule_place_order:
                PickDate();
                break;

            case R.id.et_schedule_time:
                PickTime();
                break;

            case R.id.drop_location_et:
//                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

                startActivity(new Intent(PlaceOrderActivity.this, PickupLocationActivity.class));
                break;

            case R.id.drop_icon:
//                GetLocation();
//                permissions();
                turnGPSOn();
                break;
        }
    }

    private void permissions() {
        if (ActivityCompat.checkSelfPermission(PlaceOrderActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION + Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlaceOrderActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            GetLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean location1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean location2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                if (grantResults.length > 0 && location1 && location2) {
                    GetLocation();
                } else if (Build.VERSION.SDK_INT >= 23 &&
                        !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    OpenSetting();

                } else {
                    ActivityCompat.requestPermissions(PlaceOrderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
        }

    }

    private void OpenSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission");
        builder.setMessage("Permissions are required");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(PlaceOrderActivity.this, "Go to Settings to Grant the Storage Permissions and restart application", Toast.LENGTH_LONG).show();
//                sentToSettings = true;
                //move to setting for permission
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", PlaceOrderActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        })
                .create()
                .show();
    }

    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(PlaceOrderActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            permissions();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(PlaceOrderActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            Toast.makeText(PlaceOrderActivity.this, "off", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                permissions();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Toast.makeText(this, "You have to turn on the GPS to use this Application. Please restart Application.", Toast.LENGTH_LONG).show();
            }
        }
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                LatLng latLng = place.getLatLng();
//
//                App.getSinltonPojo().setCurrentLat(String.valueOf(latLng.latitude));
//                App.getSinltonPojo().setCurrentLong(String.valueOf(latLng.longitude));
//                address = place.getAddress();
//                checkDistance(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
//
//                Geocoder geocoder;
//                List<Address> addresses;
//                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//
//                try {
//                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
////                        String address = addresses.get(0).getAddressLine(0);
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName();
//
//                    App.getSinltonPojo().setmCity(city);
//                    App.getSinltonPojo().setmState(state);
//                    App.getSinltonPojo().setmCountry(country);
//                    App.getSinltonPojo().setmZipCode(postalCode);
//
//                    address = addresses.get(0).getAddressLine(0);
////                        drop_location_et.setText(address);
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Log.i("Place Name", "Place: " + place.getName() + ", " + place.getId());
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i("Status", status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkQuantity() {
        quantity = et_quantity_place_order.getText().toString();
        viewModel.minimum(activity).observe(PlaceOrderActivity.this, new Observer<MinimumValueModel>() {
            @Override
            public void onChanged(MinimumValueModel minimumValueModel) {
                if (minimumValueModel.getSuccess().equalsIgnoreCase("1")) {
                    int minPrice = 0, minQuantity = 0;
                    for (int i = 0; i < minimumValueModel.getDetails().size(); i++) {
                        minPrice = Integer.parseInt(minimumValueModel.getDetails().get(i).getMinimumPrice());
                        minQuantity = Integer.parseInt(minimumValueModel.getDetails().get(i).getMinimumQuantity());
                        minPriceText = minimumValueModel.getDetails().get(i).getPriceText();
                        minQuantityText = minimumValueModel.getDetails().get(i).getQuantityText();
                    }

                    if (productQuantityRadio.isChecked()) {
                        if (!quantity.equalsIgnoreCase("")) {
                            if (Integer.parseInt(quantity) >= minQuantity) {
//                            Toast.makeText(activity, "Quantity True", Toast.LENGTH_SHORT).show();
                                deliveryTime();
                            } else {
                                Snackbar snackbar = Snackbar.make(placeOrderMain, "Please enter minimum quantity", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }

                    }

                    if (productPriceRadio.isChecked()) {
                        if (!quantity.equalsIgnoreCase("")) {
                            if (Integer.parseInt(quantity) >= minPrice) {
//                            Toast.makeText(activity, "Price True", Toast.LENGTH_SHORT).show();
                                deliveryTime();
                            } else {
                                Snackbar snackbar = Snackbar.make(placeOrderMain, "Please enter minimum price", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }

                    }
                }
            }
        });


    }

    private void validate() {
        quantity = et_quantity_place_order.getText().toString();
        location = drop_location_et.getText().toString();
        scheduleDate = et_schedule_place_order.getText().toString();
        scheduleTime = et_schedule_time.getText().toString();

        if (productQuantityRadio.isChecked()) {
            if (quantity.isEmpty() || location.isEmpty() || scheduleDate.isEmpty()) {
                Snackbar.make(et_quantity_place_order, "Please fill all fileds", Snackbar.LENGTH_LONG).show();
            } else {
                checkQuantity();
            }
        } else if (productPriceRadio.isChecked()) {
            if (quantity.isEmpty() || location.isEmpty() || scheduleDate.isEmpty()) {
                Snackbar.make(et_quantity_place_order, "Please fill all fileds", Snackbar.LENGTH_LONG).show();
            } else {
                checkQuantity();
            }
        }
    }

    private void checkDistance(String Lati, String Longi) {
//        String latitude = App.getSinltonPojo().getCurrentLat();
//        String longitude = App.getSinltonPojo().getCurrentLong();
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();

        viewModel.checkDistance(activity, Lati, Longi, userId, address).observe(PlaceOrderActivity.this, new Observer<CheckDistanceModel>() {
            @Override
            public void onChanged(CheckDistanceModel checkDistanceModel) {
                if (checkDistanceModel.getSuccess().equalsIgnoreCase("1")) {
                    drop_location_et.setText(address);
                } else {
                    DistanceDialog();
                    drop_location_et.setText("");
                    // Toast.makeText(activity, "Sorry we are currentle not providing this service in your area", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void DistanceDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.distance_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(PlaceOrderActivity.this).create();
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

    private void PickDate() {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePicker = new DatePickerDialog(PlaceOrderActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, month);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                et_schedule_place_order.setText(sdf.format(mcurrentDate.getTime()));
                SelectedDate = sdf.format(mcurrentDate.getTime());
            }
        }, mYear, mMonth, mDay);
        mDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDatePicker.getDatePicker().setSpinnersShown(true);
        mDatePicker.getDatePicker().setCalendarViewShown(false);
        long value = mcurrentDate.getTimeInMillis();
        mDatePicker.getDatePicker().setMinDate(value);
        mDatePicker.setTitle("Choose Drop Date");

        mDatePicker.show();

    }

    private void PickTime() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SelectedTime = hourOfDay + ":" + minute;

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        currentTime = sdf.format(new Date());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        try {
                            dateSelected = dateFormat.parse(SelectedDate);
                            dateCurrent = dateFormat.parse(currentDate);
                            timeSelected = timeFormat.parse(SelectedTime);
                            timeCurrent = timeFormat.parse(currentTime);

                            if (dateCurrent.getTime() == dateSelected.getTime()) {

                                if (timeCurrent.getTime() + 1800000 <= timeSelected.getTime()) {


                                    if (hourOfDay < 10) {
                                        if (minute < 10) {
                                            et_schedule_time.setText("0" + hourOfDay + ":" + "0" + minute + " " + format);
                                        } else {
                                            et_schedule_time.setText("0" + hourOfDay + ":" + minute + " " + format);
                                        }

                                    }
                                    if (minute < 10) {
                                        et_schedule_time.setText(hourOfDay + ":" + "0" + minute + " " + format);
                                    }
                                    if (hourOfDay < 10 && minute < 10) {
                                        et_schedule_time.setText("0" + hourOfDay + ":" + "0" + minute + " " + format);
                                    } else {
                                        et_schedule_time.setText(hourOfDay + ":" + minute + " " + format);
                                    }


                                } else {
                                    Snackbar.make(placeOrderMain, "Please select half an hour later from now.", Snackbar.LENGTH_LONG).show();
                                    et_schedule_time.setText("");
                                }

                            } else if (dateCurrent.getTime() < dateSelected.getTime()) {

                                if (hourOfDay < 10) {
                                    if (minute < 10) {
                                        et_schedule_time.setText("0" + hourOfDay + ":" + "0" + minute + " " + format);

                                    } else {
                                        et_schedule_time.setText("0" + hourOfDay + ":" + minute + " " + format);
                                    }
                                } else if (minute < 10) {
                                    et_schedule_time.setText(hourOfDay + ":" + "0" + minute + " " + format);
                                } else if (hourOfDay < 10 && minute < 10) {
                                    et_schedule_time.setText("0" + hourOfDay + ":" + "0" + minute + " " + format);
                                } else {
                                    et_schedule_time.setText(hourOfDay + ":" + minute + " " + format);
                                }

                            } else {

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Current Date: " + currentDate);
                        System.out.println("Selected Date: " + SelectedDate);
                        System.out.println("Current Date: " + currentTime);
                        System.out.println("Selected Date: " + SelectedTime);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    private void deliveryTime() {
        if (!et_schedule_time.getText().toString().isEmpty()) {

            Date checkTime = null;
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
            //  checkTime = sd.parse(et_schedule_time.getText().toString());
            checkTime = timeSelected;
            Date finalCheckTime = checkTime;
            timeViewModel.deliveryTime(activity).observe(PlaceOrderActivity.this, new Observer<DeliveryTimeModel>() {
                @Override
                public void onChanged(DeliveryTimeModel deliveryTimeModel) {
                    if (deliveryTimeModel.getSuccess().equalsIgnoreCase("1")) {
                        for (int i = 0; i < deliveryTimeModel.getDetails().size(); i++) {
                            try {
                                Date sT = sd.parse(String.valueOf(deliveryTimeModel.getDetails().get(i).getStime()));
                                Date eT = sd.parse(String.valueOf(deliveryTimeModel.getDetails().get(i).getEtime()));

                                if (sT.getTime() <= finalCheckTime.getTime() && eT.getTime() >= finalCheckTime.getTime()) {
//                                    Toast.makeText(activity, "Pass", Toast.LENGTH_SHORT).show();
                                    validate();
                                    if (productQuantityRadio.isChecked()) {
                                        et_quantity_place_order.setHint("Litres");
                                        App.getSinltonPojo().setProductStatus("1");
                                        App.getSinltonPojo().setProductQuantity(quantity);
                                        moveIntent();
//                                        }
                                    }
                                    if (productPriceRadio.isChecked()) {
                                        et_quantity_place_order.setHint("Price");
                                        App.getSinltonPojo().setProductStatus("2");
                                        App.getSinltonPojo().setProductTotalPrice(quantity);
                                        moveIntent();
//                                        }
                                    }
                                } else {
                                    Snackbar snackbar = Snackbar.make(placeOrderMain, "Please select time between 5AM to 12 AM", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });


        } else {
            Snackbar.make(et_quantity_place_order, "Choose order time", Snackbar.LENGTH_LONG).show();
        }
    }

    private void moveIntent() {
//        App.getSinltonPojo().setProductQuantity(quantity);
        App.getSinltonPojo().setProductLocation(location);
        App.getSinltonPojo().setProductDate(scheduleDate);
        App.getSinltonPojo().setProductTime(SelectedTime);
        Intent intent = new Intent(PlaceOrderActivity.this, OrderDetailsActivity.class);
        intent.putExtra("type", "place_order");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_place_order};
        unChangeButtonBackground(unChange);

        address = App.getSinltonPojo().getDropAddress();
        drop_location_et.setText(address);

    }

    private void changeButtonBackground(Button toChange, Button unChange[]) {

        toChange.setBackgroundResource(R.drawable.black_bg);
        toChange.setTextColor(getResources().getColor(R.color.white));
        if (unChange != null) {
            for (int i = 0; i < unChange.length; i++) {
                unChange[i].setBackgroundResource(R.drawable.grey_corners);
                unChange[i].setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void unChangeButtonBackground(Button unChange[]) {

        for (int i = 0; i < unChange.length; i++) {
            unChange[i].setBackgroundResource(R.drawable.grey_corners);
            unChange[i].setTextColor(getResources().getColor(R.color.black));
        }

    }

    private void GetLocation() {
        CommonUtils.showProgress(activity, "");


        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, new LocationListener() {
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
                        checkDistance(latitude, longitude);
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


                        address = addresses.get(0).getAddressLine(0);
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

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
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

                        checkDistance(latitude, longitude);
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


                        address = addresses.get(0).getAddressLine(0);
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
        }
        client = new GoogleApiClient.Builder(getApplicationContext()).addApi(AppIndex.API).build();
    }


}
