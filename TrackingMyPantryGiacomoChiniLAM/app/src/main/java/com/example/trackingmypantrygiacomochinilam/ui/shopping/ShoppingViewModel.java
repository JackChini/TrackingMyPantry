package com.example.trackingmypantrygiacomochinilam.ui.shopping;

import android.app.Application;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.trackingmypantrygiacomochinilam.Item;
import com.example.trackingmypantrygiacomochinilam.ItemRepository;
import com.example.trackingmypantrygiacomochinilam.Product;
import com.example.trackingmypantrygiacomochinilam.R;
import com.example.trackingmypantrygiacomochinilam.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ShoppingViewModel extends AndroidViewModel {

    private MutableLiveData<List<Integer>> positionList;
    private MutableLiveData<List<Product>> shoppingList;
    private LiveData<List<Item>> listLiveData;
    private MutableLiveData<Integer> count;
    private MutableLiveData<String> file;
    private MutableLiveData<Boolean> created;

    private ItemRepository itemRepository;

    public ShoppingViewModel(Application application) {
        super(application);

        String dbId = Utility.getIdPref(application);
        itemRepository = new ItemRepository(application, dbId);

        listLiveData = itemRepository.getListOfItems();
    }

    public LiveData<List<Item>> getListLiveData(){
        return listLiveData;
    }

    public MutableLiveData<List<Product>> getShoppingList(){
        if (shoppingList == null) {
            shoppingList = new MutableLiveData<List<Product>>();
        }
        return shoppingList;
    }
    public MutableLiveData<List<Integer>> getPositionList(){
        if (positionList == null) {
            positionList = new MutableLiveData<List<Integer>>();
        }
        return positionList;
    }
    public MutableLiveData<Integer> getCount(){
        if (count == null) {
            count = new MutableLiveData<Integer>();
        }
        return count;
    }
    public MutableLiveData<String> getFile(){
        if (file == null) {
            file = new MutableLiveData<String>();
        }
        return file;
    }
    public MutableLiveData<Boolean> getCreated(){
        if (created == null) {
            created = new MutableLiveData<Boolean>();
        }
        return created;
    }

    public void updateData(){
        getPositionList().setValue(new ArrayList<>());
        getCount().setValue(0);
        getCreated().setValue(false);
        getFile().setValue("tmp_Lista_Della_Spesa");
    }

    //ritorna tt se l'elemento è presente ff se non c'è
    public Boolean checkChoice(Integer n, List<Integer> list){
        for(Integer i : list){
            if(i == n){
                return true;
            }
        }
        return false;
    }

    public List<Product> convertItemToProduct(List<Item> listToConvert){
        List<Product> choseList = new ArrayList<>();
        if(!listToConvert.isEmpty()){
            for(Item i : listToConvert){
                choseList.add(new Product(i.getId(), i.getName(), i.getBarcode(), i.getDescription(), i.getImg()));
            }
        }
        return choseList;
    }

    public String generateShoppingList(){
        String shoppingString = "";
        for(Integer i : getPositionList().getValue()){
            shoppingString += "- " + getShoppingList().getValue().get(i).getName() + ". \n";
        }
        return shoppingString;
    }

    public String generateShoppingListFromEmpty(){
        String shoppingString = "";
        List<Item> emptyList = itemRepository.getEmptyItemsList();
        for(Item i : emptyList){
            shoppingString += "- " + i.getName() + ". \n";
        }
        return shoppingString;
    }

    //return tt se è vuota, ff se c'è almeno un elemento
    public Boolean emptyListIsEmpty(){
        return itemRepository.getEmptyItemsList().isEmpty();
    }

    //crea il pdf con nome del file name e contenuto data
    public void createPdf(String name, String data){
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(350,650,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();

        Calendar calendar = Calendar.getInstance();
        String title = "LISTA SPESA: " + name + " (" + Utility.getDateStringFromCalendar(calendar) + ")";

        int x = 15, y=30;
        page.getCanvas().drawText(title, x, y, paint);
        y += paint.descent()*2 - paint.ascent();

        String body = data;

        for (String line : body.split("\n")){
            page.getCanvas().drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }

        pdfDocument.finishPage(page);
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/"+name+".pdf";
        File file = new File(filePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplication(), R.string.ERROR_DOWNLOAD_SHOPPING, Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();

        getCreated().setValue(true);
        getFile().setValue(name);
    }
}