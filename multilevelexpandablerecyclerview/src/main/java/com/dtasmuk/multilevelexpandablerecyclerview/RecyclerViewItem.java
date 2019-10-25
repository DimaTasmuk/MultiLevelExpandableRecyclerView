package com.dtasmuk.multilevelexpandablerecyclerview;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewItem implements Parcelable {

    private String text;
    private int level = 0;
    private RecyclerViewItem parent = null;
    private List<RecyclerViewItem> children;

    private boolean expanded = false;
    private boolean selected = false;
    private boolean selectable = true;

    public RecyclerViewItem() {
        this.children = new ArrayList<>();
    }

    public RecyclerViewItem(List<? extends RecyclerViewItem> children) {
        this();
        setChildren(children);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        for (RecyclerViewItem child : children) {
            child.setLevel(level + 1);
        }
    }

    public RecyclerViewItem getParent() {
        return parent;
    }

    public boolean hasChildren() {
        return children != null && children.size() > 0;
    }

    public List<RecyclerViewItem> getChildren() {
        return children != null ? children : new ArrayList<RecyclerViewItem>();
    }

    public void setChildren(List<? extends RecyclerViewItem> children) {
        for (RecyclerViewItem child : children) {
            addChild(child);
        };
    }

    public void addChild(RecyclerViewItem child) {
        child.parent = this;
        child.setLevel(level + 1);
        children.add(child);
        setSelected(allChildrenSelected(this));
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isSelected() {
        return selectable && selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSelectedParent() {
        if (parent != null) {
            parent.setSelected(allChildrenSelected(parent));
            parent.setSelectedParent();
        } else {
            setSelected(allChildrenSelected(this));
        }
    }

    public void toggle() {
        setSelected(!selected);
    }

    private boolean allChildrenSelected(RecyclerViewItem parent) {
        for (RecyclerViewItem child : parent.children) {
            if (!child.selected)
                return false;
        }
        return true;
    }

    public void setSelectedAllChildren(boolean selected) {
        for (RecyclerViewItem child : children) {
            child.setSelected(selected);
            if (child.hasChildren())
                child.setSelectedAllChildren(selected);
        }
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    private RecyclerViewItem(Parcel in) {
        level = in.readInt();
        parent = (RecyclerViewItem) in.readValue(RecyclerViewItem.class.getClassLoader());
        if (in.readByte() == 0x01) {
            children = new ArrayList<>();
            in.readList(children, RecyclerViewItem.class.getClassLoader());
        } else {
            children = null;
        }
        expanded = in.readByte() != 0x00;
        selected = in.readByte() != 0x00;
        selectable = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeValue(parent);
        if (children == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(children);
        }
        dest.writeByte((byte) (expanded ? 0x01 : 0x00));
        dest.writeByte((byte) (selected ? 0x01 : 0x00));
        dest.writeByte((byte) (selectable ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<RecyclerViewItem> CREATOR = new Creator<RecyclerViewItem>() {
        @Override
        public RecyclerViewItem createFromParcel(Parcel in) {
            return new RecyclerViewItem(in);
        }

        @Override
        public RecyclerViewItem[] newArray(int size) {
            return new RecyclerViewItem[size];
        }
    };

}
