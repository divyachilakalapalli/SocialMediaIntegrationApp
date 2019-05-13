package com.example.divya.sparkssmiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CircleImageView circleImageView;
        TextView friends,txtEmail,txtBirth,gender,fullname;

        fullname=findViewById(R.id.fullname);
        friends = findViewById(R.id.friends);
        gender=findViewById(R.id.gender);
        txtEmail = findViewById(R.id.profile_email);
        txtBirth = findViewById(R.id.birth_day);
        circleImageView = findViewById(R.id.profile_pic);

        Bundle b=getIntent().getExtras();
        fullname.setText(b.getString("fname1"));
        friends.setText(b.getString("friend1"));
        gender.setText(b.getString("sex1"));
        txtEmail.setText(b.getString("mail1"));
        txtBirth.setText(b.getString("birth1"));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        Glide.with(Main2Activity.this).load(b.getString("image")).into(circleImageView);


    }

}
