package com.baoge.wnotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baoge.wnotes.R;
import com.baoge.wnotes.util.LogUtil;

import java.util.List;

public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.ViewHolder> {
    private List<String> datas;
    private OnItemLongClickLitener onItemLongClickLitener;

    public TextViewAdapter() {

    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public interface OnItemLongClickLitener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickLitener(OnItemLongClickLitener onItemLongClickLitener) {
        this.onItemLongClickLitener = onItemLongClickLitener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lay_textview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        String cityName = datas.get(position);
        viewHolder.name.setText(cityName);

        if (onItemLongClickLitener != null) {
            viewHolder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickLitener.onItemLongClick(view, position);
                    return true;
                }


            });
        }
    }

    @Override
    public int getItemCount() {
        int count = datas == null ? 0 : datas.size();
        LogUtil.i("count:" + count);
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        View itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            itemLayout = itemView.findViewById(R.id.item_root);
        }
    }


}
