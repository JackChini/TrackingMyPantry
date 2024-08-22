package com.example.trackingmypantrygiacomochinilam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SplashTheme);
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("REMEMBER_PREF", MODE_PRIVATE);
        String directAccess = sharedPreferences.getString("REMEMBER_ID_PREF","");
        String firstAccess = sharedPreferences.getString("FIRST_ACCESS_PREF", "YES");

        Intent i;
        if(firstAccess.equals("YES")){
            //caso in cui si faccia primo accesso all'app
            i = new Intent(SplashActivity.this, CreateAccountActivity.class);
        }else{
            //controllo se è presente un account che ha mantenuto l'accesso oppure no
            if(directAccess.equals("")){
                i = new Intent(SplashActivity.this, LoginActivity.class);
            }else{
                SharedPreferences getInfo = getSharedPreferences(directAccess, MODE_PRIVATE);
                Integer last = getInfo.getInt("LAST_ACCESS_PREF", 1);
                //controllo che il token sia più recente di 5 giorni (tt chiede il login, ff continua)
                if(Utility.needAuthentication(last)){
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }else{
                    String email = getInfo.getString("EMAIL_PREF", "");
                    String user = getInfo.getString("USERNAME_PREF", "");
                    String id = getInfo.getString("ID_PREF", "");
                    i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("email", email);
                    i.putExtra("user", user);
                    i.putExtra("id", id);
                }
            }
        }
        startActivity(i);
        finish();
    }


}