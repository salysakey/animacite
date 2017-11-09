package org.anima.activities;
import org.anima.animacite.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.anima.utils.PrefManager;
import org.anima.views.UpdatableLicenceSingletonView;

/**
 * Displays a picture during 1 second at each launch of the application
 * 
 * @author Momo
 * 
 */
public class SplashActivity extends Activity {
	private static final int STOPSPLASH = 0;

	/**
	 * Default duration for the splash screen (milliseconds)
	 */
	private static final long SPLASHTIME = 3000;

	/**
	 * Handler to close this activity and to start automatically
	 * {@link ConsultationActivity} after <code>SPLASHTIME</code> seconds.
	 */
	private final transient Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == STOPSPLASH) {

				if(PrefManager.getFirstStart(getApplicationContext())) {
					Intent intent =new Intent(getApplicationContext(),TutorialActivity.class);
					startActivity(intent);
					PrefManager.setFirstStart(getApplicationContext(), false);
				}else{
					/*final Intent intent = new Intent(SplashActivity.this,
							ImagePickActivity.class);*/
					final Intent intent = new Intent(SplashActivity.this,
							ImagePickActivity.class);
					startActivity(intent);
				}
				finish();
			}

			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// load all the utils in the first activity
		UpdatableLicenceSingletonView.getInstance().getLicences();

		final Message msg = new Message();
		msg.what = STOPSPLASH;
		splashHandler.sendMessageDelayed(msg, SPLASHTIME);
	}

}