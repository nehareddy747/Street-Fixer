package com.cmr.streetfixer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmr.streetfixer.databinding.ActivityDescPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DescPageActivity extends AppCompatActivity{

	private FirebaseFirestore dbf;
	private String uid;
	private String location;
	private String issues;
	private String image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityDescPageBinding binding = ActivityDescPageBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		dbf = FirebaseFirestore.getInstance();
		uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
		Button btnSubmit = binding.btnSubmit;
		EditText description = binding.etDescription;
		EditText suggestion = binding.etsuggest;

		Intent intentt = getIntent();
		if (intentt != null) {
			location = intentt.getStringExtra("location");
			issues = intentt.getStringExtra("issues");
			image = intentt.getStringExtra("img");
		}

		Map<String, Object> report = new HashMap<>();

		btnSubmit.setOnClickListener(v -> {
			if (!description.getText().toString().isEmpty() && !suggestion.getText().toString().isEmpty()) {
				String des = description.getText().toString();
				String sug = suggestion.getText().toString();
				report.put("issue", issues);
				report.put("description", des);
				report.put("location", location);
				report.put("suggestion", sug);
				report.put("image", image);
				report.put("isSolved", false);
				report.put("time", currentDateAndTime());

				dbf.collection(uid)
						.document(issues)
						.set(report)
						.addOnSuccessListener(documentReference -> Toast.makeText(DescPageActivity.this, "Problem reported successfully", Toast.LENGTH_SHORT).show())
						.addOnFailureListener(e -> Toast.makeText(DescPageActivity.this, "Error reporting problem!", Toast.LENGTH_SHORT).show());

				Intent intent = new Intent(DescPageActivity.this, HomePageActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(DescPageActivity.this, "Please fill description and suggestion!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private String currentDateAndTime() {
		// Get the current date and time
		LocalDateTime currentDateTime = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			currentDateTime = LocalDateTime.now();
		}
		// Format the date and time using a specific pattern
		DateTimeFormatter formatter = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy    HH:mm:ss");
		}
		String formattedDateTime = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			formattedDateTime = currentDateTime.format(formatter);
		}
		// Print the current date and time
		return formattedDateTime;
	}
}