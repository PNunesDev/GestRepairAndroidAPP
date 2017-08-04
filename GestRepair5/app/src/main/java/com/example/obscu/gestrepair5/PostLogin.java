package com.example.obscu.gestrepair5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import junit.framework.Test;

public class PostLogin extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        Context context = getApplicationContext();
        CharSequence text = password;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        }


    }
/*
    public String PostLogin(){

        String Test="";
        Bundle extras = getIntent().getExtras();
        Test= extras.getString(response);

        return Test;
    }
    */






