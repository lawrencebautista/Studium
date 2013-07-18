package com.gak2.studium;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.gak2.studium.EntryReaderContract.StudyEntry;
import com.gak2.studium.SubjectReaderContract.SubjectEntry;


public class MainActivity extends Activity {
	public static final String EXTRA_MESSAGE = "com.gak2.studium.";
	public static final String EXTRA_SUBJ_ID = EXTRA_MESSAGE + "SUBJ_ID";
	public static final String EXTRA_SUBJ_NAME = EXTRA_MESSAGE + "SUBJ_NAME";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		updateGrid();
		
		
	}
	
	protected void updateGrid() {
		SubjectReaderDbHelper subjectHelper = new SubjectReaderDbHelper(this);
		SQLiteDatabase db = subjectHelper.getReadableDatabase();
		String[] projection = {
				SubjectEntry._ID,
				SubjectEntry.COLUMN_NAME_TITLE
		};
		Cursor cursor = db.query(SubjectEntry.TABLE_NAME, projection, null, null, null, null, null);
		cursor.moveToFirst();
		
		
		String[] fromColumns =  {
				SubjectEntry.COLUMN_NAME_TITLE,
				SubjectEntry._ID};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this,
				android.R.layout.simple_list_item_2,
				cursor,
				fromColumns,
				new int[] {android.R.id.text1, android.R.id.text2},
				0);
				
		GridView gridview= (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		db.close();
		// Remember: We get the specific grid item by passing the position
		gridview.setOnItemClickListener(subjectClickedHandler);
		gridview.setOnItemLongClickListener(subjectLongClickedHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private OnItemClickListener subjectClickedHandler = new OnItemClickListener() {
		// Start the subject Activity
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Intent intent = new Intent(MainActivity.this, SubjectActivity.class);
			Cursor c = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
			c.moveToPosition(position);
			String s = c.getString(1); // Gets the string, which is the second item in the cursor
			
			intent.putExtra(EXTRA_SUBJ_ID, id); // Pass it the id of the subject we're concerned with
			intent.putExtra(EXTRA_SUBJ_NAME, s);
			startActivity(intent);
		}
	};
		
	
	private OnItemLongClickListener subjectLongClickedHandler = new OnItemLongClickListener() {
		public boolean onItemLongClick(final AdapterView<?> parent, View v, int position, final long id) {
			new AlertDialog.Builder(MainActivity.this)
		    .setTitle("Delete subject")
		    .setMessage("Are you sure you want to delete this subject?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	SubjectReaderDbHelper subjectHelper = new SubjectReaderDbHelper(MainActivity.this);
					SQLiteDatabase db = subjectHelper.getWritableDatabase();
					
					// Delete it from the database
					db.delete(SubjectEntry.TABLE_NAME, SubjectEntry._ID + "=?", new String[] {"" + id});
					//((SimpleCursorAdapter)parent.getAdapter()).notifyDataSetChanged();
					db.close();
					
					// Delete all the entries from the entry database
					db = (new EntryReaderDbHelper(MainActivity.this)).getWritableDatabase();
					db.delete(StudyEntry.TABLE_NAME, StudyEntry.COLUMN_NAME_SUBJ_ID + "=?", new String[] {"" + id});
					db.close();
					
					((SimpleCursorAdapter)parent.getAdapter()).notifyDataSetChanged();
					updateGrid();
		        }
		     })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            return;
		        }
		     })
		     .show();

			return true;
		}
	};
	
	
	public void createSubject(View view) {
		SubjectReaderDbHelper subjectHelper = new SubjectReaderDbHelper(this);
		SQLiteDatabase db;
		db = subjectHelper.getWritableDatabase();
		
		EditText editText = (EditText) findViewById(R.id.add_subject);
		String newSubject = editText.getText().toString();
		
		if (!(newSubject == null) && !newSubject.equals("")) {
			ContentValues values;
			values = new ContentValues();
			values.put(SubjectEntry.COLUMN_NAME_TITLE, newSubject);
			
			db.insert(SubjectEntry.TABLE_NAME, null, values);	
			editText.setText("");
			updateGrid();
		}
		db.close();
	}
	

}
