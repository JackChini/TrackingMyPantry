package com.example.trackingmypantrygiacomochinilam.ui.insert;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.trackingmypantrygiacomochinilam.Constants;
import com.example.trackingmypantrygiacomochinilam.Item;
import com.example.trackingmypantrygiacomochinilam.Product;
import com.example.trackingmypantrygiacomochinilam.R;
import com.example.trackingmypantrygiacomochinilam.Utility;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.Calendar;
import java.util.List;

public class ConfirmItemFragment extends Fragment {

    private InsertViewModel insertViewModel;

    private ImageView img;
    private TextView barcode, name, desc;
    private Button quantity, date, quality, confirm, type;
    private DatePickerDialog datePickerDialog;

    public ConfirmItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_item, container, false);

        img = view.findViewById(R.id.confImg);
        barcode = view.findViewById(R.id.confBarcode);
        name = view.findViewById(R.id.confName);
        desc = view.findViewById(R.id.confDesc);
        quantity = view.findViewById(R.id.confQuantity);
        date = view.findViewById(R.id.confExpireDate);
        quality = view.findViewById(R.id.confRating);
        confirm = view.findViewById(R.id.sendchoice);
        type = view.findViewById(R.id.confType);

        //view model
        insertViewModel = new ViewModelProvider(getActivity()).get(InsertViewModel.class);

        insertViewModel.getProductSelected().observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                barcode.setText(product.getBarcode());
                name.setText(product.getName());
                desc.setText(product.getDescription());
                img.setImageBitmap(Utility.getBitmapFromBytes(product.getImage()));
            }
        });

        insertViewModel.getQuantityConfirm().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                quantity.setText("Quantità: x: "+ i);
            }
        });
        insertViewModel.getRatingConfirm().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                quality.setText("Voto: "+ i +"/5");
            }
        });
        insertViewModel.getTypeConfirm().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String str) {
                type.setText("Genere: "+ str);
            }
        });
        insertViewModel.getExpireDateConfirm().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String str) {
                date.setText("Scadenza: "+ str);
            }
        });

        //mando la scelta e torno alla schermata di inserimento
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    insertViewModel.sendChoice();
                    NavHostFragment.findNavController(ConfirmItemFragment.this).popBackStack();
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(view, getResources().getText(R.string.ERROR_CONFIRM), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    e.printStackTrace();
                }
            }
        });

        //imposto quantità tramite picker
        quantity.setOnClickListener(v -> {
            AlertDialog.Builder quantityDialog = new AlertDialog.Builder(getContext());

            NumberPicker quantityPicker =  new NumberPicker(getContext());
            quantityPicker.setMaxValue(10);
            quantityPicker.setMinValue(1);
            quantityPicker.setWrapSelectorWheel(false);
            quantityPicker.setValue(insertViewModel.getQuantityConfirm().getValue());

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.addView(quantityPicker, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.CENTER);

            quantityDialog.setTitle(R.string.quantityTitleDialog);
            quantityDialog.setView(linearLayout);

            quantityDialog.setPositiveButton(R.string.confirmDialog, (dialog, which) -> {
                quantity.setText("Quantità: x"+ quantityPicker.getValue());
                insertViewModel.getQuantityConfirm().setValue(quantityPicker.getValue());
            });
            quantityDialog.setNegativeButton(R.string.cancelDialog, (dialog, which) -> {

            });

            quantityDialog.show();
        });

        //imposto la scadenza
        initDatePicker(); //inizializzo le info e i listener x il datepicker
        date.setOnClickListener(v -> {
            datePickerDialog.show();
            datePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
            datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        });

        //imposto il rating (x server)
        quality.setOnClickListener(v -> {
            AlertDialog.Builder rateDialog = new AlertDialog.Builder(getContext());

            RatingBar rating =  new RatingBar(getContext());
            rating.setNumStars(5);
            rating.setRating(insertViewModel.getRatingConfirm().getValue());
            rating.setStepSize(1);
            rating.setIsIndicator(false);

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.addView(rating, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.CENTER);

            rateDialog.setTitle(R.string.qualityTitleDialog);
            rateDialog.setView(linearLayout);

            rateDialog.setPositiveButton(R.string.confirmDialog, (dialog, which) -> {
                quality.setText("Voto: "+ ((int)rating.getRating())+"/5");
                insertViewModel.getRatingConfirm().setValue((int)rating.getRating());
            });
            rateDialog.setNegativeButton(R.string.cancelDialog, (dialog, which) -> {

            });

            rateDialog.show();
        });

        //imposto la categoria del prodotto
        type.setOnClickListener(v -> {
            AlertDialog.Builder typeDialog = new AlertDialog.Builder(getContext());

            NumberPicker typePicker =  new NumberPicker(getContext());
            typePicker.setDisplayedValues(new String[]{"Nessuno", "Bevande", "Verdure", "Carne e pesce", "Latticini", "Frutta", "Pasta e riso"});
            typePicker.setMinValue(0);
            typePicker.setMaxValue(6);
            typePicker.setWrapSelectorWheel(false);

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.addView(typePicker, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.CENTER);

            typeDialog.setTitle(R.string.typeFilter);
            typeDialog.setView(linearLayout);

            typeDialog.setPositiveButton(R.string.confirmDialog, (dialog, which) -> {
                type.setText("Genere: "+ typePicker.getDisplayedValues()[typePicker.getValue()]);
                insertViewModel.getTypeConfirm().setValue(typePicker.getDisplayedValues()[typePicker.getValue()]);
            });
            typeDialog.setNegativeButton(R.string.cancelDialog, (dialog, which) -> {

            });

            typeDialog.show();
        });

        return view;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {

            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            String dateChoice = "Scadenza: "+Utility.getDateStringFromCalendar(c);
            date.setText(dateChoice);

            insertViewModel.getExpireDateConfirm().setValue(Utility.getDateStringFromCalendar(c));
        };
        DialogInterface.OnCancelListener cancelListener = dialog -> date.setText(R.string.confExpireDate);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        insertViewModel.getInsertDateConfirm().setValue(Utility.getDateStringFromCalendar(cal));

        datePickerDialog = new DatePickerDialog(getContext(),R.style.MySpinnerDatePickerStyle,dateSetListener, year, month, day);
        datePickerDialog.setTitle(R.string.dateTitleDialog);

    }
}