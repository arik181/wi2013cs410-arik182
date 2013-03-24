package com.example.a3browser;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private WebView webview;
	private GestureLibrary gesturelib;
	private TextView textview;
	private GestureOverlayView gestureview; 

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		webview = (WebView) findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);		
		webview.setWebViewClient(new WebViewClient());

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				textview.setText(webview.getUrl());
			}
		});
		webview.setFocusableInTouchMode(true);
		webview.loadUrl("http://www.google.com");	
		
		textview = (TextView) findViewById(R.id.editText1);
		TextView.OnEditorActionListener doneListener = new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView textview, int actionId, KeyEvent event) {
			   if (actionId == EditorInfo.IME_ACTION_GO) {
				   webview.requestFocus();
				   String prefix = "http://";
				   String inputurl = textview.getText().toString();
				   String url = new String();
				   if (! inputurl.startsWith(prefix))
					   url = prefix + inputurl;
				   else
					   url = inputurl;
				   textview.setText(url);
				   webview.loadUrl(url);
				   return false;
			   }
			   return true;
			}
		};
		
		TextView.OnKeyListener enterListener = new TextView.OnKeyListener() {
			public boolean onKey(android.view.View view, int keycode, KeyEvent keyevent) {
				switch(keycode) {
					case KeyEvent.KEYCODE_ENTER:
						TextView tview = (TextView) view;
						webview.requestFocus();
						String prefix = "http://";
						String inputurl = tview.getText().toString();
						String url = new String();
						if (! inputurl.startsWith(prefix))
							url = prefix + inputurl;
						else
							url = inputurl;
						tview.setText(url);
						webview.loadUrl(url);
						return false;		
				}
				return false;
			}
		};
		
		textview.setOnKeyListener(enterListener);
		textview.setOnEditorActionListener(doneListener);
		
		gesturelib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		gestureview = (GestureOverlayView) findViewById(R.id.gestureoverlay);
		gesturelib.load();
		gestureview.addOnGesturePerformedListener(swipelistener);
		ViewGroup vg = ((ViewGroup) gestureview.getParent());
		vg.bringChildToFront(vg.getChildAt(0));
	}

	OnGesturePerformedListener swipelistener = new OnGesturePerformedListener() {
		@Override
		public void onGesturePerformed(GestureOverlayView overlay,
				Gesture gesture) {
			ArrayList<Prediction> prediction = gesturelib.recognize(gesture);

			Integer size  = prediction.size();
			String name = prediction.get(0).name;
			
			if (size > 0) {
				if (name.equals("left") &&
					webview.canGoBack()) {
					webview.goBack();
				} else if (name.equals("right") &&
						   webview.canGoForward()) {
					webview.goForward();
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

