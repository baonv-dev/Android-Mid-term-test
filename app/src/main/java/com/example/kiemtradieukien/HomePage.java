package com.example.kiemtradieukien;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String old_pass = intent.getStringExtra("old_pass");

                Intent go_to_change_pass = new Intent(HomePage.this, ChangePass.class);
                go_to_change_pass.putExtra("old_pass",old_pass);
                startActivity(go_to_change_pass);
            }
        });
    }
}
