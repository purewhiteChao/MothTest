package com.example.six_mothtest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.six_mothtest.R;
import com.example.six_mothtest.model.bean.GuShiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/4/30 0030
 * Time: 14:05
 * Describe: ${as}
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    private List<GuShiBean.ResultBean> list = new ArrayList<>();
    private Context context;

    public void refresh(List<GuShiBean.ResultBean> list){
        this.list.clear();;
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.title.setText(list.get(i).getTitle());
        myViewHolder.auther.setText(list.get(i).getAuthors());
        myViewHolder.content.setText(list.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView auther;
        public TextView content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_item);
            auther = itemView.findViewById(R.id.auther_item);
            content = itemView.findViewById(R.id.content_item);
        }
    }
}
