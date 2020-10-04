package com.example.kiemtradieukien;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnDangNhap = findViewById(R.id.btnDangNhap);
        Button btnDangKy = findViewById(R.id.btnDangKy);
        Button btnQuenMK = findViewById(R.id.btnQuenMatKhau);

        final EditText txtUser = findViewById(R.id.edUser);
        final EditText txtPass = findViewById(R.id.edPass);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUser.getText().toString().equals("admin") && txtPass.length()>0)
                {
                    Intent go_to_homepage = new Intent(MainActivity.this, HomePage.class);
                    go_to_homepage.putExtra("old_pass",txtPass.getText().toString());
                    startActivity(go_to_homepage);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_register = new Intent(Intent.ACTION_VIEW, Uri.parse("http://miai.vn/sample/reg.php"));
                startActivity(go_to_register);
            }
        });
        btnQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_foget_pass = new Intent(MainActivity.this, ForgetPassword.class);
                startActivity(go_to_foget_pass);
            }
        });

    }
}
