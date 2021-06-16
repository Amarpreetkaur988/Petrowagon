package com.omninos.petrowagon.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.omninos.petrowagon.Activities.PaymentMethodActivity;
import com.omninos.petrowagon.Activities.PlaceOrderActivity;
import com.omninos.petrowagon.Adapters.AdapterPastOrders;
import com.omninos.petrowagon.ModelClass.CheckDistanceModel;
import com.omninos.petrowagon.ModelClass.CurrentOrderModel;
import com.omninos.petrowagon.ModelClass.DeliveryTimeModel;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterPastOrders adapterPastOrders;
    List<CurrentOrderModel> list;
    private OrderViewModel viewModel;
    private Activity activity;
    private ImageView drop_icon_popup;
    private LayoutInflater layoutInflater;
    private EditText et_reorder_date, et_reorder_time, et_reorder_location;
    private String date, time, location;
    private Button btn_cancel_reorder, btn_next_reorder, BtnOkDistance;
    View v;
    private TextView PastOrderText;
    private String address;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private String latitude = "", longitude = "";
    private GoogleApiClient client;
    private String currentDate = "", SelectedDate = "", currentTime = "", SelectedTime = "";
    private Date dateCurrent, dateSelected, timeSelected, timeCurrent;

    private DescriptionViewModel timeViewModel;
    private GoogleApiClient googleApiClient;
    Context context;

    public PastOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_past_orders, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        timeViewModel = ViewModelProviders.of(getActivity()).get(DescriptionViewModel.class);
        initIds(v);
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateCurrent = dateFormat.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getPastOrderList();

        return v;
    }

    private void initIds(View v) {
        Places.initialize(this.getContext(), this.getResources().getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(getContext());
        activity = getActivity();
        recyclerView = v.findViewById(R.id.recycler_past_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PastOrderText = v.findViewById(R.id.PastOrderText);

    }

    private void getPastOrderList() {
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();
        viewModel.pastOrder(activity, userId).observe(getActivity(), new Observer<CurrentOrderModel>() {
            @Override
            public void onChanged(final CurrentOrderModel currentOrderModel) {
                if (currentOrderModel.getSuccess().equalsIgnoreCase("1")) {
                    PastOrderText.setVisibility(View.GONE);
                    adapterPastOrders = new AdapterPastOrders(currentOrderModel.getDetails(), getContext(), new AdapterPastOrders.Select() {
                        @Override
                        public void Choose(int position) {
                            App.getSinltonPojo().setOrderId(currentOrderModel.getDetails().get(position).getId());
                            App.getSinltonPojo().setProductQuantity(currentOrderModel.getDetails().get(position).getQuantity());
                            App.getSinltonPojo().setProductPrice(currentOrderModel.getDetails().get(position).getPricePerLitre());
                            App.getSinltonPojo().setProductTotalPrice(currentOrderModel.getDetails().get(position).getTotalPrice());
                            App.getSinltonPojo().setProductLocation(currentOrderModel.getDetails().get(position).getLocation());
                            reorderDialog();
                        }
                    });
                    recyclerView.setAdapter(adapterPastOrders);
                } else {

                }
            }
        });
    }

    private void reorderDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.reorder_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(getContext()).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setView(confirmdailog);

        drop_icon_popup = confirmdailog.findViewById(R.id.drop_icon_popup);
        drop_icon_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetLocation();
//                turnGPSOn();
//                permissions();
            }
        });

        et_reorder_location = confirmdailog.findViewById(R.id.et_reorder_location);
        et_reorder_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });


        et_reorder_date = confirmdailog.findViewById(R.id.et_reorder_date);
        et_reorder_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickDate();

            }
        });

        et_reorder_time = confirmdailog.findViewById(R.id.et_reorder_time);
        et_reorder_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime();

            }
        });

        btn_cancel_reorder = confirmdailog.findViewById(R.id.btn_cancel_reorder);
        btn_cancel_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cancel_reorder.setBackgroundResource(R.drawable.black_bg);
                btn_cancel_reorder.setTextColor(getResources().getColor(R.color.white));
                dailogbox.dismiss();
            }
        });

        btn_next_reorder = confirmdailog.findViewById(R.id.btn_next_reorder);
        btn_next_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_reorder_time.equals("")) {
                    deliveryTime();
                } else {
                    btn_next_reorder.setBackgroundResource(R.drawable.black_bg);
                    btn_next_reorder.setTextColor(getResources().getColor(R.color.white));

                }

            }
        });
        dailogbox.show();

    }


    private void checkDistance(String Lati, String Longi) {


//        String latitude = App.getSinltonPojo().getCurrentLat();
//        String longitude = App.getSinltonPojo().getCurrentLong();
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();


        viewModel.checkDistance(activity, Lati, Longi, userId, address).observe(PastOrdersFragment.this, new Observer<CheckDistanceModel>() {
            @Override
            public void onChanged(CheckDistanceModel checkDistanceModel) {
                if (checkDistanceModel.getSuccess().equalsIgnoreCase("1")) {
                    et_reorder_location.setText(address);
                } else {
                    DistanceDialog();
                    et_reorder_location.setText("");
                    // Toast.makeText(activity, "Sorry we are currentle not providing this service in your area", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();

                App.getSinltonPojo().setCurrentLat(String.valueOf(latLng.latitude));
                App.getSinltonPojo().setCurrentLong(String.valueOf(latLng.longitude));
                address = place.getAddress();
                checkDistance(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));

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
//                        drop_location_et.setText(address);


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

    private void DistanceDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.distance_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(getContext()).create();
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
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, month);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                et_reorder_date.setText(sdf.format(mcurrentDate.getTime()));
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SelectedTime = hourOfDay + ":" + minute;

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        currentTime = sdf.format(new Date());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        try {
                            dateSelected = dateFormat.parse(SelectedDate);
                            dateCurrent = dateFormat.parse(currentDate);
                            timeSelected = timeFormat.parse(SelectedTime);
                            timeCurrent = timeFormat.parse(currentTime);

                            if (dateCurrent.getTime() == dateSelected.getTime()) {
//                                Toast.makeText(activity, "EQUAL", Toast.LENGTH_SHORT).show();

                                if (timeCurrent.getTime() + 1800000 <= timeSelected.getTime()) {
//                                    Toast.makeText(activity, "Right", Toast.LENGTH_SHORT).show();
                                    if (hourOfDay < 10) {
                                        if (minute < 10) {
                                            et_reorder_time.setText("0" + hourOfDay + ":" + "0" + minute);
                                        } else {
                                            et_reorder_time.setText("0" + hourOfDay + ":" + minute);
                                        }

                                    } else if (minute < 10) {
                                        et_reorder_time.setText(hourOfDay + ":" + "0" + minute);
                                    } else if (hourOfDay < 10 && minute < 10) {
                                        et_reorder_time.setText("0" + hourOfDay + ":" + "0" + minute);
                                    } else {
                                        et_reorder_time.setText(hourOfDay + ":" + minute);
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Please select half an hour later from now.", Toast.LENGTH_SHORT).show();
//                                    Snackbar.make(placeOrderMain, "Please select half an hour later from now.", Snackbar.LENGTH_LONG).show();
                                    et_reorder_time.setText("");
                                }

                            } else if (dateCurrent.getTime() < dateSelected.getTime()) {
//                                Toast.makeText(activity, "GREATER", Toast.LENGTH_SHORT).show();
                                if (hourOfDay < 10) {
                                    if (minute < 10) {
                                        et_reorder_time.setText("0" + hourOfDay + ":" + "0" + minute);

                                    } else {
                                        et_reorder_time.setText("0" + hourOfDay + ":" + minute);
                                    }
                                } else if (minute < 10) {
                                    et_reorder_time.setText(hourOfDay + ":" + "0" + minute);
                                } else if (hourOfDay < 10 && minute < 10) {
                                    et_reorder_time.setText("0" + hourOfDay + ":" + "0" + minute);
                                } else {
                                    et_reorder_time.setText(hourOfDay + ":" + minute);
                                }
//                                et_schedule_time.setText(hourOfDay + ":" + minute);
                            } else {
//                                Toast.makeText(activity, "SMALLER", Toast.LENGTH_SHORT).show();
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
        if (!et_reorder_time.getText().toString().isEmpty()) {

            Date checkTime = null;
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
            try {
                checkTime = sd.parse(et_reorder_time.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date finalCheckTime = checkTime;
            timeViewModel.deliveryTime(activity).observe(getActivity(), new Observer<DeliveryTimeModel>() {
                @Override
                public void onChanged(DeliveryTimeModel deliveryTimeModel) {
                    if (deliveryTimeModel.getSuccess().equalsIgnoreCase("1")) {
                        for (int i = 0; i < deliveryTimeModel.getDetails().size(); i++) {
                            try {
                                Date sT = sd.parse(String.valueOf(deliveryTimeModel.getDetails().get(i).getStime()));
                                Date eT = sd.parse(String.valueOf(deliveryTimeModel.getDetails().get(i).getEtime()));

                                if (sT.getTime() <= finalCheckTime.getTime() && eT.getTime() >= finalCheckTime.getTime()) {

                                    App.getSinltonPojo().setReorderStatus("1");
//                                    App.getSinltonPojo().setProductStatus("1");
                                    location = et_reorder_location.getText().toString();
                                    App.getSinltonPojo().setProductLocation(location);
                                    date = et_reorder_date.getText().toString();
                                    App.getSinltonPojo().setProductDate(date);
                                    time = et_reorder_time.getText().toString();
                                    App.getSinltonPojo().setProductTime(time);
                                    if (location.isEmpty()) {
                                        et_reorder_location.setError("Please Enter Drop Location");
                                    }
                                    if (date.isEmpty()) {
                                        et_reorder_date.setError("Please Enter Date");
                                    } else {

                                        Intent intent = new Intent(getActivity(), PaymentMethodActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(activity, "Please select time between 5AM to 12 AM", Toast.LENGTH_SHORT).show();
//                                    Snackbar snackbar = Snackbar.make(placeOrderMain, "Please select time between 5AM to 12 AM", Snackbar.LENGTH_LONG);
//                                    snackbar.show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });


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
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        et_reorder_location.setText(address);

                        App.getSinltonPojo().setmCity(city);
                        App.getSinltonPojo().setmState(state);
                        App.getSinltonPojo().setmCountry(country);
                        App.getSinltonPojo().setmZipCode(postalCode);

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
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        et_reorder_location.setText(address);

                        App.getSinltonPojo().setmCity(city);
                        App.getSinltonPojo().setmState(state);
                        App.getSinltonPojo().setmCountry(country);
                        App.getSinltonPojo().setmZipCode(postalCode);


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
