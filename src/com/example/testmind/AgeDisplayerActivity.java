package com.example.testmind;

import com.example.testmind.view.TestRectangle;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

public class AgeDisplayerActivity extends Activity {

	public static final String AGE = "age";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		
		super.onCreate(savedInstanceState);	
		/*
		TextView tv = new TextView(this);
		setContentView(tv);
		
		Intent i = getIntent();
		int age = i.getIntExtra(AGE, -1);
		displayAge(age,tv);
		*/
		
		TestRectangle rect = new TestRectangle(this);
		setContentView(rect);
	}
	
	private void displayAge(int age, TextView tv) {
		Resources res = getResources();
		String text = res.getString(R.string.displayed_age, age);
		tv.setText(text);
	}
}
