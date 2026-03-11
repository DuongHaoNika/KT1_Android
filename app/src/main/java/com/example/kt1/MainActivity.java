package com.example.kt1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.kt1.adapter.RoomAdapter;
import com.example.kt1.model.Room;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private Button btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo
        recyclerView = findViewById(R.id.recyclerView);
        btnAddRoom = findViewById(R.id.btnAddRoom);

        roomList = new ArrayList<>();
        adapter = new RoomAdapter(roomList, this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Thêm dữ liệu mẫu
        addSampleData();

        // Xử lý sự kiện thêm phòng
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditDialog(null, -1);
            }
        });
    }

    private void addSampleData() {
        roomList.add(new Room("P001", "Phòng 101", 3000000, "Còn trống", "", ""));
        roomList.add(new Room("P002", "Phòng 102", 3500000, "Đã thuê", "Nguyễn Văn A", "0123456789"));
        roomList.add(new Room("P003", "Phòng 103", 3200000, "Còn trống", "", ""));
        adapter.notifyDataSetChanged();
    }

    private void showAddEditDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_room, null);
        builder.setView(dialogView);

        EditText etId = dialogView.findViewById(R.id.etId);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        RadioGroup rgStatus = dialogView.findViewById(R.id.rgStatus);
        RadioButton rbAvailable = dialogView.findViewById(R.id.rbAvailable);
        RadioButton rbRented = dialogView.findViewById(R.id.rbRented);
        EditText etTenantName = dialogView.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = dialogView.findViewById(R.id.etPhoneNumber);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Nếu là sửa thì hiển thị dữ liệu cũ
        if (room != null) {
            etId.setText(room.getId());
            etId.setEnabled(false); // Không cho sửa mã phòng
            etName.setText(room.getName());
            etPrice.setText(String.valueOf(room.getPrice()));
            if (room.getStatus().equals("Còn trống")) {
                rbAvailable.setChecked(true);
            } else {
                rbRented.setChecked(true);
            }
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate dữ liệu
                String id = etId.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String priceStr = etPrice.getText().toString().trim();
                String status = rbAvailable.isChecked() ? "Còn trống" : "Đã thuê";
                String tenantName = etTenantName.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();

                if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Giá thuê không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra mã phòng không trùng khi thêm mới
                if (room == null) {
                    for (Room r : roomList) {
                        if (r.getId().equals(id)) {
                            Toast.makeText(MainActivity.this, "Mã phòng đã tồn tại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                if (room == null) {
                    // Thêm mới
                    Room newRoom = new Room(id, name, price, status, tenantName, phoneNumber);
                    roomList.add(newRoom);
                    Toast.makeText(MainActivity.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Cập nhật
                    room.setName(name);
                    room.setPrice(price);
                    room.setStatus(status);
                    room.setTenantName(tenantName);
                    room.setPhoneNumber(phoneNumber);
                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showDeleteConfirmation(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    roomList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    public void editRoom(int position) {
        Room room = roomList.get(position);
        showAddEditDialog(room, position);
    }
}
