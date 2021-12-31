package com.example.jsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    private EditText et_id_lg, et_pass_lg;
    private Button btn_login_lg, btn_register_lg;
    final static private String URL_login = "http://pjpj1018.ivyro.net/Login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        sessionManager.editorClear();

        et_id_lg = findViewById(R.id.et_id_lg);
        et_pass_lg = findViewById(R.id.et_pass_lg);
        btn_login_lg = findViewById(R.id.btn_login_lg);
        btn_register_lg = findViewById(R.id.btn_register_lg);

        btn_register_lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PlaceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login_lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id_lg.getText().toString();
                String userPassword = et_pass_lg.getText().toString();

                if (!userID.isEmpty() || !userPassword.isEmpty()) {
                    loginRequest(userID,userPassword);
                } else {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요." , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sessionManager.editorClear();
    }

    private void loginRequest(String userID, String userPassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String userID = jsonObject.getString("userID");
                        String userPass = jsonObject.getString("userPassword");
                        String userAge = jsonObject.getString("userAge");
                        String userSex = jsonObject.getString("userSex");
                        String gymName = jsonObject.getString("gymName");

                        Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                        sessionManager.createSession(userID, userAge, userSex, gymName);

                        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호가 일치하지 않습니다." , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userID",userID);
                map.put("userPassword", userPassword);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}