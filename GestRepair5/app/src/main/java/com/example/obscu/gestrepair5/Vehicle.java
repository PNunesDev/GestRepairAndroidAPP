package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Vehicle extends AppCompatActivity {
    RequestQueue rq;

     TextView Registration, CC, Km, Fuel, RegisterDate, FrontTire, BackTire,  txtRegistration;
    String SRegistration, SCC, SKm, SFuel, SRegisterDate, SFrontTire, SBackTire;

    String username, password, iduser;

    Ip ip = new Ip();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_vehicle);
        rq = Volley.newRequestQueue(this);


        Registration = (TextView) findViewById(R.id.txtRegistrationValue);
        txtRegistration = (TextView) findViewById(R.id.txt_StatValue);
        CC = (TextView) findViewById(R.id.txtCCValue);
        Km = (TextView) findViewById(R.id.txtKMValue);
        Fuel = (TextView) findViewById(R.id.txtFuelValue);
        RegisterDate = (TextView) findViewById(R.id.txtDateRegisterValue);
        FrontTire = (TextView) findViewById(R.id.txtFrontTireValue);
        BackTire = (TextView) findViewById(R.id.txtBackTireValue);

        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        iduser = Intent.getStringExtra("iduser");
        String url= ip.stIp()+"/vehicle/"+iduser+"/user";
        Log.i("TAG",url+" TESTE");

        sendjsonrequest(url);
    }

    public void sendjsonrequest(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");
                    Intent Intent = getIntent();
                    int intValue = Intent.getIntExtra("position", 0);
                    Log.i("TAG", intValue+"");


                    //JSONObject jsonObject = (JSONObject) jsonArray.get(extras.getInt("ServiceType"));
                    JSONObject jsonObject = (JSONObject) jsonArray.get(intValue);

                    SRegistration = jsonObject.getString("registration");
                    SCC = jsonObject.getString("displacement");
                    SKm = jsonObject.getString("kilometers");
                    SFuel = jsonObject.getString("nameFuel");
                    SRegisterDate = jsonObject.getString("date");
                    SFrontTire = jsonObject.getString("fronttiresize");
                    SBackTire = jsonObject.getString("reartiresize");

                    DateTime TM = new DateTime();
                    SRegisterDate=TM.DateTime(SRegisterDate);

                    Registration.setText(SRegistration);
                    CC.setText(SCC);
                    Km.setText(SKm);
                    Fuel.setText(SFuel);
                    RegisterDate.setText(SRegisterDate);
                    FrontTire.setText(SFrontTire);
                    BackTire.setText(SBackTire);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //typeService.setText("Ups, ocorreu um erro");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = username + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };
        rq.add(jsonObjectRequest);
    }
}
