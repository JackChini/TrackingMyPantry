package com.example.trackingmypantrygiacomochinilam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView expireTextView;
    public TextView nameTextView;
    public TextView quantityTextView;
    public ImageView imgView;
    public ImageButton delete, remove, add;

    public ItemViewHolder(View itemView) {
        super(itemView);

        expireTextView = itemView.findViewById(R.id.expireItem);
        nameTextView = itemView.findViewById(R.id.nameItem);
        quantityTextView = itemView.findViewById(R.id.quantityItem);
        imgView = itemView.findViewById(R.id.pantryItemImg);
        delete = itemView.findViewById(R.id.deleteBtn);
        remove = itemView.findViewById(R.id.removeBtn);
        add = itemView.findViewById(R.id.addBtn);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ItemAdapter.listener.onItemClick(position);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ItemAdapter.listener.onDeleteClick(position);
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ItemAdapter.listener.onRemoveClick(position);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ItemAdapter.listener.onAddClick(position);
                }
            }
        });
    }

    public void bind(String name, String expire, String quant, byte[] img){
        nameTextView.setText(name);
        expireTextView.setText(expire);
        quantityTextView.setText(quant);
        imgView.setImageBitmap(Utility.getBitmapFromBytes(img));
    }

    static ItemViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pantry_item, parent, false);
        return new ItemViewHolder(view);
    }

}
