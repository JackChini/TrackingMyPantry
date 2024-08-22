package com.example.trackingmypantrygiacomochinilam.ui.insert;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.trackingmypantrygiacomochinilam.Constants;
import com.example.trackingmypantrygiacomochinilam.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PutMyItemFragment extends Fragment {

    private InsertViewModel insertViewModel;

    private Button confirm;
    private EditText name, desc;
    private ImageView img;
    private ImageButton openCamera, openGallery, cancelImage;
    private TextView barcode;

    public PutMyItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_put_my_item, container, false);

        img = view.findViewById(R.id.putImg);
        openCamera = view.findViewById(R.id.openCameraBtn);
        openGallery = view.findViewById(R.id.openGalleryBtn);
        cancelImage = view.findViewById(R.id.cancelImgBtn);
        confirm = view.findViewById(R.id.putItemBtn);
        barcode = view.findViewById(R.id.barcodePut);
        name = view.findViewById(R.id.putName);
        desc = view.findViewById(R.id.putDescription);

        //view model
        insertViewModel = new ViewModelProvider(getActivity()).get(InsertViewModel.class);

        insertViewModel.getBarcodeRequested().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String bar) {
                barcode.setText(bar);
            }
        });
        insertViewModel.getBitmapPut().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        });

        //chiedo permessi e apro la fotocamera
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                }else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }

            }
        });

        //per caricare una foto dalla galleria
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Seleziona foto"), Constants.RESULT_LOAD_IMAGE);
            }
        });

        //per cancellare la foto caricata e mostrare l'icona di default
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertViewModel.getBitmapPut().setValue( Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.myitem_logo), 150,150, true));
                insertViewModel.getImgChanged().setValue(false);
            }
        });

        //per mandare il prodotto al server e navigare alla finestra di ricerca
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    insertViewModel.getNameToPut().setValue(name.getText().toString());
                    insertViewModel.getDescToPut().setValue(desc.getText().toString());
                    insertViewModel.putMyProduct();

                    NavHostFragment.findNavController(PutMyItemFragment.this).popBackStack();
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(view, getResources().getText(R.string.ERROR_PUT), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    insertViewModel.getBitmapPut().setValue(bitmap);
                    insertViewModel.getImgChanged().setValue(true);
                }
                break;
            case Constants.RESULT_LOAD_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    if( null != data){
                        try {
                            insertViewModel.getBitmapPut().setValue(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri));
                            insertViewModel.getImgChanged().setValue(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}