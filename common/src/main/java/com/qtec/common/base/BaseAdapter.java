package com.qtec.common.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Base Adapter for RecyclerView, extends it to implement {@link #convertView(BaseViewHolder, Object)} method
 * @author gongw
 * @date 2019/1/11
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;
    protected int mLayoutRes;
    protected List<T> mDatas;

    public BaseAdapter(Context context, int layoutRes, List<T> datas){
        this.mContext = context;
        this.mLayoutRes = layoutRes;
        this.mDatas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemLayout(mDatas.get(position));
    }

    public int getItemLayout(T data){
        return mLayoutRes;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutRes, viewGroup, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        convertView(holder, mDatas.get(position));
    }

    protected abstract void convertView(BaseViewHolder viewHolder, T t);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

}
