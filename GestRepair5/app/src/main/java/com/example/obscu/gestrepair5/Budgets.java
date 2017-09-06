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

public class Budgets extends AppCompatActivity {

    RequestQueue rq;

    TextView txtRegistration, txtState, txtPrice, txtProcess, txtRepair;

    String SRegistration, SState, SPrice, SProcess, SRepair;

   // String username="rbarcelos";
    //String password="123qwe";
    String username, password;


    Ip ip = new Ip();
    String url= ip.stIp()+"/budget/1";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);
        rq = Volley.newRequestQueue(this);


        txtRegistration = (TextView) findViewById(R.id.txt_RegistrationValue);
        txtState = (TextView) findViewById(R.id.txt_StatValue);
        txtPrice = (TextView) findViewById(R.id.txt_PriceValue);
        txtProcess= (TextView) findViewById(R.id.txt_StateValue);
        txtRepair = (TextView) findViewById(R.id.txt_RepairTimeValue);

        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        Log.i("TAG",username+" - "+password+" TESTE");

        sendjsonrequest();
    }

    public void sendjsonrequest(){

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
                    SRegistration = jsonObject.getString("vehicle");
                    SState = jsonObject.getString("state");
                    SPrice = jsonObject.getString("price");
                    SProcess = jsonObject.getString("processOpen");
                    SRepair = jsonObject.getString("repairTime");

                    DateTime TM = new DateTime();
                    //SRepair=TM.DateTime(SRepair);
                    //Log.i("TAG","SRepair...."+SRepair);


                    /*jdescription = jsonObject.getString("priceService");
                    jdescription = jsonObject.getString("description");
                    jimage=jsonObject.getString("photo");*/

                    txtRegistration.setText(SRegistration);
                    txtState.setText(SState);
                    txtPrice.setText(SPrice);
                    txtProcess.setText(TM.DateTime(SProcess));
                    txtRepair.setText(SRepair+" dias");
                   /* priceService.setText(jdescription);
                    descriptionService.setText(jdescription);
                    imageService.setText(jimage);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //typeService.setText("Ups, ocorreu um erro");

            }
        })
        {
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

