package com.gak2.studium;

import com.gak2.studium.EntryReaderContract.StudyEntry;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class EditEntryActivity extends Activity {

	protected String entryText;
	protected String responseText;
	protected long entryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_entry);
		// Show the Up button in the action bar.
		setupActionBar();
	
		Intent intent = getIntent();
		entryId = intent.getLongExtra(SubjectActivity.EXTRA_ENTRY_ID, 0);
		entryText = intent.getStringExtra(SubjectActivity.EXTRA_ENTRY);
		responseText = intent.getStringExtra(SubjectActivity.EXTRA_RESPONSE);
	}
	
	protected void onStart() {
		super.onStart();
		EditText editEntry = (EditText) findViewById(R.id.edit_entry_text);
		EditText responseEntry = (EditText) findViewById(R.id.edit_response_text);
		
		editEntry.setText(entryText);
		responseEntry.setText(responseText);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void editEntry(View view) {
		
		EntryReaderDbHelper entryHelper = new EntryReaderDbHelper(this);
		SQLiteDatabase db;
		db = entryHelper.getWritableDatabase();
		
		EditText editText = (EditText) findViewById(R.id.edit_entry_text);
		String newEntry = editText.getText().toString();
		
		if (!(newEntry == null) && !newEntry.equals("")) {
			EditText responseText = (EditText) findViewById(R.id.edit_response_text);
			String newResponse = responseText.getText().toString();
			
			
			ContentValues values;
			values = new ContentValues();
			//values.put(StudyEntry.COLUMN_NAME_SUBJ_ID, subjectId);
			values.put(StudyEntry.COLUMN_NAME_ENTRY, newEntry);
			values.put(StudyEntry.COLUMN_NAME_RESPONSE, newResponse);
			
			db.update(StudyEntry.TABLE_NAME,
					values,
					StudyEntry._ID + "=?",
					new String[] {"" + entryId});	
		}
		else {
			Context context = getApplicationContext();
			CharSequence message = getString(R.string.edit_entry_error);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, message, duration);
			toast.show();
		}
		db.close();
		
		finish();
		return;
	}
	
	public void deleteEntry(View view) {
		new AlertDialog.Builder(this)
	    .setTitle("Delete entry")
	    .setMessage("Are you sure you want to delete this entry?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	EntryReaderDbHelper entryHelper = new EntryReaderDbHelper(EditEntryActivity.this);
				SQLiteDatabase db = entryHelper.getWritableDatabase();
				
				// Delete it from the database
				db.delete(StudyEntry.TABLE_NAME, StudyEntry._ID + "=?", new String[] {"" + entryId});
				db.close();
				finish();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            return;
	        }
	     })
	     .show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_entry, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}
		return super.onOptionsItemSelected(item);
	}

}
