package com.baoge.wnotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baoge.wnotes.R;
import com.baoge.wnotes.db.Device;
import com.baoge.wnotes.util.LogUtil;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private List<Device> datas;

    private OnItemLongClickLitener onItemLongClickLitener;
    public interface OnItemLongClickLitener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickLitener(OnItemLongClickLitener onItemLongClickLitener) {
        this.onItemLongClickLitener = onItemLongClickLitener;
    }
    public DeviceAdapter() {

    }

    public void setDatas(List<Device> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv2_look, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Device device = datas.get(position);

        viewHolder.name.setText(device.getName());
        viewHolder.price.setText(device.getPrice() + "");


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
        TextView price;
        ImageView look;
        View itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_root);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            look = itemView.findViewById(R.id.img_look);

            look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (price.getVisibility() == View.VISIBLE) {
                        price.setVisibility(View.INVISIBLE);
                    } else {
                        price.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }


}
