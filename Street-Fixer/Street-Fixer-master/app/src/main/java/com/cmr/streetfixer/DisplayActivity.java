package com.cmr.streetfixer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cmr.streetfixer.databinding.ActivityDisplayBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DisplayActivity extends AppCompatActivity {
	ActivityDisplayBinding binding;
	private TextView issueTextView, locationTextView, descriptionTextView, suggestionsTextView, statusTextView;
	private ImageView image;
	private String imgAddress;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityDisplayBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		issueTextView = binding.issueTextView;
		locationTextView = binding.locationTextView;
		descriptionTextView = binding.descriptionTextView;
		suggestionsTextView = binding.suggestionsTextView;
		statusTextView = binding.statusTextView;
		image = binding.issueImg;

		// Get the current user
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		if (currentUser != null) {
			Intent i = getIntent();
			if (i != null) {
				String docId = i.getStringExtra("issue");
				// Retrieve data from Firestore using the user's UID as the collection name
				FirebaseFirestore db = FirebaseFirestore.getInstance();
				String userUid = currentUser.getUid();
				assert docId != null;
				db.collection(userUid).document(docId)
						.get()
						.addOnCompleteListener(task -> {
							if (task.isSuccessful()) {
								DocumentSnapshot document = task.getResult();
								if (document.exists()) {
									// Map document fields to TextViews
									String issue = document.getString("issue");
									String location = document.getString("location");
									String description = document.getString("description");
									String suggestions = document.getString("suggestion");
									Boolean status = document.getBoolean("isSolved");
									imgAddress = document.getString("image");

									// Set TextViews with Firestore data
									issueTextView.setText("Issue: " + issue);
									locationTextView.setText("Location: " + location);
									descriptionTextView.setText("Description: " + description);
									suggestionsTextView.setText("Suggestions: " + suggestions);
									if (Boolean.TRUE.equals(status))
										statusTextView.setText("Issue has been solved!");
									else
										statusTextView.setText("Issue is still under process...");
									Glide.with(DisplayActivity.this).load(imgAddress).into(image);
								} else {
									Log.d("StatusActivity", "No such document");
								}
							} else {
								Log.w("StatusActivity", "Error getting documents.", task.getException());
							}
						});
			} else {
				Toast.makeText(DisplayActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
			}
		} else {
			// Handle the case where the user is not authenticated
			// Redirect to login or perform any other necessary actions
			Toast.makeText(DisplayActivity.this, "User is not logged in!", Toast.LENGTH_SHORT).show();
		}
	}
}