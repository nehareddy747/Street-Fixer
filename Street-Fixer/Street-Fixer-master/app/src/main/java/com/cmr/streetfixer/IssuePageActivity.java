package com.cmr.streetfixer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmr.streetfixer.databinding.ActivityIssuePageBinding;

public class IssuePageActivity extends AppCompatActivity{

	String selectedButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityIssuePageBinding binding = ActivityIssuePageBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		Button btnNext = binding.btnNext;
		btnNext.setOnClickListener(v -> {
			Intent intent = new Intent(IssuePageActivity.this, PhotoPageActivity.class);
			intent.putExtra("selected", selectedButton);
			startActivity(intent);
		});
	}
	public void checkbutton(View view) {
		int viewId = view.getId();

		if (viewId == R.id.btnRoadDamage ||
				viewId == R.id.btnDrainageLeakage ||
				viewId == R.id.btnGarbage ||
				viewId == R.id.btnStreetLights ||
				viewId == R.id.btnStreetDogs ||
				viewId == R.id.btnWaterLeakage) {

			// Find the RadioButton corresponding to the selected viewId
			RadioButton radioButton = findViewById(viewId);
			radioButton.setChecked(true);

			// Display a toast
			selectedButton = radioButton.getText().toString();
			Toast.makeText(this, selectedButton + " selected", Toast.LENGTH_SHORT).show();
		}
	}
}
