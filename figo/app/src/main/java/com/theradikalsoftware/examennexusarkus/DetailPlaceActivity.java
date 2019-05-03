/*
 * @author: David Ochoa Gutierrez
 * @contact: @theradikalstyle - david.ochoa.gtz@outlook.com
 * @copyright: TheRadikalSoftware - 2019
 */

package com.theradikalsoftware.examennexusarkus;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<PlacesData> list;
    int position;
    TextView name, address, distance, cardDirectionTXT, cardCallTXT, cardWebTXT;
    RatingBar rating;
    ImageView petfriendly;
    LatLng placeLoc;
    CardView cardDirections, cardCall, cardWebsite;
    String phone, url;
    static final int CALL_REQ_CODE = 3313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_place);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            list = (ArrayList<PlacesData>) bundle.getSerializable("adapter");
            position = bundle.getInt("adapPosition");

            if(getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            name = findViewById(R.id.detailplace_textview_name);
            address = findViewById(R.id.detailplace_textview_fulladdress);
            distance = findViewById(R.id.detailplace_textview_distance);
            rating = findViewById(R.id.detaiplace_ratingbar_rating);
            rating.setNumStars(5);
            rating.setMax(5);
            petfriendly = findViewById(R.id.detailplace_imageview_petfriendly);
            cardDirections = findViewById(R.id.detailview_cardview_directions);
            cardCall = findViewById(R.id.detailview_cardview_call);
            cardWebsite = findViewById(R.id.detailview_cardview_visitwebsite);
            cardDirectionTXT = findViewById(R.id.detailview_textview_directions_action);
            cardCallTXT = findViewById(R.id.detailview_textview_call_action);
            cardWebTXT = findViewById(R.id.detailview_textview_website_action);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailplace_mapview_mapview);
            mapFragment.getMapAsync(this);

            UpdateUI();
        }else{
            Toast.makeText(this, getResources().getString(R.string.error_detailplace_init), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void UpdateUI(){
        StringBuilder text = new StringBuilder(list.get(position).getCategory());

        name.setText(list.get(position).getPlaceName());
        distance.setText(String.format(getResources().getString(R.string.template_distance_between), list.get(position).getDistance()));
        rating.setRating(Float.parseFloat(list.get(position).getRating()));
        if(list.get(position).getIsPetFriendly().equals("true")){
            petfriendly.setVisibility(View.VISIBLE);
            petfriendly.setImageDrawable(getResources().getDrawable(R.mipmap.dogfriendlyactive));
            text.append(", Dogs Allowed \n");
        }else{ text.append("\n"); }
        text.append(list.get(position).getAddressLine1());
        text.append("\n");
        text.append(list.get(position).getAddressLine2());

        address.setText(text.toString());

        GetTimeBetweenPoints();
        url = list.get(position).getSite();
        phone = list.get(position).getPhoneNumber();
        cardCallTXT.setText(phone);
        cardWebTXT.setText(url);

        cardCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(DetailPlaceActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(DetailPlaceActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQ_CODE);
                    }else{
                        //Permission on M and above already granted
                        Call();
                    }
                }else{
                    Call();
                }
            }
        });

        cardWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent(DetailPlaceActivity.this, WebViewActivity.class);
                intentWeb.putExtra("url", url);
                startActivity(intentWeb);
            }
        });

        cardDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:" + placeLoc.latitude + "," + placeLoc.longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void Call(){
        Uri num;
        num = Uri.parse("tel:" + phone);

        Intent intentCall = new Intent(Intent.ACTION_CALL, num);
        startActivity(intentCall);
    }

    private void GetTimeBetweenPoints(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userlocLat = sharedPreferences.getString("userLatitude", null);
        String userlocLong = sharedPreferences.getString("userLongitude", null);
        String urlQuery;
        if(userlocLat != null || userlocLong != null){
            urlQuery = "https://maps.googleapis.com/maps/api/directions/json?origin=" + userlocLat + ","
                    + userlocLong + "&destination=" + list.get(position).getLatitude() + ","
                    + list.get(position).getLongitude() + "&key=INSERT GMAPS DIRECTIONS KEY HERE";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlQuery, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response Size: ", String.valueOf(response.length()));
                    try{
                        JSONObject jsonObject = new JSONObject(String.valueOf(response)); // parse response into json object
                        JSONArray routeObject = jsonObject.getJSONArray("routes"); // pull out the "route" object
                        JSONObject legsObject = routeObject.getJSONObject(0); // pull out the "route" object
                        JSONArray durationObject = legsObject.getJSONArray("legs"); // pull out the "route" object
                        JSONObject durationObject2 = durationObject.getJSONObject(0);
                        final JSONObject dura = durationObject2.getJSONObject("duration");
                        final String duracion = dura.getString("text");
                        DetailPlaceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cardDirectionTXT.setText(duracion);
                            }
                        });
                    }catch (JSONException ex){
                        ex.getMessage();
                        DetailPlaceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cardDirectionTXT.setText("-");
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Response Error: ", error.getMessage());
                    cardDirectionTXT.setText("-");
                }
            });
            Volley.newRequestQueue(this).add(request);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CALL_REQ_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Call();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        petfriendly.setImageDrawable(null);
        petfriendly = null;
        list.clear();
        list = null;
        name = null;
        address = null;
        distance = null;
        cardDirectionTXT = null;
        cardCallTXT = null;
        cardWebTXT = null;
        rating = null;
        cardDirections = null;
        cardCall = null;
        cardWebsite = null;
        phone = null;
        url = null;
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        placeLoc = new LatLng(Double.parseDouble(list.get(position).getLatitude())
                , Double.parseDouble(list.get(position).getLongitude()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc,16));
        googleMap.addMarker(new MarkerOptions()
                .position(placeLoc)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pinselected))
        );
    }
}
