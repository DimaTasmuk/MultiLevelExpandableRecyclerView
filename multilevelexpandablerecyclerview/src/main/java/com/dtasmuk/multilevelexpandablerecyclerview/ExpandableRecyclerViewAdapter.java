package com.dtasmuk.multilevelexpandablerecyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String EXPAND_STATE_MAP = "expandable_recycler_view_adapter_expand_state_map";

    private ArrayList<RecyclerViewItem> recyclerViewItemList;

    @SuppressWarnings("unchecked")
    protected ExpandableRecyclerViewAdapter(List<?> recyclerViewItems) {
        this.recyclerViewItemList = (ArrayList<RecyclerViewItem>) recyclerViewItems;
    }

    @SuppressWarnings("unchecked")
    protected void setItemList(List<? extends RecyclerViewItem> itemList) {
        this.recyclerViewItemList = (ArrayList<RecyclerViewItem>) itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return recyclerViewItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return recyclerViewItemList.get(position).getLevel();
    }

    public void expand(RecyclerViewItem item) {
        if (item.getParent() != null && !item.getParent().isExpanded())
            expand(item.getParent());

        int itemPosition = recyclerViewItemList.indexOf(item);
        if (!item.isExpanded() && item.hasChildren()) {
            item.setExpanded(true);
            recyclerViewItemList.addAll(itemPosition + 1, item.getChildren());
            notifyItemRangeInserted(itemPosition + 1, item.getChildren().size());
        }
    }

    public void expandAll() {
        List<RecyclerViewItem> tempList = new ArrayList<>(recyclerViewItemList);
        for (RecyclerViewItem item : tempList) {
            expand(item);
        }
    }

    public void collapse(RecyclerViewItem item) {
        if (item.hasChildren()) {
            item.setExpanded(false);
            removeAllChildren(item);
        }
    }

    public void collapseAll() {
        List<RecyclerViewItem> tempList = new ArrayList<>(recyclerViewItemList);
        for (RecyclerViewItem item : tempList) {
            if (item.getParent() == null && item.isExpanded()) {
                collapse(item);
                notifyItemChanged(recyclerViewItemList.indexOf(item));
            }
        }
    }

    public void toggleSelection(RecyclerViewItem item) {
        if (item.isSelectable()) {
            item.toggle();
            if (item.hasChildren()) {
                for (RecyclerViewItem child : item.getChildren()) {
                    child.setSelected(item.isSelected());
                    child.setSelectedAllChildren(item.isSelected());
                }
            }
            item.setSelectedParent();

            notifyDataSetChanged();
        }
    }

    public List<? extends RecyclerViewItem> getSelectedItems(RecyclerViewItem item) {
        List<? extends RecyclerViewItem> children = item != null ? item.getChildren() : getRootItems();
        List<RecyclerViewItem> selectedItems = new ArrayList<>();

        for (RecyclerViewItem child : children) {
            if (child.isSelected())
                selectedItems.add(child);

            if (child.hasChildren()) {
                selectedItems.addAll(getSelectedItems(child));
            }
        }

        return selectedItems;
    }

    public List<? extends RecyclerViewItem> getRootItems() {
        List<RecyclerViewItem> roots = new ArrayList<>();

        for (RecyclerViewItem item : recyclerViewItemList)
            if (item.getParent() == null)
                roots.add(item);

        return roots;
    }

    private void removeItem(int itemPosition) {
        recyclerViewItemList.remove(itemPosition);
        notifyItemRemoved(itemPosition);
    }

    private void removeAllChildren(RecyclerViewItem item) {
        for (int i = 0; i < item.getChildren().size(); i++) {
            RecyclerViewItem child = item.getChildren().get(i);
            if (child.isExpanded()) {
                child.setExpanded(false);
                removeAllChildren(child);
            }
            removeItem(recyclerViewItemList.indexOf(child));
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(EXPAND_STATE_MAP, recyclerViewItemList);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPAND_STATE_MAP)) {
            return;
        }

        ArrayList<RecyclerViewItem> savedList = savedInstanceState.getParcelableArrayList(EXPAND_STATE_MAP);
        if (savedList != null) {
            recyclerViewItemList.clear();
            recyclerViewItemList.addAll(savedList);
            notifyDataSetChanged();
        }
    }

}
