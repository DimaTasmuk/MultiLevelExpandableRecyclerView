package com.dtasmuk.multilevelexpandablerecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MyAdapter extends ExpandableRecyclerViewAdapter {

    private int ARROW_PAYLOAD = 0;

    private Context context;
    private List<Item> itemList;
    private List<Item> rootItems;

    MyAdapter(Context context, List<Item> itemList) {
        super(itemList);
        this.context = context;
        this.itemList = itemList;
    }

    MyAdapter(Context context) {
        super(new ArrayList<>());
        this.context = context;
        this.itemList = new ArrayList<>();
        this.rootItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.contains(ARROW_PAYLOAD)) {
                rotateArrow((Holder) viewHolder, itemList.get(position).isExpanded());
            }
        } else {
            final Holder holder = (Holder) viewHolder;
            Item item = itemList.get(position);

            holder.title.setText(item.getText());

            if (item.hasChildren()) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(item.isExpanded() ? R.drawable.ic_arrow_collapse : R.drawable.ic_arrow_expand);
            } else {
                holder.icon.setVisibility(View.INVISIBLE);
            }

            holder.checkBox.setVisibility(item.isSelectable() ? View.VISIBLE : View.GONE);
            holder.checkBox.setChecked(item.isSelected());

            holder.itemContainer.setBackgroundColor(item.isSelected() ? Color.parseColor("#BEBEBE") : Color.TRANSPARENT);

            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean is_expanded = itemList.get(viewHolder.getAdapterPosition()).isExpanded();
                    if (is_expanded) {
                        collapse(itemList.get(viewHolder.getAdapterPosition()));
                    } else {
                        expand(itemList.get(viewHolder.getAdapterPosition()));
                    }
                    notifyItemChanged(viewHolder.getAdapterPosition(), ARROW_PAYLOAD);
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
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @SuppressWarnings("unchecked")
    public void setItems(List<Item> itemList) {
        this.itemList = itemList;
        setRecyclerViewItemList(itemList);
        rootItems = (List<Item>) getRootItems();
        notifyDataSetChanged();
    }

    private void rotateArrow(final Holder holder, final boolean is_expanded) {
        final RotateAnimation rotate = new RotateAnimation(0, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rotate.cancel();
                holder.icon.setImageResource(is_expanded ? R.drawable.ic_arrow_collapse : R.drawable.ic_arrow_expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.icon.setAnimation(rotate);
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
