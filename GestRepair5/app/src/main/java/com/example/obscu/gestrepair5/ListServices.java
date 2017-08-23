package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListServices extends AppCompatActivity{

    RequestQueue rq;
    String name;
    ListView list;

    ArrayList<String> servicedata = new ArrayList<String>();
    Ip ip = new Ip();
    String url = ip.stIp() + "/service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);
        ListView list = (ListView) findViewById(R.id.lst_Service);
        rq = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        name = jsonObject.getString("nameService");
                        servicedata.add(name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListServices.this, R.layout.activity_list_services_main, servicedata);
                    final ListView list = (ListView) findViewById(R.id.lst_Service);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ListServices.this, Service.class);
                            intent.putExtra("ServiceType", list.getItemAtPosition(position).toString());
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getApplicationContext();
                CharSequence text = "Não foi possivel ligar à internet";
                int duration = Toast.LENGTH_LONG;

                String[] name= {"Bate-Chapas","Inspeção Automóvel","Diagonóstico","Alinhamento de suspensão","Suspensão","Pintura","Revisão"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListServices.this, R.layout.activity_list_services_main, name);
                final ListView list = (ListView) findViewById(R.id.lst_Service);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ListServices.this, Service.class);
                        intent.putExtra("ServiceType", list.getItemAtPosition(position).toString());
                        startActivity(intent);
                    }
                });
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

        rq.add(jsonObjectRequest);
    }

}