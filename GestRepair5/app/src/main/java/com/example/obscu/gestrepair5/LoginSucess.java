package com.example.obscu.gestrepair5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginSucess extends AppCompatActivity {
    TextView name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sucess);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        Bundle bundle = getIntent().getExtras();
        name.setText("Welcome "+bundle.getString("name"));
        email.setText("email "+bundle.getString("email"));
        ;
    }
}
