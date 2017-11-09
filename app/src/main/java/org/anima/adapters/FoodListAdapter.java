package org.anima.adapters;
import org.anima.animacite.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import org.anima.entities.Food;

import java.util.List;
import java.util.Random;

/**
 * Created by thiam on 03/07/15.
 * Pour modifier l'apparence des tuiles sur le fil d'actualité (je crois)
 */
public class FoodListAdapter extends BaseAdapter {

    private static final String TAG = "SampleAdapter";
    private static Random mRandom;
    private List<Food> listFood = null;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    LayoutInflater layoutInflater;
    Context context;
    private int lastPosition = -1;

    // constructeur
    public FoodListAdapter(Context context, List<Food> listFood) {
        this.listFood = listFood;
        layoutInflater = LayoutInflater.from(context);
        this.listFood = listFood;
        this.context = context;
        this.mRandom = new Random();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listFood.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listFood.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    static class ViewHolder {
        TextView nomView;
        TextView priceView;
        TextView date;
        DynamicHeightImageView pictureView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView=null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.projet1, null);
            holder = new ViewHolder();
            // initialisation des vues
            if ( listFood.get(position).getType()==1){
                holder.nomView = (TextView) convertView.findViewById(R.id.name2);
                holder.nomView.setVisibility(View.VISIBLE);
                holder.date = (TextView) convertView.findViewById(R.id.date);
            }else if(listFood.get(position).getType()==2){
                //Association des views qu'il faut pour chaque titre de post si besoin
                if(listFood.get(position).getName().equals("Alerte") ) {
                    holder.nomView = (TextView) convertView.findViewById(R.id.name_alerte);
                }else if (listFood.get(position).getName().equals("Commerce")){
                    holder.nomView = (TextView) convertView.findViewById(R.id.name_commerce);
                }else{
                    holder.nomView = (TextView) convertView.findViewById(R.id.name);
                }
                holder.nomView.setVisibility(View.VISIBLE);
                //METTRE COULEUR RECU PAR WS
                //holder.nomView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.date = (TextView) convertView.findViewById(R.id.date);
            }else{
                if(listFood.get(position).getName().equals("Alerte") ){
                    holder.nomView = (TextView) convertView.findViewById(R.id.name_alerte);
                }else if (listFood.get(position).getName().equals("Commerce")){
                    holder.nomView = (TextView) convertView.findViewById(R.id.name_commerce);
                }else{
                    holder.nomView = (TextView) convertView.findViewById(R.id.name);
                }
                holder.nomView.setVisibility(View.VISIBLE);
                holder.date = (TextView) convertView.findViewById(R.id.date);
            }

            /*if(!listFood.get(position).getCouleur().isEmpty()) {
                String color = listFood.get(position).getCouleur();
                holder.nomView.setBackgroundColor(Color.parseColor(color));
            }*/


            //JON holder.date.setVisibility(View.VISIBLE);
            holder.priceView = (TextView) convertView.findViewById(R.id.price);
            holder.pictureView = (DynamicHeightImageView) convertView
                    .findViewById(R.id.picture);
            if(listFood.get(position).getPictureUrl()!="null" && !listFood.get(position).getPictureUrl().equals("null")){
                holder.pictureView.setHeightRatio(0.75);

            }

            if(!"".equals(listFood.get(position).getCouleur())) {
                String color = listFood.get(position).getCouleur();
                if(color.substring(0, 1).equals("#"))
                    holder.nomView.setBackgroundColor(Color.parseColor(color));
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        // affchier les données convenablement dans leurs positions
        holder.nomView.setText(listFood.get(position).getName());
        if(listFood.get(position).getType()==1){

            if(listFood.get(position).getDate()<=0){
                holder.date.setText("Consultation terminée");
                holder.date.setTextColor(context.getResources().getColor(R.color.orange));
            }else{
                holder.date.setText("Il reste " + listFood.get(position).getDate() + " jours");
                holder.date.setTextColor(context.getResources().getColor(R.color.vert));
            }

            holder.date.setVisibility(View.VISIBLE);
        }else if(listFood.get(position).getType()==3){

            if(listFood.get(position).getDate()<0){
                holder.date.setText("Évènement terminé");
                holder.date.setTextColor(context.getResources().getColor(R.color.orange));
            }else if(listFood.get(position).getDate()==0){
                holder.date.setText("Évènement en cours");
                holder.date.setTextColor(context.getResources().getColor(R.color.orange));
            }
            else{
                holder.date.setText("Dans " + listFood.get(position).getDate() + " jours");
                holder.date.setTextColor(context.getResources().getColor(R.color.vert));
            }

            holder.date.setVisibility(View.VISIBLE);
        }else{
            //holder.date.setVisibility(View.GONE );
            //holder.date.setText("Information");
        }


        holder.priceView.setText(String.valueOf(listFood.get(position).getPrice()) + "");

        Picasso.with(context).load(listFood.get(position).getPictureUrl()).into(holder.pictureView);

        lastPosition = position;
        return convertView;

    }


    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
// if not yet done generate and stash the columns height
// in our real world scenario this will be determined by
// some match based on the known height and width of the image
// and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
// the width
    }
}