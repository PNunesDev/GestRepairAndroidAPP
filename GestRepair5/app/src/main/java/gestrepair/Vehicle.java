package gestrepair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vehicle extends AppCompatActivity {
    RequestQueue rq, queue;

    TextView Registration, CC, Km, Fuel, RegisterDate, FrontTire, BackTire,  txtRegistration, txtIdVehicle, txtVehicle;
    String SRegistration, SCC, SKm, SFuel, SRegisterDate, SFrontTire, SBackTire, SIdVehicle, SVehicleModel, SVehicleBrand;
    Button RemoveVehicle;

    ArrayList<String> info;

    String username, password, iduser, dataJ;

    Ip ip = new Ip();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_vehicle);
        rq = Volley.newRequestQueue(this);
        queue = Volley.newRequestQueue(this);

        txtVehicle = (TextView) findViewById(R.id.txt_Vehicle);
       // txtIdVehicle = (TextView) findViewById(R.id.txtIdVehicle);
        Registration = (TextView) findViewById(R.id.txtRegistrationValue);
        txtRegistration = (TextView) findViewById(R.id.txt_StatValue);
        CC = (TextView) findViewById(R.id.txtCCValue);
        Km = (TextView) findViewById(R.id.txtKMValue);
        Fuel = (TextView) findViewById(R.id.txtFuelValue);
        RegisterDate = (TextView) findViewById(R.id.txtDateRegisterValue);
        FrontTire = (TextView) findViewById(R.id.txtFrontTireValue);
        BackTire = (TextView) findViewById(R.id.txtBackTireValue);
        RemoveVehicle = (Button) findViewById(R.id.btnRemoveVehicle);

        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        iduser = Intent.getStringExtra("iduser");
        dataJ="";

        String url = ip.stIp() + "/vehicle/" + iduser + "/user";
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
                    final JSONObject jsonObject = (JSONObject) jsonArray.get(intValue);

                    SVehicleBrand = jsonObject.getString("nameBrand");
                    SVehicleModel = jsonObject.getString("nameModel");
                    SRegistration = jsonObject.getString("registration");
                    SIdVehicle=jsonObject.getString("idVehicle");
                    SCC = jsonObject.getString("displacement");
                    SKm = jsonObject.getString("kilometers");
                    SFuel = jsonObject.getString("nameFuel");
                    SRegisterDate = jsonObject.getString("date");
                    SFrontTire = jsonObject.getString("fronttiresize");
                    SBackTire = jsonObject.getString("reartiresize");

                    DateTime TM = new DateTime();
                    SRegisterDate=TM.DateTime(SRegisterDate);

                    txtVehicle.setText(SVehicleBrand+" - "+SVehicleModel);
                    Registration.setText(SRegistration);
                    CC.setText(SCC);
                    //txtIdVehicle.setText(SIdVehicle);
                    Km.setText(SKm);
                    Fuel.setText(SFuel);
                    RegisterDate.setText(SRegisterDate);
                    FrontTire.setText(SFrontTire);
                    BackTire.setText(SBackTire);

                    RemoveVehicle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = ip.stIp() + "/vehicle/" + SIdVehicle;
                           final String dataJ = jsonObject.toString();

                            StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // response

                                            String[] data = new String[2];
                                            data[0] = iduser;
                                            data[1] = dataJ;
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", iduser);
                                    params.put("vehicle", dataJ);
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
                                        Log.d("Tag","denied");
                                    }

                                    return super.getHeaders();
                                }
                            };

                            queue.add(postRequest);
                        }
                    });
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent= new Intent();
                intent.putExtra("param", "value");
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
