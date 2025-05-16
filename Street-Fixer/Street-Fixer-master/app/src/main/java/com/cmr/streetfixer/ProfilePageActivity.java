package com.cmr.streetfixer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmr.streetfixer.databinding.ActivityProfilePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfilePageActivity extends AppCompatActivity{
	private ActivityProfilePageBinding binding;
	String userId;
	String nameTextView, emailTextView, phoneTextView, addressTextView;
	FirebaseDatabase db;
	DatabaseReference reference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.btnNext.setOnClickListener(v -> {
			nameTextView = binding.nameTextView.getText().toString();
			emailTextView = binding.emailTextView.getText().toString();
			phoneTextView = binding.phoneTextView.getText().toString();
			addressTextView = binding.addressTextView.getText().toString();
			userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

			Users users;
			if (!nameTextView.isEmpty() && !emailTextView.isEmpty() && !phoneTextView.isEmpty() && !addressTextView.isEmpty()) {
				users = new Users(nameTextView, emailTextView, phoneTextView, addressTextView);
				db = FirebaseDatabase.getInstance();
				reference = db.getReference("Users");

				// Use push() to generate a unique key for each user
				reference.child(userId).setValue(users)
						.addOnCompleteListener(task -> {
							if (task.isSuccessful()) {
								Toast.makeText(ProfilePageActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
								// Navigate to HomePageActivity after saving
								Intent intent = new Intent(ProfilePageActivity.this, HomePageActivity.class);
								startActivity(intent);
								finish(); // Close this activity to prevent going back
							} else {
								Toast.makeText(ProfilePageActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
							}
						});
			} else {
				Toast.makeText(ProfilePageActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
