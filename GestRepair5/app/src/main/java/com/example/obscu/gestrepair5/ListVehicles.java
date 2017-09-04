package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListVehicles extends AppCompatActivity {

    RequestQueue rq;
    ListView list;
    int n=1;
    String username ="rbarcelos";
    String password ="123qwe";

    ArrayList<String> Vehicles = new ArrayList<String>();
    Ip ip = new Ip();
    String url = ip.stIp() + "/vehicle/"+n+"/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vehicles);
        ListView list = (ListView) findViewById(R.id.lst_Vehicles);
        final TextView barcel = (TextView) findViewById(R.id.textView);
        rq = Volley.newRequestQueue(this);




        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("data");
                    String[][] name = new String[data.length()][3];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject datas = (JSONObject) data.get(i);
                        name[i][0] = datas.getString("nameBrand");
                        name[i][1] = datas.getString("nameModel");
                        name[i][2] = datas.getString("registration");
                        Vehicles.add(name[i][0]+" "+name[i][1]+"\n"+name[i][2]);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListVehicles.this, R.layout.activity_list_vehicles_main, Vehicles);
                    final ListView list = (ListView) findViewById(R.id.lst_Vehicles);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Intent intent = new Intent(ListVehicles.this, Vehicle.class);
                                                        intent.putExtra("position", position);

                                                        startActivity(intent);
                                                    }
                                                });
                    list.setAdapter(adapter);

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
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = username + ":" + password ;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };

        rq.add(jsonObjectRequest);
    }
}
