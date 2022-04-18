package com.ungureanu.inshape;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kwabenaberko.openweathermaplib.model.common.Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class WeatherFragment extends Fragment {

    private Context context;
    private TextView textViewTemperature;
    private TextView textViewCityName;
    private TextView textViewDate;
    private View rootView;
    private String city = "Pitesti";
    private FusedLocationProviderClient client;

    private final String TAG = WeatherFragment.class.getSimpleName();
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "2eb188828152bb3c49d436fe0b48ff2b";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Log.d(TAG, "onCreateView: called");
        setContext(container);
        this.rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        this.client = LocationServices.getFusedLocationProviderClient(getActivity());

        return rootView;
    }

    public static WeatherFragment newInstance() {
        //Log.d(WeatherFragment.class.getSimpleName(), "newInstance: called");
        return new WeatherFragment();
    }

    private void setContext(@Nullable ViewGroup container) {
        //Log.d(TAG, "setContext: called");
        assert container != null;
        this.context = container.getContext();
    }

    private void setTextViewTemperatureGUI(View view) {
        //Log.d(TAG, "setTextViewTemperatureGUI: called");
        this.textViewTemperature = (TextView) view.findViewById(R.id.idTemperature);
    }

    private void setTextViewCityNameGUI(View view) {
        //Log.d(TAG, "setTextViewCityNameGUI: called");
        this.textViewCityName = (TextView) view.findViewById(R.id.idCityName);
    }

    private void setTextViewDateGUI(View view) {
        //Log.d(TAG, "setTextViewDateGUI: called");
        this.textViewDate = (TextView) view.findViewById(R.id.idDate);
    }



    @Override
    public void onStart() {
        //Log.d(TAG, "onStart: called");
        super.onStart();
        View view = getView();
        this.context = getContext();
        if (view != null) {
            setTextViewCityNameGUI(view);
            setTextViewDateGUI(view);
            setTextViewTemperatureGUI(view);
            //Toast.makeText(context, "Ciocan1", Toast.LENGTH_SHORT).show();


            //////////
            if(ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
            //////////

            String tempUrl = url + "?q=" + city + "&appid="+appid + "&units=Metric";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onResponse(String response) {
                    //Log.d(TAG, response);
                    try {
                        JSONObject jsonRespone = new JSONObject(response);
                        JSONObject jsonObject = jsonRespone.getJSONObject("main");
                        JSONArray jsonArray = jsonRespone.getJSONArray("weather");
                        JSONObject object = jsonArray.getJSONObject(0);
                        String temp = String.valueOf(Math.round(jsonObject.getDouble("temp")));
                        String description = object.getString("main");

                        String clouds = "clouds";
                        String Clouds = "Clouds";

                        int semafor = 0;
                        if(description.contains(clouds) || description.contains(Clouds)){
                            semafor = 1;
                        }

                        if(semafor == 1){
                            view.setBackground(getResources().getDrawable(R.drawable.cloudy));
                        }




                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        String simpleDateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());//Saturday
                        String monthString  = (String) DateFormat.format("MMMM",  date);//Jun
                        String day          = (String) DateFormat.format("dd",   date);//20
                        String year         = (String) DateFormat.format("yyyy", date); // 2013
                        textViewDate.setText(simpleDateFormat+", "+monthString+" "+day +", "+year);


                        textViewTemperature.setText(temp+"°C");

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            requestQueue.add(stringRequest);
        }
    }


    private String getLocation(double lat, double lon){
        //Log.d(TAG, "getLocation: called");
        String cityName = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if(addresses.size() > 0){
                for(Address adr: addresses){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return cityName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG, "onRequestPermissionsResult: called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else {
            Toast.makeText(getActivity(), "Denied permission", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        //Log.d(TAG, "getCurrentLocation: called");
        LocationManager locationManager = (LocationManager) getActivity().
                getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        textViewCityName.setText(getLocation(location.getLatitude(), location.getLongitude()));
                        //Toast.makeText(getActivity(), String.valueOf(location.getLatitude()) + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {

                                Location location1 = locationResult.getLastLocation();
                                textViewCityName.setText(getLocation(location1.getLatitude(), location1.getLongitude()));
                                //Toast.makeText(getActivity(), getLocation(location1.getLatitude(), location1.getLongitude()), Toast.LENGTH_SHORT).show();
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }
    }
    
}
