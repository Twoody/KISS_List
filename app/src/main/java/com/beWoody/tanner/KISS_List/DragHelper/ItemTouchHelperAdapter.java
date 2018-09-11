package com.beWoody.tanner.KISS_List.DragHelper;

public interface ItemTouchHelperAdapter {
    Boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
