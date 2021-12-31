package com.example.jsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static LatLng Input_LatLng = null;
    private static String PlaceName;
    private static final String TAG = "MapActivity";
    private static final String URL_cg_gym = "http://pjpj1018.ivyro.net/ChangeGym.php";
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userID = user.get(sessionManager.USERID);

        Intent intent = getIntent();
        PlaceName = intent.getStringExtra("PlaceName");
        Input_LatLng=getIntent().getExtras().getParcelable("Input_LatLng");

        // google map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // dialog create
        AlertDialog.Builder ad = new AlertDialog.Builder(MapActivity.this);
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("위치 확인");
        ad.setMessage("선택한 위치가 맞습니까?");

        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(userID == null){
                    Intent intent = new Intent(MapActivity.this, RegisterActivity.class);
                    intent.putExtra("PlaceName", PlaceName);
                    startActivity(intent);
                    finish();
                } else {
                    changeGym(userID, PlaceName);
                    sessionManager.createSession(PlaceName);
                    finish();

                    Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent iw = new Intent(MapActivity.this, PlaceActivity.class);
                startActivity(iw);
            }
        });

        AlertDialog dialog = ad.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Place: " + PlaceName + Input_LatLng);
        LatLng SEOUL = new LatLng(37.485248, 126.901451);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(PlaceName);
        markerOptions.snippet(PlaceName);
        markerOptions.position(Input_LatLng);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Input_LatLng, 16));

    }

    private void changeGym(String userID, String gymName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_cg_gym, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "헬스장 변경에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MapActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", userID);
                params.put("gymName", gymName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}