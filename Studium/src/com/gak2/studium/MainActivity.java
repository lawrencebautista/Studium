package com.gak2.studium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	SubjectReaderDbHelper subjectHelper = new SubjectReaderDbHelper(this);
	//	SQLiteDatabase db = subjectHelper.getReadableDatabase();
	//	db.close();
		
		// Test array of strings
		String[] subjectArray = {"History 3", "Computational Molecular Evolution", "Cryptography"};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectArray);
		
		GridView gridview= (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(subjectClickedHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private OnItemClickListener subjectClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			// Do something in response to the click
		}
	};
	
	public void createSubject(View view) {
		;
	}
	
	// TBI
	public void viewSubject(View view) {
		Intent intent = new Intent(this, SubjectActivity.class);
	}

}
