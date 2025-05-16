package com.cmr.streetfixer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

	private final List<StatusItem> statusList;
	private final OnItemClickListener listener;

	// Constructor with listener
	public StatusAdapter(List<StatusItem> statusList, OnItemClickListener listener) {
		this.statusList = statusList;
		this.listener = listener;
	}

	// Constructor without listener
	public StatusAdapter(List<StatusItem> statusList) {
		this.statusList = statusList;
		this.listener = null; // You can choose to set it to null or provide a default listener if needed
	}

	@NonNull
	@Override
	public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_status, parent, false);
		return new StatusViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
		StatusItem statusItem = statusList.get(position);

		holder.tvSNo.setText(String.valueOf(position + 1));
		holder.tvProblem.setText(statusItem.getIssue());
		holder.tvStatus.setText(statusItem.getStatus());

		// Set click listener on the item if a listener is provided
		if (listener != null) {
			holder.itemView.setOnClickListener(view -> {
				if (position != RecyclerView.NO_POSITION) {
					// Get the clicked item and pass it to the listener
					listener.onItemClick(statusItem);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return statusList.size();
	}

	static class StatusViewHolder extends RecyclerView.ViewHolder {
		TextView tvSNo;
		TextView tvProblem;
		TextView tvStatus;

		public StatusViewHolder(@NonNull View itemView) {
			super(itemView);

			tvSNo = itemView.findViewById(R.id.tvSNo);
			tvProblem = itemView.findViewById(R.id.tvProblem);
			tvStatus = itemView.findViewById(R.id.tvStatus);
		}
	}
}
