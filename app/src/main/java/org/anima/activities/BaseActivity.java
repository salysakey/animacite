package org.anima.activities;


import org.anima.animacite.R;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * 
 * Common parent of all the activities of the application
 * Each activity should (maybe indirectly) extends it
 * 
 *  @author Marie
 */


public class BaseActivity extends FragmentActivity {

	/**
	 * Inflate the default menu
	 */

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_consultation, menu);
		//Intent intent2 = new Intent(this, Login.class);
		//startActivity(intent2);

		return true;
	}

	/**
	 * Handling the menu clicks commun to all the activities
	 */

	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(getApplicationContext(), "Click on concours!", Toast.LENGTH_SHORT).show();
		// Handle item selection
		switch (item.getItemId()) {
			case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return true;
		case R.id.medialist:
			Intent intent3 = new Intent(this, ImagePickActivity.class);
			startActivity(intent3);
			return true;
		case R.id.my_account:
			Intent intent2 = new Intent(this, LoginActivity.class);
			startActivity(intent2);
			return true;
		case R.id.takephoto:
			Intent intent4 = new Intent(this, Follows.class);
			startActivity(intent4);
			return true;
		case R.id.concours:
			Intent intent_concours = new Intent(this, ConcoursActivity.class);
			startActivity(intent_concours);
			return true;

		//case R.id.map_filter:
		//	final Intent intent5 = new Intent(BaseActivity.this,
		//			MapActivity.class);
		//	startActivity(intent5);
		//	finish();
		//	return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

 }

