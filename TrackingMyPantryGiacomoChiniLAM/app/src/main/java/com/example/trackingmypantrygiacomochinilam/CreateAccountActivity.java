package com.example.trackingmypantrygiacomochinilam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText username, email, password;
    private Button create, needLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = findViewById(R.id.createUsername);
        email = findViewById(R.id.createEmail);
        password = findViewById(R.id.createPassword);
        create = findViewById(R.id.creaAccBtn);
        needLogin = findViewById(R.id.needLoginBtn);

        //Pulsante di creazione account
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createAccountCall(username.getText().toString(), email.getText().toString(), password.getText().toString());
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.errorCreate, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Pulsante di navigazione per aprire la login activity
        needLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Funzione che fa la chiamata al server per creare un account
    public void createAccountCall(String user, String mail, String psw) throws JSONException {
        JSONObject req = new JSONObject();
        req.put("username", user);
        req.put("email", mail);
        req.put("password", psw);

        RequestQueue requestQueue = Volley.newRequestQueue(CreateAccountActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.CREATE_ACCOUNT_URL, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    createAccountPref(response);

                    Toast.makeText(getApplicationContext(), R.string.okCreate, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.errorCreate, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.errorCreate, Toast.LENGTH_SHORT).show();
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

    //Funzione che crea le preferences per un account, viene chiamata nel caso di success della chiamata al server di creazione account
    public void createAccountPref(JSONObject res) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences(res.getString("id"), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID_PREF", res.getString("id"));
        editor.putString("USERNAME_PREF", res.getString("username"));
        editor.putString("EMAIL_PREF", res.getString("email"));
        editor.putString("CREATED_DATE_PREF", res.getString("createdAt"));
        editor.putString("UPDATED_DATE_PREF", res.getString("updatedAt"));

        editor.putInt("QUANTITY_PREF", 1);
        editor.putInt("QUALITY_PREF", 3);
        editor.putInt("GG_PREF", 0);
        editor.putInt("MM_PREF", 1);
        editor.putInt("YY_PREF", 0);
        editor.commit();

        SharedPreferences sP = getSharedPreferences("REMEMBER_PREF", MODE_PRIVATE);
        SharedPreferences.Editor ed = sP.edit();
        editor.putString("FIRST_ACCESS_PREF", "NO");
        ed.commit();
    }

}