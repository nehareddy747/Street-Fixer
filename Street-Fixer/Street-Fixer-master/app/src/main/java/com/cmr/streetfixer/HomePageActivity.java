package com.cmr.streetfixer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

		Button reportProblem = findViewById(R.id.ReportProblem);

		reportProblem.setOnClickListener(v -> {
			Intent intent = new Intent(HomePageActivity.this, IssuePageActivity.class);
			startActivity(intent);
		});
		bottomNavigationView.setSelectedItemId(R.id.home);
		bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
			if (item.getItemId() == R.id.userProfile) {
				Intent profileIntent = new Intent(HomePageActivity.this, ProfilePageActivity.class);
				startActivity(profileIntent);
			} else if (item.getItemId() == R.id.home) {
				bottomNavigationView.setSelectedItemId(R.id.home);
			} else if (item.getItemId() == R.id.status) {
				Intent profileIntent = new Intent(HomePageActivity.this, StatusActivity.class);
				startActivity(profileIntent);
			}
			return true;
		});
	}
}