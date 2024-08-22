package com.example.trackingmypantrygiacomochinilam.ui.pantry;

import android.app.Application;

import com.example.trackingmypantrygiacomochinilam.Item;
import com.example.trackingmypantrygiacomochinilam.ItemRepository;
import com.example.trackingmypantrygiacomochinilam.Utility;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class PantryViewModel extends AndroidViewModel {

    private LiveData<List<Item>> listOfItem; //lista prodotti scaricati dal server
    private MutableLiveData<Boolean> baseFilter = new MutableLiveData<Boolean>(true);
    private MutableLiveData<String> filterQuery = new MutableLiveData<String>();
    private MutableLiveData<List<Object>> filterParams = new MutableLiveData<List<Object>>();
    private MutableLiveData<String> filterText;

    public MutableLiveData<String> itemIdSelected; //prodotto selezionato

    private ItemRepository itemRepository; //repository per gli item nella dispensa locale

    public PantryViewModel(Application application) {
        super(application);

        getFilterText().setValue("Nessun filtro applicato!");

        String dbId = Utility.getIdPref(application);
        itemRepository = new ItemRepository(application, dbId);

        //in base al filtro applicato listOfItem si aggiorna
        listOfItem = Transformations.switchMap(baseFilter,
                new androidx.arch.core.util.Function<Boolean, LiveData<List<Item>>>() {
                    @Override
                    public LiveData<List<Item>> apply(Boolean filterState) {
                        if(filterState){
                            return itemRepository.getListOfItems();
                        }else{
                            return itemRepository.getFilteredQuery(filterQuery.getValue(), filterParams.getValue());
                        }
                    }
                });
    }

    public LiveData<List<Item>> getListOfItem(){
        return listOfItem;
    }
    public void removeQuantityItem(String id){
        itemRepository.removeQuantity(id);
    }
    public void addQuantityItem(String id){
        itemRepository.addQuantity(id);
    }
    public void deleteItem(String id){
        itemRepository.deleteItem(id);
    }
    public void changeExpireDate(String newExpire){
        itemRepository.updateExpireDate(getItemIdSelected().getValue(), newExpire);
    }

    public MutableLiveData<Boolean> getBaseFilter(){
        if (baseFilter == null) {
            baseFilter = new MutableLiveData<Boolean>();
        }
        return baseFilter;
    }
    public MutableLiveData<String> getFilterQuery(){
        if (filterQuery == null) {
            filterQuery = new MutableLiveData<String>();
        }
        return filterQuery;
    }
    public MutableLiveData<List<Object>> getFilterParams(){
        if (filterParams == null) {
            filterParams = new MutableLiveData<List<Object>>();
        }
        return filterParams;
    }

    public MutableLiveData<String> getItemIdSelected(){
        if (itemIdSelected == null) {
            itemIdSelected = new MutableLiveData<String>();
        }
        return itemIdSelected;
    }
    public MutableLiveData<String> getFilterText(){
        if (filterText == null) {
            filterText = new MutableLiveData<String>();
        }
        return filterText;
    }

}