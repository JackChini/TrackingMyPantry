package com.example.trackingmypantrygiacomochinilam.ui.shopping;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingmypantrygiacomochinilam.Item;
import com.example.trackingmypantrygiacomochinilam.ProductAdapter;
import com.example.trackingmypantrygiacomochinilam.R;
import com.example.trackingmypantrygiacomochinilam.Utility;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private ShoppingViewModel shoppingViewModel;

    private ImageButton downloadCart, shareCart;
    private Button createFromEmpty;
    private TextView counter;
    private EditText fileName;
    private RecyclerView shoppingRecycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.shopping_fragment, container, false);

        downloadCart = root.findViewById(R.id.downloadBtn);
        shareCart = root.findViewById(R.id.shareBtn);
        counter = root.findViewById(R.id.counter);
        fileName = root.findViewById(R.id.fileName);
        createFromEmpty = root.findViewById(R.id.emptyCartBtn);
        shoppingRecycler = root.findViewById(R.id.shoppingRecyclerView);

        //view model
        shoppingViewModel = new ViewModelProvider(getActivity()).get(ShoppingViewModel.class);
        shoppingViewModel.updateData();

        final ProductAdapter adapter = new ProductAdapter(new ProductAdapter.ProductDiff());
        shoppingRecycler.setAdapter(adapter);
        shoppingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        shoppingViewModel.getListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                shoppingViewModel.getShoppingList().setValue(shoppingViewModel.convertItemToProduct(items));
                adapter.submitList(shoppingViewModel.getShoppingList().getValue());
            }
        });

        //aggiungo/rimuovo gli elementi dalla lista provvisoria
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer pos) {
                if(shoppingViewModel.checkChoice(pos, shoppingViewModel.getPositionList().getValue())){
                    shoppingViewModel.getPositionList().getValue().remove(pos);
                    shoppingViewModel.getCount().setValue(shoppingViewModel.getCount().getValue()-1);
                    Snackbar snackbar = Snackbar.make(root, getResources().getText(R.string.removeShopping), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    shoppingViewModel.getPositionList().getValue().add(pos);
                    shoppingViewModel.getCount().setValue(shoppingViewModel.getCount().getValue()+1);
                    Snackbar snackbar = Snackbar.make(root, getResources().getText(R.string.addShopping), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                counter.setText(shoppingViewModel.getCount().getValue().toString()+"/40");
            }
        });

        //funzione per creare la lista della spesa con gli elementi esauriti, prima chiede i permessi per lo storage poi chiama la funzione
        createFromEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                }else{
                    if(fileName.getText().toString().equals("")){
                        Toast.makeText(getContext(), R.string.ERROR_FILENAME_SHOPPING, Toast.LENGTH_SHORT).show();
                    }else{
                        if(shoppingViewModel.emptyListIsEmpty()){
                            Snackbar snackbar = Snackbar.make(root, R.string.emptyShopping, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{
                            shoppingViewModel.getFile().setValue(fileName.getText().toString());
                            shoppingViewModel.createPdf( shoppingViewModel.getFile().getValue(), shoppingViewModel.generateShoppingListFromEmpty());

                            Snackbar snackbar = Snackbar.make(root, R.string.OK_DOWNLOAD_SHOPPING, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                }
            }
        });

        //scarica in formato pdf la lista creata, prima fa un check su permessi, filename e num elementi
        downloadCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                }else{
                    if(fileName.getText().toString().equals("")){
                        Toast.makeText(getContext(), R.string.ERROR_FILENAME_SHOPPING, Toast.LENGTH_SHORT).show();
                    }else{
                        if(shoppingViewModel.getCount().getValue() >= 1 && shoppingViewModel.getCount().getValue() <= 40) {

                            shoppingViewModel.getFile().setValue(fileName.getText().toString());
                            shoppingViewModel.createPdf(shoppingViewModel.getFile().getValue(), shoppingViewModel.generateShoppingList());

                            Snackbar snackbar = Snackbar.make(root, R.string.OK_DOWNLOAD_SHOPPING, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{
                            Toast.makeText(getContext(), R.string.ERROR_SHOPPING_QUANTITY, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //tasto per condividere la lista appena creata, l'intent  di tipo actionSend permette di inoltare il pdf tramite varie app (whatsapp, drive, email...)
        shareCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shoppingViewModel.getCreated().getValue()){
                    Uri uri = FileProvider.getUriForFile(getContext(),
                            getContext().getApplicationContext().getPackageName() + ".provider",
                            new File(Environment.getExternalStorageDirectory().getPath() + "/"+shoppingViewModel.getFile().getValue()+".pdf"));
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(share);
                }else{
                    Toast.makeText(getContext(), R.string.ERROR_SHARE_SHOPPING, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }




}