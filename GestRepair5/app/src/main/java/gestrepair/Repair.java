package gestrepair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
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

    TextView txtRegistration, txtPrice, txtDiagnosis, txtRepairData,txtStatus, txtEntry, txtOut;

    String SRegistration, SPrice, SDiagnosis, SRepairData, SStatus, SEntry, SOut;


    String username,password, iduser;

    Ip ip = new Ip();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        rq = Volley.newRequestQueue(this);

        txtRegistration = (TextView) findViewById(R.id.txt_Repair_Registration_Value);
        txtPrice = (TextView) findViewById(R.id.txt_Repair_Price_Value);
        txtDiagnosis = (TextView) findViewById(R.id.txt_Repair_UserDiagnosis_Value);
        txtRepairData = (TextView) findViewById(R.id.txt_Repair_RepairData_Value);
        txtStatus = (TextView) findViewById(R.id.txt_Repair_Status_Value);
        txtEntry = (TextView) findViewById(R.id.txt_Repair_Entry_Value);
        txtOut = (TextView) findViewById(R.id.txt_Repair_Out_Value);

        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        iduser = Intent.getStringExtra("iduser");
        String url= ip.stIp()+"/repair/user/"+iduser;

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
                    SRegistration = jsonObject.getString("vehicle");
                    SPrice = jsonObject.getString("price");
                    SDiagnosis = jsonObject.getString("description");
                    SRepairData = jsonObject.getString("information");
                    SStatus = jsonObject.getString("state");
                    SEntry = jsonObject.getString("startDate");
                    SOut = jsonObject.getString("finishDate");

                    DateTime TM = new DateTime();
                    SEntry=TM.DateTime(SEntry);
                    SOut=TM.DateTime(SOut);


                    txtRegistration.setText(SRegistration);
                    txtPrice.setText(SPrice);
                    txtDiagnosis.setText(SDiagnosis);
                    txtRepairData.setText(SRepairData);
                    txtStatus.setText(SStatus);
                    txtEntry.setText(SEntry);
                    txtOut.setText(SOut);

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


