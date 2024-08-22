package com.example.trackingmypantrygiacomochinilam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView prodNameTextView;
    public TextView prodDescTextView;
    public ImageView prodImgView;

    ProductViewHolder(View itemView) {
        super(itemView);

        prodNameTextView = itemView.findViewById(R.id.nameItem);
        prodDescTextView = itemView.findViewById(R.id.itemDescription);
        prodImgView = itemView.findViewById(R.id.pantryItemImg);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ProductAdapter.listener.onItemClick(position);
                }
            }
        });
    }

    public void bind(String name, String desc, byte[] img){
        prodNameTextView.setText(name);
        prodDescTextView.setText(desc);
        prodImgView.setImageBitmap(Utility.getBitmapFromBytes(img));
    }

    static ProductViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

}