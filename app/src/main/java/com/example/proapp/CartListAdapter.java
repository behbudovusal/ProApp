package com.example.proapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private Context context;
    private List<Item> cartlist;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public CartListAdapter(Context context, List<Item> cartList) {
        this.context = context;
        this.cartlist = cartList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Item item = cartlist.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice() + "m");
        Glide.with(context)
                .load(item.getThumbnail())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position) {
        cartlist.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item,int position) {

        cartlist.add(position,item);
        notifyItemInserted(position);

    }
}
