package com.example.trackingmypantrygiacomochinilam.ui.insert;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.trackingmypantrygiacomochinilam.Constants;
import com.example.trackingmypantrygiacomochinilam.Item;
import com.example.trackingmypantrygiacomochinilam.ItemRepository;
import com.example.trackingmypantrygiacomochinilam.Product;
import com.example.trackingmypantrygiacomochinilam.ProductRepository;
import com.example.trackingmypantrygiacomochinilam.R;
import com.example.trackingmypantrygiacomochinilam.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class InsertViewModel extends AndroidViewModel {

    public MutableLiveData<String> authenticationToken; //token di accesso
    public MutableLiveData<String> temporaryToken; //token breve

    public MutableLiveData<Product> productSelected; //prodotto selezionato
    public MutableLiveData<String> barcodeRequested; //barcode richiesto
    private LiveData<List<Product>> listOfProduct; //lista prodotti scaricati dal server

    public MutableLiveData<String> nameToPut; //variabili per la put
    public MutableLiveData<String> descToPut; //variabili per la put
    public MutableLiveData<String> imgToPut; //variabili per la put
    public MutableLiveData<Bitmap> bitmapPut; //variabili per la put
    public MutableLiveData<Boolean> imgChanged; //variabili per la put

    public MutableLiveData<Integer> ratingConfirm; //variabili per la confirm
    public MutableLiveData<Integer> quantityConfirm; //variabili per la confirm
    public MutableLiveData<String> typeConfirm; //variabili per la confirm
    public MutableLiveData<String> expireDateConfirm; //variabili per la confirm
    public MutableLiveData<String> insertDateConfirm; //variabili per la confirm

    private ProductRepository productRepository; //repository per i prodotti scaricati dal server
    private ItemRepository itemRepository; //repository per gli item nella dispensa locale

    public InsertViewModel(Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        String dbId = Utility.getIdPref(application);
        itemRepository = new ItemRepository(application, dbId);

        productRepository.removeAllProducts();
        listOfProduct = productRepository.getListOfProducts();
        authenticationToken = new MutableLiveData<String>(Utility.getTokenPref(application.getApplicationContext()));

        getBitmapPut().setValue( Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.myitem_logo), 150,150, true));
        getImgChanged().setValue(false);

        setDefaultConfirmData();
    }


    public MutableLiveData<String> getAuthToken(){
        if (authenticationToken == null) {
            authenticationToken = new MutableLiveData<String>();
        }
        return authenticationToken;
    }
    public MutableLiveData<String> getBarcodeRequested(){
        if (barcodeRequested == null) {
            barcodeRequested = new MutableLiveData<String>();
        }
        return barcodeRequested;
    }
    public MutableLiveData<Product> getProductSelected(){
        if (productSelected == null) {
            productSelected = new MutableLiveData<Product>();
        }
        return productSelected;
    }
    public void setTemporaryToken(String temp){
        getTemporaryToken().setValue(temp);
    }
    public MutableLiveData<String> getTemporaryToken(){
        if(temporaryToken == null){
            temporaryToken = new MutableLiveData<String>();
        }
        return temporaryToken;
    }
    public LiveData<List<Product>> getListOfProduct(){
        return listOfProduct;
    }
    public MutableLiveData<String> getNameToPut(){
        if (nameToPut == null) {
            nameToPut = new MutableLiveData<String>();
        }
        return nameToPut;
    }
    public MutableLiveData<String> getDescToPut(){
        if (descToPut == null) {
            descToPut = new MutableLiveData<String>();
        }
        return descToPut;
    }
    public MutableLiveData<String> getImgToPut(){
        if (imgToPut == null) {
            imgToPut = new MutableLiveData<String>();
        }
        return imgToPut;
    }
    public MutableLiveData<Bitmap> getBitmapPut(){
        if (bitmapPut == null) {
            bitmapPut = new MutableLiveData<Bitmap>();
        }
        return bitmapPut;
    }
    public MutableLiveData<Boolean> getImgChanged(){
        if (imgChanged == null) {
            imgChanged = new MutableLiveData<Boolean>();
        }
        return imgChanged;
    }

    //funzioni per insertitemfragment
    public boolean checkId(String id){
        return itemRepository.existId(id);
    }

    public void searchProduct(){
        productRepository.removeAllProducts(); // azzero la lista dei prodotti salvati

        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constants.PRODUCTS_BARCODE_URL + barcodeRequested.getValue(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                insertTemporaryToken(response);
                insertProductData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.ERROR_SEARCH), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer " + authenticationToken.getValue();
                Map<String, String> headers = new ArrayMap<String, String>();
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        Thread thread = new Thread() {
            @Override
            public void run() {
                requestQueue.add(jsonRequest);
            }
        };
        thread.start();
    }

    public void insertProductData(JSONObject res){
        try {
            JSONArray products = res.getJSONArray("products");
            if(products.length()==0){
                Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.EMPTY_SEARCH), Toast.LENGTH_SHORT).show();
            }else{
                for (int i = 0; i < products.length(); i++) {
                    try {
                        String itId = products.getJSONObject(i).getString("id");
                        String itName = products.getJSONObject(i).getString("name");
                        String itBarcode = products.getJSONObject(i).getString("barcode");
                        String itDesc = products.getJSONObject(i).getString("description");
                        byte[] img;
                        try{
                            String imageStr = products.getJSONObject(i).getString("img");
                            imageStr = imageStr.substring(imageStr.indexOf(",")+1);
                            InputStream stream = new ByteArrayInputStream(Base64.decode(imageStr.getBytes(), Base64.DEFAULT));
                            img = Utility.getBytesFromBitmap(BitmapFactory.decodeStream(stream));
                        } catch (Exception e) {
                            img=null;
                        }
                        Product p;
                        //se non c'è un'immagine allora metto quella di default
                        if(img==null){
                            p = new Product(itId, itName, itBarcode, itDesc, Utility.getBytesFromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.myitem_logo), 75, 75, true)));
                        }else{
                            p = new Product(itId, itName, itBarcode, itDesc, img);
                        }
                        //aggiungo al db
                        productRepository.insertProduct(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void insertTemporaryToken(JSONObject res){
        try {
            setTemporaryToken(res.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //fine funzioni


    //funzioni per putmyitemfragment
    public void putMyProduct() throws JSONException {

        JSONObject req = new JSONObject();
        req.put("token", getTemporaryToken().getValue());
        req.put("name", getNameToPut().getValue());
        req.put("description", getDescToPut().getValue());
        req.put("barcode", getBarcodeRequested().getValue());

        if(getImgChanged().getValue()){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            Bitmap resized = ThumbnailUtils.extractThumbnail(getBitmapPut().getValue(), 150, 150);//getBitmapPut().getValue().getWidth(), getBitmapPut().getValue().getHeight());
            resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image=stream.toByteArray();
            getImgToPut().setValue(Base64.encodeToString(image, 0));
            req.put("img", getImgToPut().getValue());
        }
        req.put("test", false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.PRODUCTS_URL, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                productRepository.removeAllProducts(); // azzero la lista dei prodotti salvati
                temporaryToken=null;
                nameToPut=null;
                descToPut=null;
                imgToPut=null;
                getBitmapPut().setValue( Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.myitem_logo), 150,150, true));
                getImgChanged().setValue(false);

                Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.OK_PUT), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.ERROR_PUT), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer "+authenticationToken.getValue();
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
    //fine funzioni

    //funzioni confirm
    public MutableLiveData<Integer> getRatingConfirm(){
        if (ratingConfirm == null) {
            ratingConfirm = new MutableLiveData<Integer>();
        }
        return ratingConfirm;
    }
    public MutableLiveData<Integer> getQuantityConfirm(){
        if (quantityConfirm == null) {
            quantityConfirm = new MutableLiveData<Integer>();
        }
        return quantityConfirm;
    }
    public MutableLiveData<String> getExpireDateConfirm(){
        if (expireDateConfirm == null) {
            expireDateConfirm = new MutableLiveData<String>();
        }
        return expireDateConfirm;
    }
    public MutableLiveData<String> getInsertDateConfirm(){
        if (insertDateConfirm == null) {
            insertDateConfirm = new MutableLiveData<String>();
        }
        return insertDateConfirm;
    }
    public MutableLiveData<String> getTypeConfirm(){
        if (typeConfirm == null) {
            typeConfirm = new MutableLiveData<String>();
        }
        return typeConfirm;
    }

    public void setDefaultConfirmData(){
        //imposta date, expire, type, quantity e rating perchè nel caso l'utente non le metta, utilizza i valori predefiniti (modificabili dalle impostazioni)
        Calendar calendar = Calendar.getInstance();
        getInsertDateConfirm().setValue(Utility.getDateStringFromCalendar(calendar));
        calendar.add(Calendar.DAY_OF_MONTH, Utility.getGGPref(getApplication()));
        calendar.add(Calendar.MONTH, Utility.getMMPref(getApplication()));
        calendar.add(Calendar.YEAR, Utility.getYYPref(getApplication()));
        getExpireDateConfirm().setValue(Utility.getDateStringFromCalendar(calendar));
        getTypeConfirm().setValue("Nessuno");
        getQuantityConfirm().setValue(Utility.getQuantityPref(getApplication())); //almeno una unità di prodotto bisogna inserire
        getRatingConfirm().setValue(Utility.getQualityPref(getApplication()));
    }

    public void sendChoice() throws JSONException {

        JSONObject req = new JSONObject();
        req.put("token", getTemporaryToken().getValue());
        req.put("rating", getRatingConfirm().getValue());
        req.put("productId", getProductSelected().getValue().getId());

        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.VOTES_URL, req, response -> {

            Item item = new Item(getProductSelected().getValue().getId(),
                    getProductSelected().getValue().getBarcode(),
                    getProductSelected().getValue().getName(),
                    getProductSelected().getValue().getDescription(),
                    getInsertDateConfirm().getValue(),
                    getProductSelected().getValue().getImage(),
                    getExpireDateConfirm().getValue(),
                    getTypeConfirm().getValue() ,
                    getQuantityConfirm().getValue());

            itemRepository.insertItem(item); //aggiungo al db

            temporaryToken = null;
            productSelected = null;
            barcodeRequested = null;
            productRepository.removeAllProducts();
            setDefaultConfirmData();
            Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.OK_CONFIRM), Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.ERROR_CONFIRM), Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer "+getAuthToken().getValue();
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
    //fine funzioni
}
