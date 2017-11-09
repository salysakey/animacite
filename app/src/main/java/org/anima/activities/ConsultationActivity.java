package org.anima.activities;

import org.anima.animacite.R;

import android.os.Bundle;

/**
 * Activity composed by :
 * <ul>
 * <li>only the media list, if portrait</li>
 * <li>the media list and the detail of the selected media, if landscape.</li>
 * </ul>
 * 
 * @author Momo
 * 
 */
public class ConsultationActivity extends BaseConsultationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consultation);

	}

}
