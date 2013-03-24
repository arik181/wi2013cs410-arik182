package com.example.a3browser.test;

import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import com.example.a3browser.MainActivity;
import com.example.a3browser.R;


public class ValidURLTest extends ActivityUnitTestCase<MainActivity> {

	TextView textview;
	MainActivity mainActivity;
	
	public ValidURLTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
		textview = (TextView) mainActivity.findViewById(R.id.editText1);
	}
	
	public void testValidURL() {
    	textview.setText("www.slashdot.org");
		assertTrue(textview.getText().equals("www.slashdot.org"));
		mainActivity.finish();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
