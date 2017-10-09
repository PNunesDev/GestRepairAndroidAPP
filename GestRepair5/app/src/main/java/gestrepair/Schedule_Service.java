package gestrepair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class Schedule_Service extends AppCompatActivity {

    RequestQueue rq, queue;

    TextView Registration, Service, Date;
    String SRegistration, SService, SDate, SIDScheduleService;
    Button RemoveSchedule;
    boolean confirmbutton=false;

    String username,password, iduser;

    Ip ip = new Ip();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule_service);
        rq = Volley.newRequestQueue(this);
        queue = Volley.newRequestQueue(this);


        Registration = (TextView) findViewById(R.id.txtRegistrationValue);
        Service = (TextView) findViewById(R.id.txt_ServiceValue);
        Date = (TextView) findViewById(R.id.txtDateValue);
        RemoveSchedule = (Button) findViewById(R.id.btn_CancelSchedule);

        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        iduser = Intent.getStringExtra("iduser");
        String url = ip.stIp() + "/schedule/"+iduser;

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

                    SIDScheduleService = jsonObject.getString("idSchedule");
                    SRegistration = jsonObject.getString("vehicle");
                    SService = jsonObject.getString("service");
                    SDate = jsonObject.getString("date");

                    DateTime TM = new DateTime();
                    SDate=TM.DateTime(SDate);

                    Registration.setText(SRegistration);
                    Service.setText(SService);
                    Date.setText(SDate);

                    RemoveSchedule.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                String url = ip.stIp() + "/schedule/disable/" + SIDScheduleService;

                                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // response

                                                String[] data = new String[1];
                                                data[0] = iduser;
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Context context = getApplicationContext();
                                                CharSequence text = "Error!";
                                                int duration = Toast.LENGTH_LONG;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", iduser);
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
                                            Log.d("Tag", "denied");
                                        }

                                        return super.getHeaders();
                                    }
                                };

                                queue.add(postRequest);
                        }
                    });

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                String value = data.getStringExtra("param");
            }
        }
    }

}



