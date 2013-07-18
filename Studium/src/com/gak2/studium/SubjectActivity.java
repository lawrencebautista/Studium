package com.gak2.studium;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gak2.studium.EntryReaderContract.StudyEntry;

public class SubjectActivity extends Activity {
	public static final String EXTRA_MESSAGE = "com.gak2.studium.";
	public static final String EXTRA_SUBJ_ID = EXTRA_MESSAGE + "SUBJ_ID";
	public static final String EXTRA_ENTRY_ID = EXTRA_MESSAGE + "ENTRY_ID";
	public static final String EXTRA_SUBJ_NAME = EXTRA_MESSAGE + "SUBJ_NAME";
	public static final String EXTRA_ENTRY = EXTRA_MESSAGE + "ENTRY";
	public static final String EXTRA_RESPONSE = EXTRA_MESSAGE + "RESPONSE";
	protected long subjectId;
	protected String subjectName;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject);
		// Show the Up button in the action bar.
		setupActionBar();

		// Get the subject this came from
		Intent intent = getIntent();
		subjectId = intent.getLongExtra(MainActivity.EXTRA_SUBJ_ID, 0);
		subjectName = intent.getStringExtra(MainActivity.EXTRA_SUBJ_NAME);

		setTitle(subjectName);
		
	}
	
	protected void onStart() {
		super.onStart();
		updateUI();
	}
	
	protected void updateUI() {
		EntryReaderDbHelper entryHelper = new EntryReaderDbHelper(this);
		SQLiteDatabase db = entryHelper.getReadableDatabase();
		String[] projection = {
				StudyEntry._ID,
				StudyEntry.COLUMN_NAME_SUBJ_ID,
				StudyEntry.COLUMN_NAME_TYPE,
				StudyEntry.COLUMN_NAME_UNIT,
				StudyEntry.COLUMN_NAME_CHAPTER,
				StudyEntry.COLUMN_NAME_ENTRY,
				StudyEntry.COLUMN_NAME_RESPONSE
		};
		Cursor cursor = db.query(StudyEntry.TABLE_NAME,
				projection,
				StudyEntry.COLUMN_NAME_SUBJ_ID + "=?",
				new String[] {"" + subjectId},
				null, null, null);
		cursor.moveToFirst();
		
		
		String[] fromColumns =  {
				
				StudyEntry.COLUMN_NAME_ENTRY,
				StudyEntry.COLUMN_NAME_RESPONSE
		};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this,
				android.R.layout.simple_list_item_2,
				cursor,
				fromColumns,
				new int[] {android.R.id.text1, android.R.id.text2},
				0);
				
		ListView listview= (ListView) findViewById(R.id.entry_listview);
		listview.setAdapter(adapter);
		db.close();
		
		listview.setOnItemClickListener(entryClickedHandler);
		listview.setOnItemLongClickListener(entryLongClickedHandler);
	}
	
	private OnItemClickListener entryClickedHandler = new OnItemClickListener() {
		// Edit the entry
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Intent intent = new Intent(SubjectActivity.this, EditEntryActivity.class);
			Cursor c = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
			c.moveToPosition(position);
			String s = c.getString(1); // Gets the string, which is the second item in the cursor
			String entry = c.getString(5);
			String response = c.getString(6);
			
			intent.putExtra(EXTRA_ENTRY_ID, id); // Pass it the id of the subject we're concerned with
			intent.putExtra(EXTRA_SUBJ_NAME, s);
			intent.putExtra(EXTRA_ENTRY, entry);
			intent.putExtra(EXTRA_RESPONSE, response);
			startActivity(intent);
		}
	};
	
	private OnItemLongClickListener entryLongClickedHandler = new OnItemLongClickListener() {
		public boolean onItemLongClick(final AdapterView<?> parent, View v, int position, final long id) {
			new AlertDialog.Builder(SubjectActivity.this)
		    .setTitle("Delete entry")
		    .setMessage("Are you sure you want to delete this entry?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	EntryReaderDbHelper entryHelper = new EntryReaderDbHelper(SubjectActivity.this);
					SQLiteDatabase db = entryHelper.getWritableDatabase();
					
					// Delete it from the database
					db.delete(StudyEntry.TABLE_NAME, StudyEntry._ID + "=?", new String[] {"" + id});
					//((SimpleCursorAdapter)parent.getAdapter()).notifyDataSetChanged();
					db.close();
					
					((SimpleCursorAdapter)parent.getAdapter()).notifyDataSetChanged();
					updateUI();
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

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subject, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_add_entry:
			intent = new Intent(this, AddEntryActivity.class);
			intent.putExtra(EXTRA_SUBJ_ID, subjectId);
			startActivity(intent);
			return true;
		case R.id.menu_quiz:
			intent = new Intent(this, QuizActivity.class);
			intent.putExtra(EXTRA_SUBJ_ID, subjectId);
			intent.putExtra(EXTRA_SUBJ_NAME, subjectName);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}

}
