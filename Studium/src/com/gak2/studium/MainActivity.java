package com.gak2.studium;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gak2.studium.SubjectReaderContract.SubjectEntry;


public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SubjectReaderDbHelper subjectHelper = new SubjectReaderDbHelper(this);
		SQLiteDatabase db = subjectHelper.getWritableDatabase();
		
		// Test array of strings
		String[] subjectArray = {"History 3", "Computational Molecular Evolution", "Cryptography"};
		
		ContentValues values;
		for (int i = 0; i < subjectArray.length; i++) {
			values = new ContentValues();
			values.put(SubjectEntry.COLUMN_NAME_TITLE, subjectArray[i]);
		
			long newRowId;
			newRowId = db.insert(SubjectEntry.TABLE_NAME, null, values);
		}		
		db.close();
		

		// Display the items in the database
		db = subjectHelper.getReadableDatabase();
		String[] projection = {
				SubjectEntry._ID,
				SubjectEntry.COLUMN_NAME_TITLE
		};
		
		Cursor cursor = db.query(SubjectEntry.TABLE_NAME, projection, null, null, null, null, null);
		cursor.moveToFirst();
		
		db.close();
		
		String[] fromColumns =  {
				SubjectEntry.COLUMN_NAME_TITLE};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this,
				android.R.layout.simple_list_item_1,
				cursor,
				fromColumns,
				new int[] {android.R.id.text1},
				0);
				
		GridView gridview= (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		
		// Remember: We get the specific grid item by passing the position
		gridview.setOnItemClickListener(subjectClickedHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private OnItemClickListener subjectClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			
			SubjectReaderDbHelper subjectHelper = new SubjectReaderDbHelper(MainActivity.this);
			SQLiteDatabase db = subjectHelper.getWritableDatabase();
			
			// Get the subject ID to be deleted
			//Cursor c = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
		//	c.moveToPosition(position);
	
			
			// Delete it from the database
			db.delete(SubjectEntry.TABLE_NAME, SubjectEntry._ID, new String[] {"" + id});
			((SimpleCursorAdapter)parent.getAdapter()).notifyDataSetChanged();
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
