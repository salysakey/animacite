package org.anima.activities;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.anima.adapters.AdapterQuestion;
import org.anima.adapters.FoodListAdapter;
import org.anima.database.AnimaDatabase;
import org.anima.database.CacheActuDB;
import org.anima.entities.Food;
import org.anima.entities.Project;
import org.anima.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Allows to choose beetween Taking the picture through camera (done) or Loading
 * an existing picture from the gallerie (not yet available)
 * 
 * @author Momo
 * 
 */
public class ImagePickActivity extends AppCompatActivity {
    private String TAG = "ImagePickActivity";
	int votes_minimum_signalement = 3;

	ProgressDialog prgDialog;
	// Error Msg TextView Object
	TextView errorMsg;
	// Email Edit View Object
	EditText emailET;
	// Passwprd Edit View Object
	EditText pwdET;

	String[] titles;
	int[] idents,date;
	String[] resumes;
	String[] desciptifs;
	String[] couleurs;
	TextView txtEmpty;
	String[] listPictures;
	private ListView listView;
	GoogleCloudMessaging gcmObj;
	String regId = "";
	int[] types;
	private SwipeRefreshLayout swipeRefreshLayout;

	List<Food> listFood;
	List<Food> listFoodNew;
	//GridView grid;

	private StaggeredGridView mGridView;
	EditText search;
	private Toolbar toolbar;
	private DrawerLayout drawerLayout;
	private MenuItem menuSearch, signalement;
	private MenuItem menuHide;
	private AdapterQuestion mAdapter;
	private Menu menudeux;
	private PagerAdapter mPagerAdapter;
	private CacheActuDB db;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_image);

		db = new AnimaDatabase(this).getCacheDB();
		initToolbar();
		setupDrawerLayout();
		//getregistrationId();

		//AVOIR LE ON BOARDING OU NON
		if(PrefManager.getFirstStart(getApplicationContext())) {
			Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
			startActivity(intent);
			//setContentView(R.layout.tuto);
            //UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
			//indicator.setViewPager(mPager);
			//indicator.setFades(false);
			PrefManager.setFirstStart(getApplicationContext(),false);
		}

        FacebookSdk.sdkInitialize(getApplicationContext());

		mGridView = findViewById(R.id.grid);
		search = (EditText) findViewById(R.id.search);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // Set Progress Dialog Text
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Chargement...");

		invokeWS();

		prgDialog.setCancelable(false);
        //prgDialog.hide(); A ENLEVER

        //setFilter(); //Mise en place du filtre en bas à droite
        setScrollRefresh(); //Mise en place de rafraichissement en tirant en bas

		if (PrefManager.getRatingStatus(ImagePickActivity.this)==1){
			//Envoie de la registration id si il est pas nul
			if(PrefManager.getRegistrationId(getApplicationContext()).length()!=0 ) {
				RequestParams params2 = new RequestParams();
				params2.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
				// Put Http parameter password with value of Password Edit Value control
				params2.put("IdAndroid", "" + PrefManager.getRegistrationId(getApplicationContext()));
				invokeWSRegistration(params2);
			}
		}else {
			PrefManager.setRatingStatus(ImagePickActivity.this, 0);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.setIntent(intent);
	}

	//Affichage de la tuile selectionnée
	// Select Item
    public void showPickActivity(int index) {

		// Where "EmptyActivity.class" is Item Detail Activity or Consultation
		Intent intent = new Intent( ImagePickActivity.this , EmptyActivity.class);
		Food selectedFood = listFood.get(index);

		if(selectedFood.getDescriptif()=="" && selectedFood.getType()==1){

			if (PrefManager.getRatingStatus(getApplicationContext())==0){



				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImagePickActivity.this);
				// Setting Dialog Title
				alertDialog.setTitle("Vous n'etes pas inscrit");
				alertDialog.setIcon(R.drawable.logo48);
				// Setting Dialog Message
				alertDialog.setMessage("Inscrivez vous pour participer");
				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//Move to Next screen
								Intent chnIntent = new Intent(ImagePickActivity.this, LoginActivity.class);
								startActivity(chnIntent);
							}
						});
				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("Non",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Cancel Dialog
								dialog.cancel();
							}
						});
				// Showing Alert Message
				AlertDialog alert = alertDialog.create();
				alert.show();

			}else{

				decision(index);
			}

		}else{

			Bundle b = new Bundle();
			b.putString("description", selectedFood.getName());
			b.putString("titre", selectedFood.getDescriptif());
            b.putString("color", selectedFood.getCouleur());
			b.putInt("ident", selectedFood.getIdent());
			b.putInt("position", index);
			b.putString("pictureUrl", selectedFood.getPictureUrl());
			b.putInt("tipe", selectedFood.getType());
			b.putLong("date", selectedFood.getDate());
			b.putBoolean("affichercalendar",true);
			intent.putExtras(b);
			startActivity(intent);
		}



	}

	public void decision( int index){

		Food selectedFood = listFood.get(index);
		// Which Sondage.class is Consultantation Activity
		Intent intent = new Intent( ImagePickActivity.this , Sondage.class);

		Bundle b = new Bundle();

		b.putInt("id", selectedFood.getIdent());
		b.putString("titre", selectedFood.getDescriptif());

		if(selectedFood.getDate()<=0){
			b.putBoolean("resultat",true);
		}else{
			b.putBoolean("resultat", false);
		}

		intent.putExtras(b);

		startActivity(intent);


	}

	public void invokeWS() {
		// Show Progress Dialog
		if (!prgDialog.isShowing()) {
			Log.d(TAG, " - Before show");
			prgDialog.show();
		}
		else{
            Log.d(TAG, " - invokeWS progs not showing");
        }
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, " - invokeWS beforeGet doActu");
		client.get(ConfigurationVille.Debut_WS + "/actu/doactu", new AsyncHttpResponseHandler() {
			//client.get("http://192.168.0.10:8080/Anima/rest/actu/doactu", new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
                Log.d(TAG, " - invokeWS on Success");
				try {
					Calendar cl = Calendar.getInstance();
					Calendar current = Calendar.getInstance();
					current.setTimeInMillis(System.currentTimeMillis());


					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					if (obj.getBoolean("status")) {
                        Log.d(TAG, " - invokeWS status is ok");

						JSONArray jarray = obj.getJSONArray("phoneNumber");
						listPictures = new String[jarray.length()];
						titles = new String[jarray.length()];
						resumes = new String[jarray.length()];
                        couleurs = new String[jarray.length()];
                        types = new int[jarray.length()];
						date = new int[jarray.length()];
						desciptifs = new String[jarray.length()];
						idents = new int[jarray.length()];

						for (int i = 0; i < jarray.length(); i++) {
							JSONObject object = jarray.getJSONObject(i);


							Project project = new Project();

							project.setTitle(object.getString("Titre"));
							project.setResume(object.getString("resume"));
                            project.setCouleur(object.getString("couleur"));
                            project.setPicture(object.getString("picture"));
							project.setType(object.getInt("type"));
							project.setDate_debut(object.getLong("date_debut"));
							project.setDate_fin(object.getLong("date_fin"));
							project.setDescription(object.getString("description"));

							if (project.getType() == 3) {
								cl.setTimeInMillis(project.getDate_debut());
							} else {
								cl.setTimeInMillis(project.getDate_fin());
							}


							//date[i] = cl.get(Calendar.DAY_OF_YEAR) - current.get(Calendar.DAY_OF_YEAR);
							long diff = (object.getLong("date_fin")) - (System.currentTimeMillis());
							int days = (int) (diff / (1000*60*60*24));
							date[i] = days;

							titles[i] = project.getTitle();
							resumes[i] = project.getResume();
                            couleurs[i] = project.getCouleur();
                            idents[i] = object.getInt("id");
							desciptifs[i] = project.getDescription();
							//listPictures[i]="http://i.imgur.com/DvpvklR.png";
							listPictures[i] = project.getPicture();
							types[i] = project.getType();
							//Picasso.with(ImagePickActivity.this).load(R.drawable.piscine).placeholder(listPictures[i]);
                            //Navigate to Home screen

						}
						Log.d(TAG, " - Before hide");
						prgDialog.hide();
						Log.d(TAG, " - After hide");
                        prgDialog.hide();
                        Log.d(TAG, " - After 2 hide");
						listFood = null;
                        prgDialog.hide();
						init();



						RequestParams params3 = new RequestParams();
						params3.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
						invokeWSsignalement(params3);

						RequestParams params2 = new RequestParams();
						params2.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
						// Put Http parameter password with value of Password Edit Value control
						//params2.put("IdAndroid", regId);

						//invokeWSRegistration(params2);//ATTENTION A REMETTRE SI ON VEUX LIMITATION DU NOMBRE VOTE AVANT SIGNALEMENT
						//Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, " - End of invokeWS");
                    }else {
                        Log.d(TAG, " - End of invokeWS No Status");
						Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
						prgDialog.hide();
                        prgDialog.hide();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
                    prgDialog.hide();
                    prgDialog.hide();
					Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors du chargement des publications, vérifiez votre connection internet et réessayez.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}

			// When the response returned by REST has Http response code other than '200'
			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {
                Log.d(TAG," - invokeWS.asyncHttpResponse faillure");
				listFood = db.getFoodsFromCache();
				if (listFood != null && listFood.size() > 0) {
					init();
				}
				// Hide Progress Dialog
				prgDialog.hide();
                Log.d(TAG, " - After hide");
                prgDialog.hide();
				// When Http response code is '404'
				if (statusCode == 404) {
					Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
				}
				// When Http response code is '500'
				else if (statusCode == 500) {
					Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
				}
				// When Http response code other than 404, 500
				else {
					Toast.makeText(getApplicationContext(), "Vous n'êtes plus connecté à internet, veuillez réessayez. :)", Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public void invokeWSRegistration(RequestParams params) {

		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		//client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/register/doregid", params, new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
			}
			// When the response returned by REST has Http response code other than '200'
			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {

				// When Http response code is '404'
				if (statusCode == 404) {
					Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
				}
				// When Http response code is '500'
				else if (statusCode == 500) {
					Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
				}
				// When Http response code other than 404, 500
				else {
					Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void invokeWSsignalement(RequestParams params) {
		// Show Progress Dialog

		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		//client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
		client.get(ConfigurationVille.Debut_WS + "/classement/targetpseudo", params, new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {

				try {

					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					if (obj !=null) {
						JSONArray jarray = obj.getJSONArray("classement");
						for (int i = 0; i < jarray.length(); i++) {

							// JSON Object
							JSONObject object = jarray.getJSONObject(i);
							// When the JSON response has status boolean value assigned with true

							//Affichage de la visibilité du signalement
							if (object.getInt("score") == ConfigurationVille.NB_VOTES_MINIMUM_SIGNALEMENT && PrefManager.getFirstSignalement(getApplicationContext())==false) {
							if(false){
								//if(PrefManager.getFirstSignalement(getApplicationContext())==false){
									AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImagePickActivity.this);
									// Setting Dialog Title
									alertDialog.setTitle("Bravo");
									alertDialog.setIcon(R.drawable.petite_image);
									// Setting Dialog Message
									alertDialog.setMessage("Vous pouvez accéder à la nouvelle fonctionnalité signalement (en haut à droite)");
									// Setting Positive "Yes" Button
									// Setting Negative "NO" Button
									alertDialog.setNegativeButton("Super !",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
												}
											});
									// Showing Alert Message
									AlertDialog alert = alertDialog.create();
									alert.show();

									PrefManager.setFirstSignalement(getApplicationContext(), true);
								}
							}
                            //MISE EN PLACE DE L'AFFICHAGE DU SIGNALEMENT
                            //Pas de limitation du nombre de votes pour avoir accès au signalement
							if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            //if(object.getInt("score") >= ConfigurationVille.NB_VOTES_MINIMUM_SIGNALEMENT){
                                signalement.setVisible(true);
                            }else{
                                signalement.setVisible(false);
                            }
						}
					}


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "ErrorSignalement Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
					e.printStackTrace();

				}
				// Hide Progress Dialog


			}

			// When the response returned by REST has Http response code other than '200'
			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {
				// Hide Progress Dialog

				// When Http response code is '404'
				if (statusCode == 404) {
					Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
				}
				// When Http response code is '500'
				else if (statusCode == 500) {
					Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
				}
				// When Http response code other than 404, 500
				else {
					Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void init(){

		if (listFood == null) {
			listFood = new ArrayList<Food>();


			for (int i = listPictures.length - 1; i >= 0; i--) {
				listFood.add(new Food(i + 1, titles[i], listPictures[i],
						resumes[i], desciptifs[i], types[i], idents[i],date[i], couleurs[i]));
			}
			db.saveCache(listFood);
		}

		mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFood));
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				filtrer();
			}
		});

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (listFoodNew != null && listFoodNew.get(position).getId() == -4) {
				} else {
					if (listFoodNew != null) {
						//Toast.makeText(ImagePickActivity.this, "Selected Position: " + listFoodNew.get(position).toString(), Toast.LENGTH_SHORT).show();
						Food lolo = listFoodNew.get(position);
						int b = 0;
						for (int i = 0; i < listFood.size(); i++) {
							if (lolo.getId() == listFood.get(i).getId()) {
								b = i;
							}
						}
						showPickActivity(b);
					} else {

						showPickActivity(position);

					}
				}
			}
		});
	}

	public void filtrer() {
		// retourner la chaine saisie par l'utilisateur
		String name = search.getText().toString();
		// créer une nouvelle liste qui va contenir la résultat à afficher
		listFoodNew = new ArrayList<Food>();

		for (Food food : listFood) {
			// si le nom du food commence par la chaine saisie , ajouter-le !
			if (food.getName().toLowerCase().toString().startsWith(name)) {
				listFoodNew.add(food);
			}
		}
		// vider la liste
		mGridView.setAdapter(null);
		if (listFoodNew.size() == 0) {

			listFoodNew.add(new Food(-4, "Pas d'élements.. réessayer !",
					"--", "--", "--", 1,1,1));

		}
		// ajouter la nouvelle liste
		mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));

	}

	public void filtrer2() {
		// retourner la chaine saisie par l'utilisateur
		String name = search.getText().toString();
		// créer une nouvelle liste qui va contenir la résultat à afficher
		listFoodNew = new ArrayList<Food>();

		for (Food food : listFood) {
			// si le nom du food commence par la chaine saisie , ajouter-le !
			if (food.getType()==2) {
				listFoodNew.add(food);
			}
		}
		// vider la liste
		mGridView.setAdapter(null);
		if (listFoodNew.size() == 0) {

			listFoodNew.add(new Food(-4, "Pas d'élements.. réessayer !",
					"--", "--", "--", 1,1,1));

		}
		// ajouter la nouvelle liste
		mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));

	}

	public void filtrer3() {
		// retourner la chaine saisie par l'utilisateur
		String name = search.getText().toString();
		// créer une nouvelle liste qui va contenir la résultat à afficher
		listFoodNew = new ArrayList<Food>();

		for (Food food : listFood) {
			// si le nom du food commence par la chaine saisie , ajouter-le !
			if (food.getType()==3) {
				listFoodNew.add(food);
			}
		}
		// vider la liste
		mGridView.setAdapter(null);
		if (listFoodNew.size() == 0) {

			listFoodNew.add(new Food(-4, "Pas d'élements.. réessayer !",
					"--", "--", "--", 1,1,1));

		}
		// ajouter la nouvelle liste
		mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));

	}

	public void filtrer1() {
		// retourner la chaine saisie par l'utilisateur
		String name = search.getText().toString();
		// créer une nouvelle liste qui va contenir la résultat à afficher
		listFoodNew = new ArrayList<Food>();

		for (Food food : listFood) {
			// si le nom du food commence par la chaine saisie , ajouter-le !
			if (food.getType()==1) {
				listFoodNew.add(food);
			}
		}
		// vider la liste
		mGridView.setAdapter(null);
		if (listFoodNew.size() == 0) {

			listFoodNew.add(new Food(-4, "Pas d'élements.. réessayer !",
					"--", "--", "--", 1,1,1));

		}
		// ajouter la nouvelle liste
		mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));

	}

	public void filtrer4() {
		// retourner la chaine saisie par l'utilisateur
		String name = search.getText().toString();
		// créer une nouvelle liste qui va contenir la résultat à afficher
		listFoodNew = new ArrayList<Food>();

		for (Food food : listFood) {
			// si le nom du food commence par la chaine saisie , ajouter-le !
			listFoodNew.add(food);
		}
		// vider la liste
		mGridView.setAdapter(null);
		if (listFoodNew.size() == 0) {

			listFoodNew.add(new Food(-4, "Pas d'élements.. réessayer !",
					"--", "--", "--", 1,1,1));

		}
		// ajouter la nouvelle liste
		mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));

	}

    private void setFilter(){
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.filtre));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();



        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        int subActionButtonSize = 130;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);
        itemBuilder.setLayoutParams(params);
// repeat many times:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.filtre1));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(getResources().getDrawable(R.drawable.filtre2));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageDrawable(getResources().getDrawable(R.drawable.filtre3));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageDrawable(getResources().getDrawable(R.drawable.home48));
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                // ...
                .attachTo(actionButton)
                .build();



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrer1();
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrer2();
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrer3();
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrer4();
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);
            }
        });
    }

    private void setScrollRefresh(){
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (mGridView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mGridView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    //boolean topOfFirstItemVisible = mGridView.getChildAt(0).getTop() == 21;

                    //Toast.makeText(getApplicationContext(), "on est a : "+mGridView.getChildAt(0).getTop()+"", Toast.LENGTH_LONG).show();
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible;
                    //&&topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeRefreshLayout.setRefreshing(true);
                invokeWS();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

	private void initToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		AppCompatActivity dodo = new AppCompatActivity();
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setTitle("Dans ma ville...");
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}


	}

	public void getregistrationId(){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					regId = gcmObj.register("455491445841");
					msg = "Registration ID :" + regId;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				if (!TextUtils.isEmpty(regId)) {
					// Store RegId created by GCM Server in SharedPref
					//    storeRegIdinSharedPref(applicationContext, regId, emailID);
					//  Toast.makeText(applicationContext, "Registered with GCM Server successfully.nn" + msg, Toast.LENGTH_SHORT).show();
				} else {
					//  Toast.makeText(applicationContext, "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg, Toast.LENGTH_LONG).show();
				}
			}
		}.execute(null, null, null);
	}

	/**
	 * Refactored by Saly Sakey November-07-2017
	 */
	private void setupDrawerLayout() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
		new Navigation(ImagePickActivity.this, drawerLayout, view);
	}// end function

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		menuSearch = menu.findItem(R.id.menu_search);
		signalement= menu.findItem(R.id.signalement);
		menuHide = menu.findItem(R.id.menu_hide);
        if(PrefManager.getRatingStatus(getApplicationContext()) == 1){
            signalement.setVisible(true);
        }
        else{
            signalement.setVisible(false);
        }


		if(search.getVisibility() ==View.VISIBLE){
			//menuSearch.setVisible(false);
			menuHide.setVisible(true);
		}else{
			//menuSearch.setVisible(true);
			menuHide.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
				return true;
			case R.id.menu_search:
				search.setVisibility(View.VISIBLE);
				menuSearch.setVisible(false);
				menuHide.setVisible(true);
				//Intent intent = new Intent(ImagePickActivity.this, SignalementActivity.class);
				//startActivity(intent);
				return true;
			case R.id.menu_hide:
				search.setVisibility(View.GONE);
				menuSearch.setVisible(true);
				menuHide.setVisible(false);
				return true;
			case R.id.signalement:
				Intent intent = new Intent(ImagePickActivity.this, SignalementActivity.class);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}






}
