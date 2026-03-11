package com.example.kt1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kt1.R;
import com.example.kt1.MainActivity;
import com.example.kt1.model.Room;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private MainActivity mainActivity;

    public RoomAdapter(List<Room> roomList, MainActivity mainActivity) {
        this.roomList = roomList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getName());

        // Format giá tiền
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(formatter.format(room.getPrice()));

        holder.tvStatus.setText(room.getStatus());

        // Tô màu theo trạng thái
        if (room.getStatus().equals("Còn trống")) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(android.R.color.holo_green_light));
        } else {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_red_dark));
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(android.R.color.holo_red_light));
        }

        // Hiển thị thông tin người thuê nếu có
        if (!room.getTenantName().isEmpty()) {
            holder.tvTenantInfo.setText("Người thuê: " + room.getTenantName() + " - " + room.getPhoneNumber());
            holder.tvTenantInfo.setVisibility(View.VISIBLE);
        } else {
            holder.tvTenantInfo.setVisibility(View.GONE);
        }

        // Click để sửa
        holder.itemView.setOnClickListener(v -> {
            mainActivity.editRoom(position);
        });

        // Nhấn giữ để xóa
        holder.itemView.setOnLongClickListener(v -> {
            mainActivity.showDeleteConfirmation(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvPrice, tvStatus, tvTenantInfo;
        CardView cardView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTenantInfo = itemView.findViewById(R.id.tvTenantInfo);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}