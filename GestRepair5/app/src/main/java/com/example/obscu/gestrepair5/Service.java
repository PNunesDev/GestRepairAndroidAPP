package com.example.obscu.gestrepair5;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Obscu on 31/07/2017.
 */

public class Service extends Activity {

    RequestQueue rq;

    TextView typeService, priceService, descriptionService, imageService, googlePlusUrlText;

    String name, description, jdescription, jimage, gplusUrl;

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
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
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
        });

        rq.add(jsonObjectRequest);
    }
}

