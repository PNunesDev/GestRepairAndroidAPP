package com.example.obscu.gestrepair5;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
    String login_url = "http://192.168.1.67:8080/login";
    AlertDialog.Builder builder;




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
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=UserName.getText().toString();
                password=Password.getText().toString();

                if(username.equals("") || password.equals("")){
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
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username",username);
                            params.put("password",password);
                            return params;
                        }
                    };
                    //MySingleton.getInstance(Login.this).addToRequestque(stringRequest);
                }

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
