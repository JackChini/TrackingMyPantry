package com.example.trackingmypantrygiacomochinilam.ui.insert;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.trackingmypantrygiacomochinilam.Product;
import com.example.trackingmypantrygiacomochinilam.ProductAdapter;
import com.example.trackingmypantrygiacomochinilam.R;

import java.util.List;

public class InsertItemFragment extends Fragment {

    private InsertViewModel insertViewModel;

    private EditText barcode;
    private ImageButton search, scanner;
    private Button moveToPut;
    private RecyclerView productRecyclerView;


    public InsertItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_insert_item, container, false);

        barcode = root.findViewById(R.id.barcodeNum);
        search = root.findViewById(R.id.searchBtn);
        scanner = root.findViewById(R.id.scannerBtn);
        productRecyclerView = root.findViewById(R.id.itemListRecView);
        moveToPut = root.findViewById(R.id.moveToPutBtn);

        //view model
        insertViewModel = new ViewModelProvider(getActivity()).get(InsertViewModel.class);

        final ProductAdapter adapter = new ProductAdapter(new ProductAdapter.ProductDiff());
        productRecyclerView.setAdapter(adapter);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        insertViewModel.getBarcodeRequested().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String bar) {
                barcode.setText(bar);
            }
        });

        insertViewModel.getListOfProduct().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.submitList(products);
            }
        });

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer pos) {
                insertViewModel.getProductSelected().setValue(insertViewModel.getListOfProduct().getValue().get(pos));

                if(insertViewModel.getProductSelected().getValue() != null){
                    if(! insertViewModel.checkId(insertViewModel.getProductSelected().getValue().getId())){
                        insertViewModel.setDefaultConfirmData();
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.action_nav_insert_to_nav_confirm);
                    }else{
                        Toast.makeText(getContext(), R.string.ERROR_ID_INSERT, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), R.string.ERROR_SELECT_INSERT, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //tasto cerca (lente d'ingrandimento)
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertViewModel.getBarcodeRequested().setValue(barcode.getText().toString());
                insertViewModel.searchProduct();
            }
        });

        //tasto scanner
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_insert_to_nav_scanner);
            }
        });

        //tasto per andare a crearsi il proprio product (devo aper prima fatto la search per avere il tmp token)
        moveToPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(insertViewModel.getTemporaryToken().getValue() != null){
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_insert_to_nav_putItem);
                }
            }
        });

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();

    }
}