package com.android.giveandtake.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.giveandtake.R;

public class AdminConnect extends AppCompatActivity {

    Button showuser;
    Button showpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_connect);

        showuser=(Button)findViewById(R.id.View_User);
        showpost=(Button)findViewById(R.id.allpostadmin);

        showuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Show_User=new Intent(AdminConnect.this,ShowUsers.class);
                startActivity(Show_User);
            }
        });

showpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Show_User=new Intent(AdminConnect.this,PostAdmin.class);
                startActivity(Show_User);
            }
        });


    }



}


