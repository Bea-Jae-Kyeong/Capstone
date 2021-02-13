package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter {

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        LinearLayout searchItemLayout;
        ImageView search_img;
        TextView search_tv;

        public ItemViewHolder(View itemView){
            super(itemView);
            searchItemLayout = (LinearLayout) itemView.findViewById(R.id.search_item_layout);
            search_img = (ImageView) itemView.findViewById(R.id.search_item_img);
            search_tv = (TextView) itemView.findViewById(R.id.search_item_kwd);
        }
    }

    Context context;
    List<KeywordModel> items;
    KeywordModel item;


    public SearchAdapter(Context context, List<KeywordModel> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        RecyclerView.ViewHolder holder = new ItemViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        item = items.get(holder.getAdapterPosition());

        itemViewHolder.search_tv.setText(item.getFull_name());

        int num = item.getKeyword_no();
        if(0 <= num && num <= 505){
            itemViewHolder.search_img.setImageResource(R.drawable.soccer_jersey);
        }
        else if(505 < num && num <= 525){
            if (num == 506){
                itemViewHolder.search_img.setImageResource(R.drawable.arsenal);
            }
            else if(num == 507){
                itemViewHolder.search_img.setImageResource(R.drawable.villa);
            }
            else if(num == 508){
                itemViewHolder.search_img.setImageResource(R.drawable.bournmouth);
            }
            else if(num == 509){
                itemViewHolder.search_img.setImageResource(R.drawable.brighton);
            }
            else if(num == 510){
                itemViewHolder.search_img.setImageResource(R.drawable.burnley);
            }
            else if(num == 511){
                itemViewHolder.search_img.setImageResource(R.drawable.chelsea);
            }
            else if(num == 512){
                itemViewHolder.search_img.setImageResource(R.drawable.crystal);
            }
            else if(num == 513){
                itemViewHolder.search_img.setImageResource(R.drawable.everton);
            }
            else if(num == 514){
                itemViewHolder.search_img.setImageResource(R.drawable.leicester);
            }
            else if(num == 515){
                itemViewHolder.search_img.setImageResource(R.drawable.liverpool);
            }
            else if(num == 516){
                itemViewHolder.search_img.setImageResource(R.drawable.mancity);
            }
            else if(num == 517){
                itemViewHolder.search_img.setImageResource(R.drawable.manu);
            }
            else if(num == 518){
                itemViewHolder.search_img.setImageResource(R.drawable.newcastle);
            }
            else if(num == 519){
                itemViewHolder.search_img.setImageResource(R.drawable.norwich);
            }
            else if(num == 520){
                itemViewHolder.search_img.setImageResource(R.drawable.sheffield);
            }
            else if(num == 521){
                itemViewHolder.search_img.setImageResource(R.drawable.southampton);
            }
            else if(num == 522){
                itemViewHolder.search_img.setImageResource(R.drawable.tottenham);
            }
            else if(num == 523){
                itemViewHolder.search_img.setImageResource(R.drawable.watford);
            }
            else if(num == 524){
                itemViewHolder.search_img.setImageResource(R.drawable.westham);
            }
            else if(num == 525){
                itemViewHolder.search_img.setImageResource(R.drawable.wolves);
            }
        }
        else if(525 < num && num <= 545){
            itemViewHolder.search_img.setImageResource(R.drawable.ic_manager);
        }


        itemViewHolder.searchItemLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResultsListActivity.class);

                String kwd = items.get(position).getFull_name();
                if(kwd != null){
                    intent.putExtra("keyword",kwd);

                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void setItemList(List<KeywordModel> searchitems) {
        this.items = searchitems;
        notifyDataSetChanged();
    }
}
