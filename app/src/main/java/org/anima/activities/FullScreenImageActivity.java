package org.anima.activities;

import android.os.Bundle;
import android.widget.Button;

/**
 * Activity showing the media in full screen <br/>
 * Needs an Intent with the StringExtra "mediaId"
 * 
 * @author Momo
 * 
 */
public class FullScreenImageActivity extends BaseConsultationActivity {

	private String description;
	private String resume;
	private String titre;
	private int id = -1;
	private int ident;
	private int position = -1;
	private String pictureUrl = "";
	private int tipe;
	private Button z=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen_image);

		Intent intent = getIntent();
		if (intent != null) {
			Bundle b = intent.getExtras();
			if (b != null) {
				description = b.getString("description");
				titre = b.getString("titre");
				id = b.getInt("id");
				ident =b.getInt("ident");
				position = b.getInt("position");
				pictureUrl = b.getString("pictureUrl");
				tipe=b.getInt("tipe");
			}
		}
		String[] resumes = getResources().getStringArray(R.array.resume_items);
		ImageView avatar = (ImageView) findViewById(R.id.img);
		TextView txtTitre = (TextView) findViewById(R.id.titre);
		TextView txtDesc = (TextView) findViewById(R.id.descriptif);
		TextView txtResume = (TextView) findViewById(R.id.resume);

		if (id != -1) {
			//avatar.setImageResource(pictureId);
			Picasso.with(this).load(pictureUrl).into(avatar);
		}
		// Verifier si titre est null or vide
		if (!TextUtils.isEmpty("Titre : " + titre)) {
			txtTitre.setText(titre);
		}
		// Verifier si description est null or vide
		if (!TextUtils.isEmpty("Descriptif : " + description)) {
			txtDesc.setText(description);
		}

		if (position != -1 && resumes.length > position) {
			txtResume.setText(resumes[position]);
		}



		final Button z = (Button) findViewById(R.id.Comment2);
		z.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
                Food objectToRemove = new Food(id, titre, pictureUrl,resume, description, tipe,ident);
                FollowModel.getInstance().removeFromList(objectToRemove);
				Toast.makeText(FullScreenImageActivity.this, "Merci ! Vous ne suivez plus ce projet!", Toast.LENGTH_LONG).show();

				//On désactive le bouton
				z.setEnabled(false);
                startActivity(new Intent(FullScreenImageActivity.this, Follows.class));
			}
		});

		*/

	}
}

