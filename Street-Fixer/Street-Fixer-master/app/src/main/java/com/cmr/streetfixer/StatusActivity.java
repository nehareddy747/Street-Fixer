package com.cmr.streetfixer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatusActivity extends AppCompatActivity {
	private StatusAdapter statusAdapter;
	private final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
	private List<StatusItem> statusList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		// Initialize an empty list or fetch data from Firestore
		statusList = new ArrayList<>();  // Initialize the list
		fetchDataFromFirestore();
		// Initialize the adapter with the statusList
		statusAdapter = new StatusAdapter(statusList);
		// Set the adapter to the RecyclerView
		recyclerView.setAdapter(statusAdapter);

		statusAdapter = new StatusAdapter(statusList, statusItem -> {
			// Handle item click, for example, start a new activity
			Intent intent = new Intent(StatusActivity.this, DisplayActivity.class);
			// Pass necessary data to the next activity
			intent.putExtra("issue", statusItem.getIssue());
			startActivity(intent);
		});

		recyclerView.setAdapter(statusAdapter);
	}

	// Method to fetch data from Firestore or create a sample list for demonstration
	@SuppressLint("NotifyDataSetChanged")
	private void fetchDataFromFirestore() {
		List<StatusItem> statusItemList = new ArrayList<>();

		FirebaseFirestore db = FirebaseFirestore.getInstance();
		CollectionReference userCollection = db.collection(uid);

		userCollection.get().addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				for (QueryDocumentSnapshot document : task.getResult()) {
					// Assuming "issue" and "status" are fields in your documents
					String issue = document.getString("issue");
					Boolean statusf = document.getBoolean("isSolved");
					String status;

					if (Boolean.TRUE.equals(statusf))
						status = "Solved";
					else
						status = "Pending";
					// Create a new StatusItem and add it to the list
					StatusItem statusItem = new StatusItem(issue, status);
					statusItemList.add(statusItem);
				}

				// Update the adapter with the new data
				statusList.addAll(statusItemList);
				statusAdapter.notifyDataSetChanged();
			} else {
				// Handle errors
				Log.e("Firestore", "Error getting documents: ", task.getException());
			}
		});
	}
}
