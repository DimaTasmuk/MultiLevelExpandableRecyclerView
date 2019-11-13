package com.dtasmuk.multilevelexpandablerecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MyAdapter extends ExpandableRecyclerViewAdapter {

    private Context context;
    private List<Item> itemList;

    MyAdapter(Context context, List<Item> itemList) {
        super(itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        final Holder holder = (Holder) viewHolder;
        Item item = itemList.get(position);

        holder.title.setText(item.getText());
        if (item.hasChildren() && item.isExpanded()) {
            holder.icon.setVisibility(View.VISIBLE);
            RotateAnimation rotate = new RotateAnimation(180,180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setFillAfter(true);
            holder.icon.setAnimation(rotate);
        } else if (item.hasChildren() && !item.isExpanded()) {
            holder.icon.setVisibility(View.VISIBLE);
            RotateAnimation rotate = new RotateAnimation(0,0, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setFillAfter(true);
            holder.icon.setAnimation(rotate);
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        holder.checkBox.setVisibility(item.isSelectable() ? View.VISIBLE : View.GONE);
        holder.checkBox.setChecked(item.isSelected());

        holder.itemContainer.setBackgroundColor(item.isSelected() ? Color.parseColor("#BEBEBE") : Color.TRANSPARENT);

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            boolean is_expanded = itemList.get(viewHolder.getAdapterPosition()).isExpanded();
            RotateAnimation rotate = new RotateAnimation(is_expanded ? 180 : 0, is_expanded ? 0 : 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);

            ((Holder) viewHolder).icon.setAnimation(rotate);
            if (is_expanded) {
                collapse(itemList.get(viewHolder.getAdapterPosition()));
            } else {
                expand(itemList.get(viewHolder.getAdapterPosition()));
            }
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(itemList.get(viewHolder.getAdapterPosition()));
            }
        });

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(itemList.get(viewHolder.getAdapterPosition()));
            }
        });

        float density = context.getResources().getDisplayMetrics().density;
        ((ViewGroup.MarginLayoutParams) holder.itemContainer.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 30) * density + 0.5f);
    }

    private class Holder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView icon;
        ConstraintLayout itemContainer;
        CheckBox checkBox;

        Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            itemContainer = itemView.findViewById(R.id.item_container);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
