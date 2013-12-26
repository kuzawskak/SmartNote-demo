package com.example.smartnote_demo;

import com.samsung.spenemulatorlibrary.ActivityWithSPenLayer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CanvasActivity extends ActivityWithSPenLayer {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_basic_ui);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
