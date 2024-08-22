package com.example.trackingmypantrygiacomochinilam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private CheckBox remember;
    private Button login, needAcc;

    private Boolean rememberMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        login = findViewById(R.id.loginBtn);
        needAcc = findViewById(R.id.needAccBtn);

        //Funzione per il pulsante di login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginRequestCall(email.getText().toString(), password.getText().toString());
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, R.string.errorLogin, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Funzione di navigazione per aprire la create account activity
        needAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i);
                finish();
            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compBtn, boolean isChecked) {
                if(compBtn.isChecked()){
                    rememberMe = true;
                }else if(!compBtn.isChecked()){
                    rememberMe = false;
                }
            }
        });
    }

    //Funzione per chiamata al server di login
    public void loginRequestCall(String mail, String psw) throws JSONException {
        JSONObject req = new JSONObject();
        req.put("email", mail);
        req.put("password", psw);

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.LOGIN_URL, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setLoginPref(response);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, R.string.errorLogin, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, R.string.errorLogin, Toast.LENGTH_SHORT).show();
            }
        });
        Thread thread = new Thread(){
            @Override
            public void run(){
                requestQueue.add(jsonObjectRequest);
            }
        };
        thread.start();
    }

    public void setLoginPref(JSONObject res) throws JSONException {
        String token = res.getString("accessToken");

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constants.USER_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setPref(response, token);
                    Toast.makeText(LoginActivity.this, R.string.okLogin, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, R.string.errorLogin, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, R.string.errorLogin, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer "+token;
                Map<String, String> headers = new ArrayMap<String, String>();
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        Thread thread = new Thread(){
            @Override
            public void run(){
                requestQueue.add(jsonRequest);
            }
        };
        thread.start();
    }

    //funzione che gestisce le preferenze
    public void setPref(JSONObject res, String token) throws JSONException {
            String email = res.getString("email");
            String user = res.getString("username");
            String id = res.getString("id");

            SharedPreferences sharedPreferences = getSharedPreferences(res.getString("id"), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(sharedPreferences.contains("ID_PREF")){
                editor.putString("ACCESS_TOKEN_PREF", token);
            }else{
                editor.putInt("LAST_ACCESS_PREF", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                editor.putString("ID_PREF", res.getString("id"));
                editor.putString("USERNAME_PREF", res.getString("username"));
                editor.putString("EMAIL_PREF", res.getString("email"));
                editor.putString("CREATED_DATE_PREF", res.getString("createdAt"));
                editor.putString("UPDATED_DATE_PREF", res.getString("updatedAt"));
                editor.putString("ACCESS_TOKEN_PREF", token);

                editor.putInt("QUANTITY_PREF", 1);
                editor.putInt("QUALITY_PREF", 3);
                editor.putInt("GG_PREF", 0);
                editor.putInt("MM_PREF", 1);
                editor.putInt("YY_PREF", 0);
            }
            editor.commit();

            SharedPreferences sP = getSharedPreferences("REMEMBER_PREF", MODE_PRIVATE);
            SharedPreferences.Editor ed = sP.edit();
            if(rememberMe){
                ed.putString("REMEMBER_ID_PREF", res.getString("id"));
            }else{
                ed.putString("REMEMBER_ID_PREF", "");
            }
            ed.putString("CURRENT_ID_PREF", res.getString("id"));
            ed.putString("FIRST_ACCESS_PREF", "NO");
            ed.commit();

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("email", email);
            i.putExtra("user", user);
            i.putExtra("id", id);
            startActivity(i);
            finish();
    }
}