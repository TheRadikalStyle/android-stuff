/*
 * @author: David Ochoa Gutierrez
 * @contact: @theradikalstyle - david.ochoa.gtz@outlook.com
 * @copyright: TheRadikalSoftware - 2019
 */

package com.theradikalsoftware.examennexusarkus;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;
    static final int LOC_PERM_CODE = 3312;
    LocationManager locMan;
    Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = findViewById(R.id.mainact_recyclerview_mainview);
        recyclerview.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(lManager);
        adapter = new AdapterData(new ArrayList(), this);
        recyclerview.setAdapter(adapter);

        locMan = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        GetUserLocation();

        GetJsonData();
    }

    private void GetJsonData() {
        String url = "http://www.mocky.io...";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response Size: ", String.valueOf(response.length()));
                JSONObject object;
                PlacesData pData;
                ArrayList<PlacesData> data = new ArrayList<>();
                for (int x = 0; x <= response.length() - 1; x++) {
                    try {
                        object = response.getJSONObject(x);
                        pData = new PlacesData();
                        pData.PlaceId = object.getString("PlaceId");
                        pData.PlaceName = object.getString("PlaceName");
                        pData.Address = object.getString("Address");
                        pData.Category = object.getString("Category");
                        pData.IsOpenNow = object.getString("IsOpenNow");
                        pData.Latitude = object.getString("Latitude");
                        pData.Longitude = object.getString("Longitude");
                        pData.Thumbnail = object.getString("Thumbnail");
                        pData.Rating = object.getString("Rating");
                        pData.IsPetFriendly = object.getString("IsPetFriendly");
                        pData.AddressLine1 = object.getString("AddressLine1");
                        pData.AddressLine2 = object.getString("AddressLine2");
                        pData.PhoneNumber = object.getString("PhoneNumber");
                        pData.Site = object.getString("Site");

                        double placeLat = Double.parseDouble(object.getString("Latitude"));
                        double placeLong = Double.parseDouble(object.getString("Longitude"));
                        pData.distance = GetDistance(placeLat, placeLong);

                        data.add(pData);
                    } catch (JSONException ex) {
                        Log.d("JSON Exc: ", ex.getMessage());
                    }
                }
                SetAdapters(data);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void SetAdapters(ArrayList<PlacesData> data) {
        Collections.sort(data, new CustomListLocComparator());
        adapter = new AdapterData(data, this);
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
    }

    private void GetUserLocation() {
        //Check permission. If version >= Marshmallow ask for permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOC_PERM_CODE);
            }else{
                //Permission on M and above already granted
                GetLKLocation();
            }
        }else{
            //Device run Lollipop or older
            GetLKLocation();
        }

    }

    private void GetLKLocation(){
        lastKnownLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userLatitude", String.valueOf(lastKnownLocation.getLatitude()));
        editor.putString("userLongitude", String.valueOf(lastKnownLocation.getLongitude()));
        editor.commit();
        Log.d("LastKnowLoc-> ", lastKnownLocation.toString());
    }

    private String GetDistance(double placeLat, double placeLong){
        String distance;
        Location locationPlace = new Location("jsonProvider");
        locationPlace.setLatitude(placeLat);
        locationPlace.setLongitude(placeLong);

        if(lastKnownLocation == null){ distance = "N/D"; } else{
            float dist = lastKnownLocation.distanceTo(locationPlace) / 1609.344f; //def mts, /1000 km , /1609.344 mi
            distance = String.format(new Locale("es-MX"), "%.2f", dist);
        }
        return distance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOC_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetLKLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Exit app?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

class CustomListLocComparator implements Comparator<PlacesData> {
    @Override
    public int compare(PlacesData o1, PlacesData o2) {
        return o1.getDistance().compareTo(o2.getDistance());
    }
}
