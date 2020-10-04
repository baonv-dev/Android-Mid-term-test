package com.example.kiemtradieukien;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChangePass extends AppCompatActivity {
    public TextView txtResult;
    public String old_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Button btnChangePass = findViewById(R.id.btnChangePass);
        final EditText txtNewPass = findViewById(R.id.txtNewPass);
        txtResult = findViewById(R.id.txtResult);
        checkPermission();
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                old_pass = intent.getStringExtra("old_pass");
                if(txtNewPass.length() > 0)
                {
                    String urlChangePass = "https://www.miai.vn/sample/updatepass.php";
                    ChangePassPost task =  new ChangePassPost(txtNewPass, txtResult);
                    task.execute(urlChangePass);
                }
                else
                {
                    Toast.makeText(com.example.kiemtradieukien.ChangePass.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void checkPermission() {
        Log.d("PERM", "RUN");
        // Send SMS to 5556

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int p4 = checkSelfPermission(Manifest.permission.INTERNET);
            int p5 = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

            List<String> permissions = new ArrayList<String>();


            if (p4 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (p5 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 9999);
            } else {
                //TODO

            }
        } else {
            //TODO
        }
    }
    private boolean checkInternetConnection() {
        // Get Connectivity Manager
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Details about the currently active default data network
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(this, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Network is not connected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }
    public class ChangePassPost extends AsyncTask<String, Void, String>  // AsyncTask<Params, Progress, Result>
    {
        EditText txtNewPass;
        TextView txtResult;

        public ChangePassPost(EditText txtNewPass, TextView txtResult) {
            this.txtNewPass = txtNewPass;
            this.txtResult = txtResult;
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlChangePass = strings[0];
            String result = "";
            try {
                URL url = new URL(urlChangePass);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("old_pass", old_pass);
                jsonParam.put("new_pass", txtNewPass.getText().toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                Log.e("bao199",jsonParam.toString());
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                if(code==200){
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("phuc",result);
            if (result != null) {
                try {
                    JSONObject json = new JSONObject(result);    // create JSON obj from string
                    String tb = json.getString("result_message");
                    txtResult.setText(tb)   ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("MyMessage", "Failed to fetch data!");
            }
        }
    }
}
