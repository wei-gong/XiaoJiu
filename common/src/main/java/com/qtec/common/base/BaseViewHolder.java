package com.qtec.common.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * @author gongw
 * @date 2019/1/17
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View itemView;
    private SparseArray<View> views;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.views = new SparseArray<>();
    }

    public <V extends View> V getView(int resId){
        View view = views.get(resId);
        if(view == null){
            view = itemView.findViewById(resId);
            views.put(resId, view);
        }
        return (V) view;
    }

    public View getItemView(){
        return itemView;
    }

}
