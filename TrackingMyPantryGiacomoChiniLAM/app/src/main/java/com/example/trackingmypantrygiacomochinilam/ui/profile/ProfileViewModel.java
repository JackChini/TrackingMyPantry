package com.example.trackingmypantrygiacomochinilam.ui.profile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.trackingmypantrygiacomochinilam.Utility;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<String> id;
    private MutableLiveData<String> user;
    private MutableLiveData<String> email;
    private MutableLiveData<String> created;

    public ProfileViewModel(Application application) {
        super(application);

        setProfileInformation(application);
    }

    //imposta le preference per far si che al prossimo avvio chieda il login
    public void onLogoutAction(Context context){
        SharedPreferences pf = context.getSharedPreferences("REMEMBER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pf.edit();
        ed.putString("REMEMBER_ID_PREF", "");
        ed.putString("CURRENT_ID_PREF", "");
        ed.commit();
        Toast.makeText(context, "LOGOUT", Toast.LENGTH_SHORT).show();
    }


    public void setProfileInformation(Context context){
        SharedPreferences pf = context.getSharedPreferences(Utility.getIdPref(context), Context.MODE_PRIVATE);
        getId().setValue(pf.getString("ID_PREF", ""));
        getUser().setValue(pf.getString("USERNAME_PREF", ""));
        getEmail().setValue(pf.getString("EMAIL_PREF", ""));
        getCreated().setValue(pf.getString("CREATED_DATE_PREF", ""));
    }

    public MutableLiveData<String> getId(){
        if (id == null) {
            id = new MutableLiveData<String>();
        }
        return id;
    }
    public MutableLiveData<String> getUser(){
        if (user == null) {
            user = new MutableLiveData<String>();
        }
        return user;
    }
    public MutableLiveData<String> getEmail(){
        if (email == null) {
            email = new MutableLiveData<String>();
        }
        return email;
    }
    public MutableLiveData<String> getCreated(){
        if (created == null) {
            created = new MutableLiveData<String>();
        }
        return created;
    }

}
