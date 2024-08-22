package com.example.trackingmypantrygiacomochinilam;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<String> id;
    private MutableLiveData<String> user;
    private MutableLiveData<String> email;

    public MainViewModel(Application application) {
        super(application);
    }

    public String getId(){
        return id.getValue();
    }
    public String getUser(){
        return user.getValue();
    }
    public String getEmail(){
        return email.getValue();
    }

    public void setData(String i, String us, String ml){
        id = new MutableLiveData<String>(i);
        user = new MutableLiveData<String>(us);
        email = new MutableLiveData<String>(ml);
    }

    public boolean userDataIsSet(){
        return (!(id==null || user == null || email==null));
    }

}
