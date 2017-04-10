package com.tuansbook.lvxing.Adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 * 带Header和Footer的RecyclerAdapter 基础Adapter 可添加Header和Footer
 * Class 'BaseAdapter' must either be declared abstract or implement abstract method 'onBindViewHolder(VH, int)' in 'Adapter
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0; // HEADER
    private static final int TYPE_BODY = 1; // BODY
    private static final int TYPE_FOOTER = 2; // FOOTER

    private View header;
    private View footer;
    private List<T> dataList = new ArrayList<>(); // 数据

    /**
     * 添加dataList
     */
    public void addData(List<T> dataList){
        this.dataList = dataList;
    }

    /**
     * 添加头部
     * @param header
     */
    public void addHeader(View header){
        this.header = header;
        this.notifyItemInserted(0); // 将header插入第一条
    }

    /**
     * 添加Footer
     * @param footer
     */
    public void addFooter(View footer){
        this.footer = footer;
        this.notifyItemInserted(getItemCount()); // 将footer插入最后一条
    }

    /**
     * 刷新
     * @param dataList
     */
    public void refreshList(List<T> dataList){
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }

    /**
     * 判断View类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){

        if(header == null){ // 头部

            if(footer == null){
                return TYPE_BODY;
            }else {
                if(position + 1 == getItemCount()){
                    return TYPE_FOOTER;
                }else {
                    return TYPE_BODY;
                }
            }
        }

        if(header != null){

            if(position == 0){
                return TYPE_HEADER;
            }else {
                if(footer == null){
                    return TYPE_BODY;
                }else {
                    if(position + 1 == getItemCount()){
                        return TYPE_FOOTER;
                    }else {
                        return TYPE_BODY;
                    }
                }
            }
        }
        return TYPE_BODY;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // GridLayoutManager 并且 Header或者footer不为 null 的情况下，Header和Footer各占一整行
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if(layoutManager instanceof GridLayoutManager){

            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (header != null && getItemViewType(position) == TYPE_HEADER) || ((footer != null && getItemViewType(position) == TYPE_FOOTER))?((GridLayoutManager) layoutManager).getSpanCount():1;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(header != null && viewType == TYPE_HEADER){
            return new MyHolder(header);
        }

        if(footer != null && viewType == TYPE_FOOTER){
            return new MyHolder(footer);
        }

        return createHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == TYPE_HEADER){
            return;
        }

        if(getItemViewType(position) == TYPE_FOOTER){
            return;
        }

        T data = dataList.get(getRealPosition(position));
        bindHolder(holder,getRealPosition(position),data);

    }

    @Override
    public int getItemCount() {
        // 算上Header和Footer
        return dataList.size() == 0?0:(header == null?(footer == null?dataList.size():dataList.size() + 1):(footer == null?dataList.size() + 1:dataList.size() + 2));
    }

    /**
     * ViewHolder
     */
    public class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 获取数据真正的位置 区分是否有header
     * @param position
     * @return
     */
    public int getRealPosition(int position){
        int pos;

        if(header != null){
            pos = position -1;
        }else {
            pos = position;
        }
        return pos;
    }

    /**
     * createHolder ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    public abstract RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType);

    /**
     * 绑定ViewHolder
     * @param holder
     * @param position
     */
    public abstract void bindHolder(RecyclerView.ViewHolder holder, int position, T data);
}
