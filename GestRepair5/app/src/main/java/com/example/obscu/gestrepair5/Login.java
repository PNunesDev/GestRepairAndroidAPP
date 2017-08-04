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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView textview;
    Button login_button;
    EditText UserName,Password;
    String username, password;
    Ip ip = new Ip();
    String login_url = ip.stIp()+"/login";
    AlertDialog.Builder builder;
    RequestQueue queue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
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


                /*
                if(username.equals("") || password.equals("")) {
                    builder.setTitle("Alguma coisa correu mal");
                    displayAlert("Introduza um Username e Password válido");
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");

                                if(code.equals("login_failed")){
                                    builder.setTitle("Login Error...");
                                    displayAlert(jsonObject.getString("message"));
                                }
                                else {
                                    Intent intent = new Intent(Login.this, LoginSucess.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", jsonObject.getString("name"));
                                    bundle.putString("email", jsonObject.getString("email"));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("Response", response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                                Toast.makeText(Login.this,"Erro",Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username",username);
                            params.put("password",password);
                            return params;
                        }
                    };
                    //MySingleton.getInstance(Login.this).addToRequestque(stringRequest);
                }*/



                StringRequest postRequest = new StringRequest(Request.Method.POST, login_url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                String Json = response;
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONObject data = obj.getJSONObject("data");
                                    String name = data.getString("name");
                                    /*Context context = getApplicationContext();
                                    CharSequence text = name;
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();*/




                                    Intent intent = new Intent(getBaseContext(), PostLogin.class);
                                    intent.putExtra("response", response);
                                    intent.putExtra("username", username);
                                    intent.putExtra("password", password);
                                    startActivity(intent);


                                    /*Intent i = new Intent (Login.this, PostLogin.class);

                                    i.putExtra(response,Json);
                                    startActivity(i);*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());

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
