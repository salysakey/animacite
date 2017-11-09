package org.anima.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by thiam on 02/07/15.
 */
public class Comment extends Activity {
    private String description;
    private String titre;
    private int id = -1;
    private int position = -1;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private int pictureId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail2);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                description = b.getString("description");
                titre = b.getString("titre");
                id = b.getInt("id");
                position = b.getInt("position");
                pictureId = b.getInt("pictureId");
            }
        }
        String[] resumes = getResources().getStringArray(R.array.resume_items2);
        ImageView avatar = (ImageView) findViewById(R.id.img);
        TextView txtTitre = (TextView) findViewById(R.id.titre);
        TextView txtDesc = (TextView) findViewById(R.id.descriptif);
        TextView txtResume = (TextView) findViewById(R.id.resume);

        if (id != -1) {
            avatar.setImageResource(pictureId);
        }
        // Verifier si titre est null or vide
        if (!TextUtils.isEmpty("Titre : " + titre)) {
            txtTitre.setText(titre);
        }
        //Verifier si description est null or vide
        if (!TextUtils.isEmpty("Descriptif : " + description)) {
            txtDesc.setText(description);
        }
        if (position != -1) {
            txtResume.setText(resumes[position]);
        }

        Button c = (Button) findViewById(R.id.Comment);
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(Comment.this, ImagePickActivity.class);

                startActivity(intent2);

                Toast.makeText(Comment.this, "Merci ! Les données ont été envoyées !", Toast.LENGTH_LONG).show();

            }
        });

        addListenerOnRatingBar();

    }
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //txtRatingValue.setText(String.valueOf(rating));

            }
        });
        */
    }


}
