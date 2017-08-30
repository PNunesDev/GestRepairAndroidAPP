package com.example.obscu.gestrepair5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

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

public class Repair extends AppCompatActivity {

    RequestQueue rq;

    TextView typeService, priceService, descriptionService, imageService, googlePlusUrlText;

    String name, description, jdescription, jimage, gplusUrl;
    String username = "rbarcelos";
    String password = "123qwe";
    Ip ip = new Ip();
    String url= ip.stIp()+"/service";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);
        rq = Volley.newRequestQueue(this);

        typeService = (TextView) findViewById(R.id.ServiceType);
        priceService = (TextView) findViewById(R.id.ServicePrice);
        descriptionService = (TextView) findViewById(R.id.ServiceDescription);
        imageService = (TextView) findViewById(R.id.ServiceImage);

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
                    name = jsonObject.getString("nameService");
                    jdescription = jsonObject.getString("priceService");
                    jdescription = jsonObject.getString("description");
                    jimage=jsonObject.getString("photo");

                    typeService.setText(name);
                    priceService.setText(jdescription);
                    descriptionService.setText(jdescription);
                    imageService.setText(jimage);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                typeService.setText("Ups, ocorreu um erro");

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "username" + ":" + "password";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };
        rq.add(jsonObjectRequest);
    }
}
