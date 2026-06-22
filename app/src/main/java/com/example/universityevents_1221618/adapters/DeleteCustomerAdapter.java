package com.example.universityevents_1221618.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;

import java.util.List;

public class DeleteCustomerAdapter extends RecyclerView.Adapter<DeleteCustomerAdapter.ViewHolder> {

    private final List<User> userList;
    private final Context context;
    private final DatabaseHelper dbHelper;

    public DeleteCustomerAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_delete_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(holder.getAdapterPosition());

        holder.customerName.setText(user.getFirstName() + " " + user.getLastName());
        holder.customerEmail.setText(user.getEmail());

        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            new AlertDialog.Builder(context)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete " + user.getFirstName() + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (dbHelper.deleteUser(user.getId())) {
                            userList.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            Toast.makeText(context, "User deleted.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, customerEmail;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.item_customer_name_delete);
            customerEmail = itemView.findViewById(R.id.item_customer_email_delete);
            deleteButton = itemView.findViewById(R.id.item_delete_button);
        }
    }
}