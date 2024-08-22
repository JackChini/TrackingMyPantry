package com.example.trackingmypantrygiacomochinilam.ui.pantry;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.trackingmypantrygiacomochinilam.Item;
import com.example.trackingmypantrygiacomochinilam.ItemAdapter;
import com.example.trackingmypantrygiacomochinilam.R;
import com.example.trackingmypantrygiacomochinilam.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PantryFragment extends Fragment {

    private RecyclerView pantryRecyclerView;
    private TextView filterApplied;
    private ImageButton setFilter;

    private PantryViewModel pantryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantry_fragment, container, false);

        filterApplied = view.findViewById(R.id.pantryFilterApplied);
        setFilter = view.findViewById(R.id.pantryFilterBtn);
        pantryRecyclerView = view.findViewById(R.id.pantryRecyclerView);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        FloatingActionButton btnNav = view.findViewById(R.id.fastInsertBtn);
        btnNav.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_pantry_to_nav_insert);
        });

        //view model
        pantryViewModel = new ViewModelProvider(getActivity()).get(PantryViewModel.class);

        //aggiorno la view con i filtri applicati con observe
        pantryViewModel.getFilterText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String filter) {
                filterApplied.setText(filter);
            }
        });;

        setFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterApplied.setText("Nessun filtro applicato!");
                pantryViewModel.getBaseFilter().setValue(true);

                AlertDialog.Builder filterDialog = new AlertDialog.Builder(getContext());

                filterDialog.setTitle(R.string.filterDialog);

                View filterView = inflater.inflate(R.layout.filter_dialog, container, false);
                RadioGroup radioGroup = filterView.findViewById(R.id.quantityRdGrp);

                filterDialog.setView(filterView);

                filterDialog.setPositiveButton(R.string.confirmDialog, (dialog, which) -> {

                    String queryString = "SELECT * FROM items"; //in base ai filtri questa stringa diventa la query per il db
                    List<Object> args = new ArrayList(); //in base ai filtri vengono aggiunti alla lista dei parametri che servono per creare la query con simpleSqliteQuery
                    String filterTextBox = "";

                    //questo if serve perchè avendo due xml diversi per il filtro mi serve sapere come prendere i dati (in base a come è girato il cell)
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        // In landscape
                        LinearLayout container1 = filterView.findViewById(R.id.checkContainer1);
                        LinearLayout container2 = filterView.findViewById(R.id.checkContainer2);

                        for (int i = 1; container1.getChildCount() > i; ++i) { //parto da 1 perche il primo elemento è una textview
                            CheckBox c = (CheckBox) container1.getChildAt(i);
                            if(c.isChecked()){
                                if(queryString.equals("SELECT * FROM items")){
                                    queryString = queryString + " WHERE type = ?";
                                    args.add(c.getText().toString());
                                }else{
                                    queryString = queryString + " OR type = ?";
                                    args.add(c.getText().toString());
                                }
                                filterTextBox += c.getText().toString() + " - ";
                            }
                        }
                        for (int i = 0; container2.getChildCount() > i; ++i) {
                            CheckBox c = (CheckBox) container2.getChildAt(i);
                            if(c.isChecked()){
                                if(queryString.equals("SELECT * FROM items")){
                                    queryString = queryString + " WHERE type = ?";
                                    args.add(c.getText().toString());
                                }else{
                                    queryString = queryString + " OR type = ?";
                                    args.add(c.getText().toString());
                                }
                                filterTextBox += c.getText().toString() + " - ";
                            }
                        }
                        int i = radioGroup.getCheckedRadioButtonId();
                        if(i != -1){
                            RadioButton rb = filterView.findViewById(i);
                            switch (rb.getText().toString()){
                                case "Crescente":
                                    queryString = queryString + " ORDER BY quantity ASC";
                                    break;
                                case "Decrescente":
                                    queryString = queryString + " ORDER BY quantity DESC";
                                    break;
                                default:
                                    break;
                            }
                        }

                    } else {
                        // In portrait
                        LinearLayout container = filterView.findViewById(R.id.checkBoxContainer);

                        for (int i = 0; container.getChildCount() > i; ++i) {
                            CheckBox c = (CheckBox) container.getChildAt(i);
                            if(c.isChecked()){
                                if(queryString.equals("SELECT * FROM items")){
                                    queryString = queryString + " WHERE type = ?";
                                    args.add(c.getText().toString());
                                }else{
                                    queryString = queryString + " OR type = ?";
                                    args.add(c.getText().toString());
                                }
                                filterTextBox += c.getText().toString() + " - ";
                            }
                        }
                        int i = radioGroup.getCheckedRadioButtonId();
                        if(i != -1){
                            RadioButton rb = filterView.findViewById(i);
                            switch (rb.getText().toString()){
                                case "Crescente":
                                    queryString = queryString + " ORDER BY quantity ASC";
                                    break;
                                case "Decrescente":
                                    queryString = queryString + " ORDER BY quantity DESC";
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    queryString += ";";

                    pantryViewModel.getFilterQuery().setValue(queryString);
                    pantryViewModel.getFilterParams().setValue(args);
                    pantryViewModel.getBaseFilter().setValue(false);

                    if(filterTextBox.equals("")){
                        pantryViewModel.getFilterText().setValue("Nessun filtro applicato!");
                        //filterApplied.setText("Nessun filtro applicato!");
                    }else{
                        pantryViewModel.getFilterText().setValue("Filtrato per: - "+filterTextBox);
                        //filterApplied.setText("Filtrato per: - "+filterTextBox);
                    }

                });

                filterDialog.setNegativeButton(R.string.cancelDialog, (dialog, which) -> {
                    filterApplied.setText("Nessun filtro applicato!");
                    pantryViewModel.getBaseFilter().setValue(true);
                });

                filterDialog.show();
            }
        });

        final ItemAdapter adapter = new ItemAdapter(new ItemAdapter.ItemDiff());
        pantryRecyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape distribuisco in 4 colonne i prodotti
            pantryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        } else {
            // In portrait distribuisco su 2 colonne i prodotti
            pantryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        pantryViewModel.getListOfItem().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.submitList(items);
            }
        });

        //se clicco sull'item posso aggiornare la scadenza
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer pos) {
                pantryViewModel.getItemIdSelected().setValue(pantryViewModel.getListOfItem().getValue().get(pos).getId());

                DatePickerDialog datePickerDialog;

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getContext(), R.style.MySpinnerDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);

                        pantryViewModel.changeExpireDate(Utility.getDateStringFromCalendar(c));
                    }
                }, year, month, day);

                datePickerDialog.setTitle(R.string.updateDateDialog);

                datePickerDialog.show();
                datePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
            }

            //elimino il prodotto dalla dispensa
            @Override
            public void onDeleteClick(Integer pos) {
                pantryViewModel.deleteItem(pantryViewModel.getListOfItem().getValue().get(pos).getId());

                Snackbar snackbar = Snackbar.make(view, getResources().getText(R.string.OK_REMOVE), Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            //tolto una unità di quel prodotto dal db (controllo che non sia <0)
            @Override
            public void onRemoveClick(Integer pos) {
                if(pantryViewModel.getListOfItem().getValue().get(pos).getQuantity() >= 1){
                    pantryViewModel.removeQuantityItem(pantryViewModel.getListOfItem().getValue().get(pos).getId());
                }
            }

            //aggiungo una unità di prodotto nel db
            @Override
            public void onAddClick(Integer pos) {
                pantryViewModel.addQuantityItem(pantryViewModel.getListOfItem().getValue().get(pos).getId());
            }
        });

        return view;
    }

}