package com.example.trackingmypantrygiacomochinilam;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ProductAdapter extends ListAdapter<Product, ProductViewHolder>  {

    public static OnItemClickListener listener;

    public ProductAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback) {
        super(diffCallback);
    }

    public interface OnItemClickListener {
        void onItemClick(Integer pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product current = getItem(position);
        holder.bind(current.getName(), current.getDescription(), current.getImage());
    }

    public static class ProductDiff extends DiffUtil.ItemCallback<Product> {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId().equals(newItem.getId());
        }


    }

}