package com.example.jsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText  et_id, et_pass, et_sex, et_age,et_gym;
    private Button btn_register;
    private RadioButton rbtn_men, rbtn_women;
    private String sex;
    private int userAge;
    final static private String URL_register = "http://pjpj1018.ivyro.net/Register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String gymName = intent.getStringExtra("PlaceName");

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        rbtn_men = findViewById(R.id.rbtn_men);
        rbtn_women = findViewById(R.id.rbtn_women);
        et_age = findViewById(R.id.et_age);
        et_gym = findViewById(R.id.et_gym);
        btn_register = findViewById(R.id.btn_register);
        et_gym.setText(gymName);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();

                if (isNumeric(et_age.getText().toString())) {
                    userAge = Integer.parseInt(et_age.getText().toString());
                    if(userID.length() < 2){
                        Toast.makeText(getApplicationContext(), "2글자 이상의 ID로 만들어주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (rbtn_men.isChecked()){
                            sex = "men";
                        } else if (rbtn_women.isChecked()){
                            sex = "women";
                        } else {
                            Toast.makeText(getApplicationContext(), "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        registerRequest(userID, userPassword, sex, userAge, gymName);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "나이에 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerRequest(String userID, String userPassword, String userSex, int userAge, String gymName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(RegisterActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(RegisterActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userID",userID);
                map.put("userPassword", userPassword);
                map.put("userAge",userAge + "");
                map.put("userSex",userSex);
                map.put("gymName",gymName);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}