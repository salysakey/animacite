package org.anima.activities;

import android.support.v4.app.FragmentActivity;

/**
 * Created by thiam on 09/07/15.
 */
public class Follows extends FragmentActivity {

    /*

        String[] titles;
        String[] descriptifs;

        List<Food> listFood;
        GridView grid;
        EditText search;
        TextView txtEmpty;
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.pick_image);
            titles = getResources().getStringArray(R.array.title_items);
            descriptifs = getResources().getStringArray(R.array.descriptif_items);
            grid = (GridView) findViewById(R.id.grid);
            search = (EditText) findViewById(R.id.search);
            txtEmpty = (TextView) findViewById(R.id.empty_list);
            listFood = FollowModel.getInstance().getLists();
            if (listFood != null && listFood.size() == 0) {
                txtEmpty.setVisibility(View.VISIBLE);
                grid.setVisibility(View.GONE);
            }else {
                txtEmpty.setVisibility(View.GONE);
                grid.setVisibility(View.VISIBLE);
                grid.setAdapter(new FoodListAdapter(getApplicationContext(), listFood));
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

                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(Follows.this, "Selected Position: " + position, Toast.LENGTH_SHORT).show();
                        showPickActivity(position);
                    }
                });
            }

        }

        public void showPickActivity(int index) {
            Intent intent = new Intent( Follows.this , FullScreenImageActivity.class);
            Food selectedFood = listFood.get(index);

            Bundle b = new Bundle();
            b.putString("description", selectedFood.getName());
            b.putString("titre", selectedFood.getPrice());
            b.putInt("id", selectedFood.getId());
            b.putInt("position", index);
            b.putString("pictureUrl", selectedFood.getPictureUrl());
            b.putInt("tipe", selectedFood.getType());

            intent.putExtras(b);

            startActivity(intent);
        }

	/*@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//showDetails(position);
		showPickActivity(position);
	}

	public void showPickActivity(int index) {
		Intent intent = new Intent( ImagePickActivity.this , EmptyActivity.class);



		Food selectedFood = (Food)
				getListView().getItemAtPosition(index);

		Bundle b = new Bundle();
		b.putString("description", selectedFood.getName());
		b.putString("titre", selectedFood.getPrice());
		b.putInt("id", selectedFood.getId());
		b.putInt("position", index);

		intent.putExtras(b);

		startActivity(intent);
	}*/

    /*


        public void filtrer() {
            // retourner la chaine saisie par l'utilisateur
            String name = search.getText().toString();
            // créer une nouvelle liste qui va contenir la résultat à afficher
            ArrayList<Food> listFoodNew = new ArrayList<Food>();

            for (Food food : listFood) {
                // si le nom du food commence par la chaine saisie , ajouter-le !
                if (food.getName().toLowerCase().toString().startsWith(name)) {
                    listFoodNew.add(food);
                }
            }
            // vider la liste
            grid.setAdapter(null);
            if (listFoodNew.size() == 0) {
                listFoodNew.add(new Food(100, "Pas d'élements.. réessayer !",
                        "blabla","", "",1,1));
            }
            // ajouter la nouvelle liste
            grid.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));
        }

*/
    }
