package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestQueue rq;
    Ip ip = new Ip();
    String url = ip.stIp()+"/login";
    TextView typeService, priceService, descriptionService, imageService, googlePlusUrlText,txtMainUsr;

    String name, description, jdescription, jimage, gplusUrl, txtTitle,username,password,response, iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent Intent = getIntent();
        username = Intent.getStringExtra("username");
        password = Intent.getStringExtra("password");
        response = Intent.getStringExtra("response");
        if(response!=null) {
            iduser = response.substring(response.indexOf("idUser") + 2);
            iduser=iduser.substring(5,10);
            iduser=iduser.replaceAll("[^\\.0123456789]","");
        }

       /* if(response!=null)
        response.substring(response.indexOf("idUser:") + 1 , response.length());*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Custom
        rq = Volley.newRequestQueue(this);

        typeService = (TextView) findViewById(R.id.ServiceType);
        priceService = (TextView) findViewById(R.id.ServicePrice);
        descriptionService = (TextView) findViewById(R.id.ServiceDescription);
        imageService = (TextView) findViewById(R.id.ServiceImage);
        txtMainUsr = (TextView) findViewById(R.id.txt_MainUser);

        txtMainUsr.setText(username);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG","Lhurz");
                try {
                    JSONArray data = (JSONArray) response.get("data");

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //1 - Lista serviços
        if (id == R.id.ListServices) {
            url= ip.stIp()+"/service";
            Intent i = new Intent(MainActivity.this, ListServices.class);
            startActivity(i);

        //2- Autenticação
        } else if (id == R.id.Login) {
            //url= ip.stIp()+"/login";
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);

            // Daqui para baixo todas as opções precisam de autenticação
         //3- Listar Veículos
        } else if (id == R.id.ListVehicles) {
            Intent i = new Intent(MainActivity.this, ListVehicles.class);
            String[] data = new String[3];
            data[0] = username;
            data[1] = password;
            data[2] = iduser;
            Bundle bundle = new Bundle();
            i.putExtra("username", data[0]);
            i.putExtra("password", data[1]);
            i.putExtra("iduser", data[2]);
            i.putExtras(bundle);
            startActivity(i);

        //4 - Ver Agendamentos
        } else if (id == R.id.ListSchedules) {
            Intent i = new Intent(MainActivity.this, ListScheduleService.class);
            String[] data = new String[2];
            data[0] = username;
            data[1] = password;
            Bundle bundle = new Bundle();
            i.putExtra("username", data[0]);
            i.putExtra("password", data[1]);
            i.putExtras(bundle);
            startActivity(i);

        //5 - Agendar Serviços
        } else if (id == R.id.ScheduleService) {
            Intent i = new Intent(MainActivity.this, SetScheduleService.class);
            String[] data = new String[2];
            data[0] = username;
            data[1] = password;
            Bundle bundle = new Bundle();
            i.putExtra("username", data[0]);
            i.putExtra("password", data[1]);
            i.putExtras(bundle);
            startActivity(i);

        //6 - Ver Reparação
        } else if (id == R.id.ListRepairs) {
            Intent i = new Intent(MainActivity.this, ListRepair.class);
            String[] data = new String[2];
            data[0] = username;
            data[1] = password;
            Bundle bundle = new Bundle();
            i.putExtra("username", data[0]);
            i.putExtra("password", data[1]);
            i.putExtras(bundle);
            startActivity(i);

        //7 - Ver Orçamentos
        } else if (id == R.id.ListBudgets) {
            Intent i = new Intent(MainActivity.this, ListBudgets.class);
            String[] data = new String[2];
            data[0] = username;
            data[1] = password;
            Bundle bundle = new Bundle();
            i.putExtra("username", data[0]);
            i.putExtra("password", data[1]);
            i.putExtras(bundle);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
