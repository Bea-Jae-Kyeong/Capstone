package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleAdapterTotal extends RecyclerView.Adapter {
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View headerView) {
            super(headerView);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        LinearLayout articleItemLayout;
        TextView articleTitle;
        TextView articleSource;
        ImageView articleContent;
        TextView articleDate;
        //        TextView article_keyword1;
//        TextView article_keyword2;
//        TextView article_keyword3;
        CardView cardView;

        public ItemViewHolder(View itemView){
            super(itemView);
//            articleItemLayout = (LinearLayout) itemView.findViewById(R.id.article_item_layout);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            articleSource = (TextView) itemView.findViewById(R.id.article_source);
            articleContent = (ImageView) itemView.findViewById(R.id.article_content);
            articleDate = (TextView) itemView.findViewById(R.id.article_date);
//            article_keyword1 = (TextView) itemView.findViewById(R.id.article_keyword1);
//            article_keyword2 = (TextView) itemView.findViewById(R.id.article_keyword2);
//            article_keyword3 = (TextView) itemView.findViewById(R.id.article_keyword3);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
        }
    }

    Context context;
    List<ArticleModel> items;
    ArticleModel item;
    int item_layout;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public ArticleAdapterTotal(Context context, List<ArticleModel> items,int item_layout){
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
            holder = new ItemViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        } else {
            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            item = items.get(position-1);

            itemViewHolder.articleTitle.setText(item.getTitle());
            itemViewHolder.articleSource.setText(item.getSource());

            String temp_date[] = item.getDate().split("T");
            String temp_time[] = temp_date[1].split(":");
            String format_date = temp_date[0] + " " + temp_time[0] + ":" + temp_time[1];
            itemViewHolder.articleDate.setText(format_date);

            Glide.with(context).load(item.getImgSrc()).into(itemViewHolder.articleContent);

            itemViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleDetail.class);

                    int id = items.get(position-1).getId();
                    if(id != 0){
                        intent.putExtra("news_id",id);
                    }


                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void setItemList(List<ArticleModel> articleitems) {
        this.items = articleitems;
        notifyDataSetChanged();
    }
}
