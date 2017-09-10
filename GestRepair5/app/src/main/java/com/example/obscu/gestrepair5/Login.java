package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView textview;
    RequestQueue rq;
    Button login_button;
    EditText UserName,Password;
    String username, password, ID;
    Ip ip = new Ip();
    String login_url = ip.stIp()+"/login";
    AlertDialog.Builder builder;
    RequestQueue queue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        rq = Volley.newRequestQueue(this);
       /* textview = (TextView)findViewById(R.id.reg_txt);
        textview.setOnClickListener((v) {
                startActivity(new Intent(Login.this, Register.class));
        });*/
        builder = new AlertDialog.Builder(Login.this);
        login_button = (Button)findViewById(R.id.bn_login);
        UserName = (EditText) findViewById(R.id.login_name);
        Password = (EditText) findViewById(R.id.login_password);
        queue = Volley.newRequestQueue(this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=UserName.getText().toString();
                password=Password.getText().toString();

                StringRequest postRequest = new StringRequest(Request.Method.POST, login_url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response

                                Log.d("Response", response);
                                String[] data = new String[3];
                                data[0] = username;
                                data[1] = password;
                                data[2] = response;

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                intent.putExtra("username", data[0]);
                                intent.putExtra("password", data[1]);
                                intent.putExtra("response", data[2]);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                //Toast.makeText(Login.this,"Erro",Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        try {
                            Map<String, String> map = new HashMap<String, String>();
                            String key = "Authorization";
                            String encodedString = Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
                            String value = String.format("Basic %s", encodedString);
                            map.put(key, value);
                            return map;
                        } catch (Exception e) {

                        }

                        return super.getHeaders();
                    }

                };


                queue.add(postRequest);


            }
        });


    }
    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserName.setText("");
                Password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}