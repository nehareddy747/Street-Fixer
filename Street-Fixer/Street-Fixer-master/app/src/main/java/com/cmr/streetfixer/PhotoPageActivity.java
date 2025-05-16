package com.cmr.streetfixer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cmr.streetfixer.databinding.ActivityPhotoPageBinding;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class PhotoPageActivity extends AppCompatActivity {
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 123; // You can use any unique value
	StorageReference storageReference;
	LinearProgressIndicator progress;
	Uri image;
	Button btnPickImage, btnNext, addLocation;
	ImageView imageView;
	EditText location;
	String issues;

	private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
		@Override
		public void onActivityResult(ActivityResult result) {

			if (result.getResultCode() == RESULT_OK) {
				btnPickImage.setEnabled(true);
				if (result.getData() != null) {
					image = result.getData().getData();
					btnPickImage.setEnabled(true);
					Glide.with(getApplicationContext()).load(image).into(imageView);
				}
			} else {
				Toast.makeText(PhotoPageActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
			}
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityPhotoPageBinding binding = ActivityPhotoPageBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		FirebaseApp.initializeApp(PhotoPageActivity.this);
		storageReference = FirebaseStorage.getInstance().getReference();

		Toolbar toolbar = binding.welcomeTextView;
		setSupportActionBar(toolbar);

		progress = binding.progress;
		imageView = binding.imageView;
		btnPickImage = binding.btnPickImage;
		btnNext = binding.btnNext;
		location = binding.etLocationAddress;
		addLocation = binding.btnPicklocation;

		btnNext.setOnClickListener(v -> uploadImage(image));

		btnPickImage.setOnClickListener(v -> {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			activityResultLauncher.launch(intent);
		});

		addLocation.setOnClickListener(v -> {
			// Check if location permissions are granted
			if (ActivityCompat.checkSelfPermission(PhotoPageActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(PhotoPageActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// Request location permissions if not granted
				ActivityCompat.requestPermissions(PhotoPageActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
			} else {
				// Location permissions are granted, proceed to get the last known location
				getLocation();
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Location permissions granted, proceed to get the last known location
				getLocation();
			} else {
				// Location permissions denied, handle accordingly (e.g., show a message)
				Toast.makeText(PhotoPageActivity.this, "Location permissions denied", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Get the last known location
	private void getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		@SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			String locationText = "Latitude: " + lastKnownLocation.getLatitude() + ", Longitude: " + lastKnownLocation.getLongitude();
			location.setText(locationText);

		} else {
			Toast.makeText(PhotoPageActivity.this, "Last known location not available", Toast.LENGTH_SHORT).show();
		}
	}


	private void uploadImage(Uri image) {
		if (image == null) {
			Toast.makeText(PhotoPageActivity.this, "Please upload an image of the problem!", Toast.LENGTH_SHORT).show();
			return;
		}

		StorageReference reference = storageReference.child("image/" + UUID.randomUUID().toString());
		reference.putFile(image)
				.addOnSuccessListener(taskSnapshot -> {
					// Image uploaded successfully, now get the download URL
					reference.getDownloadUrl().addOnSuccessListener(uri -> {
						// Store the download URL in a variable
						String downloadUrl = uri.toString();

						// Rest of your code...
						if (!location.getText().toString().isEmpty()) {
							Intent i = getIntent();
							if (i != null)
								issues = i.getStringExtra("selected");
							else
								issues = null;
							Intent intent = new Intent(PhotoPageActivity.this, DescPageActivity.class);
							intent.putExtra("issues", issues);
							intent.putExtra("location", location.getText().toString());
							intent.putExtra("img", downloadUrl);
							startActivity(intent);
						} else {
							Toast.makeText(PhotoPageActivity.this, "Please add location!", Toast.LENGTH_SHORT).show();
						}
					});
				})
				.addOnFailureListener(e -> Toast.makeText(PhotoPageActivity.this, "There was an error while uploading", Toast.LENGTH_SHORT).show())
				.addOnProgressListener(snapshot -> {
					progress.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
					progress.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
				});
	}
}