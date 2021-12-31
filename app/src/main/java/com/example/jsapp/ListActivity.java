package com.example.jsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    private EditText edt_time;
    private EditText edt_content;
    private Button btn_add, btn_profile;
    private ImageView btn_email;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter adapter;
    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String URL_list = "http://pjpj1018.ivyro.net/List.php";
    private static final String URL_insert = "http://pjpj1018.ivyro.net/Insert.php";
    private static final String URL_emailnew = "http://pjpj1018.ivyro.net/EmailNew.php";
    private static final String URL_emaildelete = "http://pjpj1018.ivyro.net/EmailDelete.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userID = user.get(sessionManager.USERID);
        String gymName = user.get(sessionManager.GYM_NAME);

        textView = findViewById(R.id.gym_name) ;
        btn_email = findViewById(R.id.email);
        btn_profile = findViewById(R.id.btn_profile);
        edt_time = findViewById(R.id.edt_time);
        edt_content = findViewById(R.id.edt_content);
        btn_add = findViewById(R.id.btn_add);

        listView = findViewById(R.id.listview);
        adapter = new ListViewAdapter(ListActivity.this);
        listView.setAdapter(adapter);

        textView.setText(gymName) ;

        getList(gymName);
        getNewEmail(userID);

        //MY page
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, ProfileActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("gymName", gymName);
                startActivity(intent);
                finish();
            }
        });

        //쪽지 page
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //email 확인하여 emailnew table에서 지워, 더이상 알림 안뜨게
                deleteNewEmail(userID);
                Intent intent = new Intent(ListActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        // 게시판 글 작성
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_time.getText().toString().equals("") || edt_content.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    insertList(userID, edt_time.getText().toString(), edt_content.getText().toString(), gymName);
                }
                edt_time.setText("");
                edt_content.setText("");
                // adapter.notifyDataSetChanged();추가해도 list가 update 되지 않는 문제로 추가.
                adapter = new ListViewAdapter(ListActivity.this);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                getList(gymName);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userID = user.get(sessionManager.USERID);

        getNewEmail(userID);
    }

    private void insertList(String userID, String time, String content, String gymName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_insert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("listActivity@@" + response);
                Log.e(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "글 작성에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", userID);
                params.put("time", time);
                params.put("content", content);
                params.put("gymName", gymName);
                return params;
            }
        };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

    private void getList(String gymName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("listActivity@@" + response);
                    Log.e(TAG, response.toString());
                    Log.e(TAG, response);

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id").trim();
                            String time = object.getString("time").trim();
                            String content = object.getString("content").trim();

                            adapter.addItem(id, time, content);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ListActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gymName", gymName);

                return params;
            }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    private void getNewEmail(String userID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_emailnew, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String a = jsonObject.getString("id");

                    if (a.equals(userID)) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(ListActivity.this);
                        ad.setIcon(R.mipmap.ic_launcher);
                        ad.setTitle("쪽지 도착!");
                        ad.setMessage("새로운 쪽지가 도착했습니다 확인해주세요.");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        ad.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void deleteNewEmail(String userID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_emaildelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
