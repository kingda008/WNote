package com.baoge.wnotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baoge.wnotes.R;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.util.DateFormat;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<Order> orders;

    private OnItemLongClickLitener onItemLongClickLitener;
    private OnItemClickLitener onItemClickLitener;

    public interface OnItemLongClickLitener {
        void onItemLongClick(View view, int position);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemLongClickLitener(OnItemLongClickLitener onItemLongClickLitener) {
        this.onItemLongClickLitener = onItemLongClickLitener;
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public OrderListAdapter() {

    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        viewHolder.time.setText("日期：" + DateFormat.getDate(orders.get(position).getOrderTime(), DateFormat.FORMAT_YYYY_MM_DD));
        viewHolder.hospital.setText("医院：" + orders.get(position).getHospital());
        viewHolder.device.setText("设备：" + orders.get(position).getDevice());


        if (onItemLongClickLitener != null) {
            viewHolder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickLitener.onItemLongClick(view, position);
                    return true;
                }


            });
        }
        if (onItemClickLitener != null) {
            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitener.onItemClick(v, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView hospital;
        TextView device;
        View itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            hospital = itemView.findViewById(R.id.hospital);
            device = itemView.findViewById(R.id.device);
            itemLayout = itemView.findViewById(R.id.item_root);
        }
    }


}
