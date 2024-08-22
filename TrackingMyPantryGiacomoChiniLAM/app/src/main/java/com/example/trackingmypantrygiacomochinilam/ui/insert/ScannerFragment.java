package com.example.trackingmypantrygiacomochinilam.ui.insert;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private InsertViewModel sharedModel;
    private ZXingScannerView scannerView;

    public ScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedModel = new ViewModelProvider(getActivity()).get(InsertViewModel.class);
        scannerView = new ZXingScannerView(getContext());
        return scannerView;
    }

    //chiedo permessi per la fotocamera che sono necessari per usare la librearia dello scanner e imposto il gestore del risultato ottenuto
    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);

            NavHostFragment.findNavController(this).popBackStack();
        }else{
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        sharedModel.getBarcodeRequested().setValue(result.getText());
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}