package com.example.jsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import java.util.Arrays;
import java.util.HashMap;

public class PlaceActivity extends AppCompatActivity {
    private static LatLng Input_LatLng = null;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "PlaceActivity";
    private String userID = null;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Places.initialize(getApplicationContext(), "apikey");

        sessionManager = new SessionManager(this);
        if(sessionManager.isLoggin()){
            HashMap<String, String> user = sessionManager.getUserDetail();
            userID = user.get(sessionManager.USERID);
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(PlaceActivity.this);
            ad.setIcon(R.mipmap.ic_launcher);
            ad.setTitle("헬스장 등록");
            ad.setMessage("헬스장을 먼저 등록해주세요.");

            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            ad.show();
        }

        // 자동완성 Fragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        autocompleteFragment.setCountries("KR");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Input_LatLng = place.getLatLng();
                Intent intent = new Intent(PlaceActivity.this, MapActivity.class);
                intent.putExtra("Input_LatLng", Input_LatLng);
                intent.putExtra("PlaceName", place.getName());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
}
