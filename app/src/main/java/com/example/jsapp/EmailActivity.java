package com.example.jsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class EmailActivity extends AppCompatActivity {
    private static final String TAG = EmailActivity.class.getSimpleName();
    private ListView listView;
    private EmailViewAdapter adapter;
    private EditText edt_id;
    private EditText edt_content;
    private Button btn_send,btn_sendedEmail;

    private static final String URL_receive = "http://pjpj1018.ivyro.net/EmailReceive.php";
    private static final String URL_send = "http://pjpj1018.ivyro.net/EmailSend.php";

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userID = user.get(sessionManager.USERID);

        edt_id = (EditText) findViewById(R.id.edt_id);
        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_sendedEmail = findViewById(R.id.btn_sendedEmail);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new EmailViewAdapter(EmailActivity.this);
        listView.setAdapter(adapter);

        btn_sendedEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailActivity.this, SendedEmailActivity.class);
                startActivity(intent);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_id.getText().toString().equals("") || edt_content.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
                } else if(edt_id.getText().toString().equals(userID)) {
                    Toast.makeText(getApplicationContext(), "본인에게 쪽지를 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    sendEmail(userID, edt_id.getText().toString(), edt_content.getText().toString());
                }
                edt_id.setText("");
                edt_content.setText("");

                adapter = new EmailViewAdapter(EmailActivity.this);
                listView.setAdapter(adapter);
                getList(userID);
            }
        });

        getList(userID);
        adapter.notifyDataSetChanged();
    }
    //쪽지 보냄
    private void sendEmail(String userID, String receiver, String content) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_send, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "쪽지를 보냈습니다." , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "존재하지 않는 사용자입니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EmailActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmailActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", userID);
                params.put("receiver", receiver);
                params.put("content", content);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //받은 쪽지리스트를 가져옴
    private void getList(String userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_receive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("sender").trim();
                            String content = object.getString("content").trim();

                            adapter.addItem(id, content);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EmailActivity.this, "Error Reading Detail : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmailActivity.this, "Error Reading Detail : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
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