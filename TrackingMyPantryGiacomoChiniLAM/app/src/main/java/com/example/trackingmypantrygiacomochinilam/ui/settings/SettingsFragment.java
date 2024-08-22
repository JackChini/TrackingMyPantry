package com.example.trackingmypantrygiacomochinilam.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.example.trackingmypantrygiacomochinilam.R;
import com.example.trackingmypantrygiacomochinilam.Utility;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Button quantity, quality, gg, mm, yy, exit, dbToEmpty, access;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        quantity = root.findViewById(R.id.defQuantityBtn);
        quality = root.findViewById(R.id.defQualityBtn);
        gg = root.findViewById(R.id.defExpireDayBtn);
        mm = root.findViewById(R.id.defExpireMonthBtn);
        yy = root.findViewById(R.id.defExpireYearBtn);
        exit = root.findViewById(R.id.defExitBtn);
        dbToEmpty = root.findViewById(R.id.defDbBtn);
        access = root.findViewById(R.id.defAccessBtn);

        //view model
        settingsViewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpinner(getResources().getString(R.string.quantDialog), Utility.getQuantityPref(getContext()), 1, 10, "Q");
            }
        });

        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpinner(getResources().getString(R.string.qualDialog), Utility.getQualityPref(getContext()), 1, 5, "R");
            }
        });

        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpinner(getResources().getString(R.string.ggDialog), Utility.getGGPref(getContext()), 0, 20, "GG");
            }
        });

        mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpinner(getResources().getString(R.string.mmDialog), Utility.getMMPref(getContext()), 0, 10, "MM");
            }
        });

        yy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpinner(getResources().getString(R.string.yyDialog), Utility.getYYPref(getContext()), 0, 5, "YY");
            }
        });

        dbToEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsViewModel.emptyDb();
                Snackbar snackbar = Snackbar.make(root, getResources().getText(R.string.OK_EMPTY_PANTRY), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsViewModel.disableAccess();
                Snackbar snackbar = Snackbar.make(root, getResources().getText(R.string.OK_REMOVE_ACCESS), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });

        return root;
    }

    public void createSpinner(String dialogTitle, Integer currentValue, Integer minValue, Integer maxValue, String resultType){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        NumberPicker picker =  new NumberPicker(getContext());
        picker.setMaxValue(maxValue);
        picker.setMinValue(minValue);
        picker.setValue(currentValue);
        picker.setWrapSelectorWheel(false);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.addView(picker, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER);

        alertDialog.setTitle(dialogTitle);
        alertDialog.setView(linearLayout);

        alertDialog.setPositiveButton(R.string.confirmDialog, (dialog, which) -> {
            settingsViewModel.setResultFromDialog(resultType, picker.getValue());
        });
        alertDialog.setNegativeButton(R.string.cancelDialog, (dialog, which) -> {
            //non devo fare nulla serve solo a chiudere il dialog
        });

        alertDialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}