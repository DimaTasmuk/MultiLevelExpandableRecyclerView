package com.dtasmuk.multilevelexpandablerecyclerview;

public enum ItemSelectableType {
    NONE(false),
    MULTI_SELECT(true),
    SINGLE_SELECT(true);

    ItemSelectableType(boolean selectable) {
        this.selectable = selectable;
    }

    private boolean selectable;

    public boolean isSelectable() {
        return selectable;
    }
}
