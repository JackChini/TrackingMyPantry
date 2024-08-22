package com.example.trackingmypantrygiacomochinilam.ui.settings;

import android.app.Application;
import com.example.trackingmypantrygiacomochinilam.ItemRepository;
import com.example.trackingmypantrygiacomochinilam.Utility;

import androidx.lifecycle.AndroidViewModel;

public class SettingsViewModel extends AndroidViewModel {

    private ItemRepository itemRepository;

    public SettingsViewModel(Application application) {
        super(application);

        String dbId = Utility.getIdPref(application);
        itemRepository = new ItemRepository(application, dbId);
    }

    public void emptyDb(){
        itemRepository.deleteAll();
    }
    public void disableAccess(){
        Utility.disableAutomatedAccess(getApplication());
    }

    public void updateQuantity(Integer value){
        Utility.setQuantityPref(getApplication(), value);
    }
    public void updateQuality(Integer value){
        Utility.setQualityPref(getApplication(), value);
    }
    public void updateGG(Integer value){
        Utility.setGGPref(getApplication(), value);
    }
    public void updateMM(Integer value){
        Utility.setMMPref(getApplication(), value);
    }
    public void updateYY(Integer value){
        Utility.setYYPref(getApplication(), value);
    }

    public void setResultFromDialog(String type, Integer value){
        switch (type){
            case "Q":
                updateQuantity(value);
                break;
            case "R":
                updateQuality(value);
                break;
            case "GG":
                updateGG(value);
                break;
            case  "MM":
                updateMM(value);
                break;
            case "YY":
                updateYY(value);
                break;
            default:
                break;
        }
    }

}