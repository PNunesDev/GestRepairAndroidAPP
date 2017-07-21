package com.example.obscu.gestrepair4;

import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    RequestQueue rq;

    TextView typeService, priceService, descriptionService, imageService, googlePlusUrlText;

    String name, description, jdescription, jimage, gplusUrl;

    Ip ip = new Ip();
    String url= ip.stIp()+"/service";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
