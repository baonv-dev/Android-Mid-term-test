package com.example.kiemtradieukien;

import android.Manifest;
import android.content.Context;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ForgetPassword extends AppCompatActivity {
    public EditText txtMail;
    public TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Button btnQuenMatKhau = findViewById(R.id.btnQuenMatKhau);
        txtMail = findViewById(R.id.txtMail);
        tvResult = findViewById(R.id.tvResult);
        checkPermission();
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtMail.length() > 0)
                {
                    if (checkInternetConnection()) {
                        String urlForgetPass = "https://www.miai.vn/sample/resetpass.php?email=";
                        String email = txtMail.getText().toString();
                        urlForgetPass += email;
                        GetEmail task = new GetEmail(txtMail, tvResult);
                        task.execute(urlForgetPass);
                    }
                }
                else
                {
                    Toast.makeText(ForgetPassword.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
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

            }
        } else {

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
    public class GetEmail extends AsyncTask<String, Void, String>  // AsyncTask<Params, Progress, Result>
    {
        TextView txtEmail, txtResult;
        public GetEmail(TextView txtEmail,TextView txtResult)  {
            this.txtEmail = txtEmail;
            this.txtResult = txtResult;

        }
        @Override
        protected String doInBackground(String... strings) {
            String urlForgetPass = strings[0];
            InputStream in = null;
            BufferedReader br= null;
            try {
                
                URL url = new URL(urlForgetPass);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                int resCode = httpConn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK)
                {
                    in = httpConn.getInputStream();
                    br = new BufferedReader(new InputStreamReader(in));

                    StringBuilder sb= new StringBuilder();
                    String s= null;
                    while((s= br.readLine())!= null)
                    {
                        sb.append(s);
                        sb.append("\n");
                    }
                    return sb.toString();
                }
                else
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    in.close();
                }
                catch (Exception e)
                {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result  != null){
                try{
                    JSONObject json = new JSONObject(result);    // create JSON obj from string
                    String tb = json.getString("result_message");
                    txtResult.setText(tb);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                Log.e("MyMessage", "Failed to fetch data!");
            }
        }

    }
}
