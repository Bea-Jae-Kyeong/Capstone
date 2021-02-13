package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleAdapterNoHeader extends RecyclerView.Adapter {

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        LinearLayout articleItemLayout;
        TextView articleTitle;
        TextView articleSource;
        ImageView articleContent;
        TextView articleDate;
        CardView cardView;

        public ItemViewHolder(View itemView){
            super(itemView);
//            articleItemLayout = (LinearLayout) itemView.findViewById(R.id.article_item_layout);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            articleSource = (TextView) itemView.findViewById(R.id.article_source);
            articleContent = (ImageView) itemView.findViewById(R.id.article_content);
            articleDate = (TextView) itemView.findViewById(R.id.article_date);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
        }
    }

    Context context;
    List<ArticleModel> items;
    int item_layout;
    ArticleModel item;


    public ArticleAdapterNoHeader(Context context, List<ArticleModel> items,int item_layout){
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        RecyclerView.ViewHolder holder = new ArticleAdapterNoHeader.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ArticleAdapterNoHeader.ItemViewHolder itemViewHolder = (ArticleAdapterNoHeader.ItemViewHolder) holder;
        item = items.get(position);
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

                int id = items.get(position).getId();
                if(id != 0){
                    intent.putExtra("news_id",items.get(position).getId());

                }
                context.startActivity(intent);
            }
        });

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
