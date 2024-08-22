package com.example.trackingmypantrygiacomochinilam;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ItemAdapter extends ListAdapter<Item, ItemViewHolder> {

    public static OnItemClickListener listener;

    public ItemAdapter(@NonNull DiffUtil.ItemCallback<Item> diffCallback) {
        super(diffCallback);
    }

    public interface OnItemClickListener {
        void onItemClick(Integer pos);
        void onDeleteClick(Integer pos);
        void onRemoveClick(Integer pos);
        void onAddClick(Integer pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item current = getItem(position);
        holder.bind(current.getName(), current.getExpire(), current.getQuantity().toString(), current.getImg());
    }

    public static class ItemDiff extends DiffUtil.ItemCallback<Item> {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId().equals(newItem.getId());
        }


    }
}
