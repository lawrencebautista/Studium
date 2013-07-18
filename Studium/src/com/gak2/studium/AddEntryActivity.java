package com.gak2.studium;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gak2.studium.EntryReaderContract.StudyEntry;

public class AddEntryActivity extends Activity {

	protected long subjectId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_entry);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		subjectId = intent.getLongExtra(SubjectActivity.EXTRA_SUBJ_ID, 0);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	
	public void addStatement(View view) {
		//TODO implement this to update the database. Will need to create a new table and make additional SQLiteOpener class as well as  a contract
		
		EntryReaderDbHelper entryHelper = new EntryReaderDbHelper(this);
		SQLiteDatabase db;
		db = entryHelper.getWritableDatabase();
		
		EditText editText = (EditText) findViewById(R.id.add_entry_text);
		String newEntry = editText.getText().toString();
		
		if (!(newEntry == null) && !newEntry.equals("")) {
			EditText responseText = (EditText) findViewById(R.id.add_response_text);
			String newResponse = responseText.getText().toString();
			
			
			ContentValues values;
			values = new ContentValues();
			values.put(StudyEntry.COLUMN_NAME_SUBJ_ID, subjectId);
			values.put(StudyEntry.COLUMN_NAME_ENTRY, newEntry);
			values.put(StudyEntry.COLUMN_NAME_RESPONSE, newResponse);
			
			db.insert(StudyEntry.TABLE_NAME, null, values);	
		}
		else {
			Context context = getApplicationContext();
			CharSequence message = getString(R.string.add_entry_error);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, message, duration);
			toast.show();
		}
		db.close();
		
		finish();
		return;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_entry, menu);
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
