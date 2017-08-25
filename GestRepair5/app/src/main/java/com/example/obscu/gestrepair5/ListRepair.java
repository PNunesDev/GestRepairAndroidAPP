package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListRepair extends AppCompatActivity {

    RequestQueue rq;
    String name;
    ListView list;

    ArrayList<String> listRepair = new ArrayList<String>();
    Ip ip = new Ip();
    String url = ip.stIp() + "/vehicle/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_repair);
        ListView list = (ListView) findViewById(R.id.lst_Repair);
        rq = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            name = jsonObject.getString("idVehicle");
                            listRepair.add(name);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListRepair.this, R.layout.activity_repair_main, listRepair);
                        final ListView list = (ListView) findViewById(R.id.lst_Repair);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(ListRepair.this, Repair.class);
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

                    String[] name= {"Bate-Chapas Not","Inspeção Automóvel","Diagonóstico","Alinhamento de suspensão","Suspensão","Pintura","Revisão"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListRepair.this, R.layout.activity_repair, name);
                    final ListView list = (ListView) findViewById(R.id.lst_Repair);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ListRepair.this, Repair.class);
                            intent.putExtra("ServiceType", list.getItemAtPosition(position).toString());
                            startActivity(intent);
                        }
                    });
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }){

                    public Map<String, String> getHeaders() throws AuthFailureError {
                    try {
                        String username = "";
                        String password = "";
                        Intent i = getIntent();
                        Bundle extras=i.getExtras();

                        if(extras != null)  //this line is necessary for getting any value
                        {
                            String[] cred = i.getStringArrayExtra("cred");

                            username = cred[0];
                            password = cred[1];
                        }
                        Map<String, String> map = new HashMap<String, String>();
                        String key = "Authorization";
                        String encodedString = Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
                        String value = String.format("Basic %s", encodedString);
                        map.put(key, value);
                        return map;
                    } catch (Exception e) {

                    }
                    return super.getHeaders();
                };


            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        rq.add(jsonObjectRequest);
    }

}

