package gestrepair;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SetScheduleService extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    RequestQueue rq,rq2;
    String name;
    ListView list;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    String username,password,iduser;
    Button btnDate, btnHour;
    EditText etxtDate, etxtHour;
    int year, month, day, hour, minutes;
    int yearfinal, monthfinal, dayfinal, hourfinal, minutesfinal;

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";




    ArrayList<String> Vehicles = new ArrayList<String>();
    ArrayList<String> Service = new ArrayList<String>();
    ArrayList<String> Hours = new ArrayList<String>();
    Ip ip = new Ip();
    String url2 = ip.stIp() + "/service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_schedule__service);
        rq = Volley.newRequestQueue(this);
        rq2 = Volley.newRequestQueue(this);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnHour = (Button) findViewById(R.id.btn_Hour);
        etxtDate = (EditText) findViewById(R.id.etxtDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SetScheduleService.this, SetScheduleService.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });


        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        iduser = Intent.getStringExtra("iduser");
        String url = ip.stIp() + "/vehicle/"+iduser+"/user";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("data");
                    String[][] name = new String[data.length()][3];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject datas = (JSONObject) data.get(i);
                        name[i][2] = datas.getString("registration");
                        Vehicles.add(name[i][2]);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetScheduleService.this, R.layout.activity_list_vehicles_main, Vehicles);
                    Spinner spinnerVehicles = (Spinner) findViewById(R.id.spn_Vehicle);
                    spinnerVehicles.setAdapter(adapter);

                    spinnerVehicles.setAdapter(adapter);

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



        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = (JSONArray) response.get("data");
                    String[][] name = new String[data.length()][3];
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject datas = (JSONObject) data.get(i);
                        name[i][0] = datas.getString("nameService");
                        Service.add(name[i][0]);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetScheduleService.this, R.layout.activity_list_vehicles_main, Service);
                    Spinner spinnerService = (Spinner) findViewById(R.id.spn_Service);
                    spinnerService.setAdapter(adapter);

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
        });

        rq2.add(jsonObjectRequest2);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        yearfinal = year;
        monthfinal = month+1;
        dayfinal = dayOfMonth;

        Calendar cal = Calendar.getInstance();
        hour=cal.get(Calendar.HOUR_OF_DAY);
        minutes=cal.get(Calendar.MINUTE);
        etxtDate.setText(dayfinal+"/"+monthfinal+"/"+dayfinal);

        for (int i = 8; i <=12; i++) {
            Hours.add(i+"");
        }
        for (int i = 14; i <=17; i++) {
            Hours.add(i+"");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetScheduleService.this, R.layout.activity_list_vehicles_main, Hours);
        Spinner spinnerService = (Spinner) findViewById(R.id.spn_hours);
        spinnerService.setAdapter(adapter);
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
