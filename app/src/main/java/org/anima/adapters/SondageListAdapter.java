package org.anima.adapters;
import org.anima.animacite.R;
import android.app.Activity;
import android.content.Context;
//import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.anima.entities.Proposition;
import org.anima.entities.Question;

import java.util.ArrayList;
import java.util.List;


public class SondageListAdapter extends ArrayAdapter<Question> implements RatingBar.OnRatingBarChangeListener, AdapterView.OnItemClickListener {
    private String TAG = "SondageListAdapter";
    public static final int AVAILABLE_TAG = -1;
    private final Context context;
    private Question question;
    private final List<Question> questions;
    public int [] allReponses;
    public boolean resultat;
    public int globalnbrselection;
    public List<ResultatsProposition> reponses = new ArrayList<>();
    private List<QuestionResponse> ratingResponses = new ArrayList<>();
    private List<RatingResponseNUm> ratingResponses2 = new ArrayList<>();

    private List<QuestionResponse> formResponses = new ArrayList<>();
    private List<QuestionResponse> finalResponses = new ArrayList<>();
    private List<QuestionResponse> finalResponses2 = new ArrayList<>();
    private ListViewHolder holder = null;

    public SondageListAdapter(Context context, List<Question> questions, boolean resultat) {
        super(context, 0, questions);
        globalnbrselection=0;
        this.resultat=resultat;
        this.context = context;
        this.questions = questions;
        this.formResponses = getAllresponses(questions);
    }

    private List<QuestionResponse> getAllresponses(List<Question> questions) {
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (Question question : questions) {
            //question.setGloablereponse( globalnbrselection=0);
            List<Proposition> propositions = question.getPropositions();
            for (Proposition proposition : propositions) {
                questionResponses.add(new QuestionResponse(question.getId(), proposition.getId()));
            }
        }
        return questionResponses;
    }// end method

    @Override
    public int getCount() {
        return questions != null ? questions.size() : -1;
    }// end method

    @Override
    public Question getItem(int position) {
        return questions != null && questions.size() > 0 ? questions.get(position) : null;
    }// end method

    @Override
    public long getItemId(int position) {
        return position;
    }// end method

    /**
     * This function will fire every time we user scroll the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        question = getItem(position);
        View view = convertView;
        /*
        view = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_question, null);
        holder = new ListViewHolder();
        holder.txtQuestionDesc = (TextView) view.findViewById(R.id.question_description);
        holder.ratingBar = (RatingBar) view.findViewById(R.id.question_rating_bar);
        holder.listView = (ListView) view.findViewById(R.id.list);
        view.setTag(holder);
        */

        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(R.layout.list_item_question, null);
            holder = new ListViewHolder();
            holder.txtQuestionDesc = (TextView) view.findViewById(R.id.question_description);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.question_rating_bar);
            holder.listView = (ListView) view.findViewById(R.id.list);
            view.setTag(holder);
        } else {
            holder = (ListViewHolder) view.getTag();
        }

        if (question != null) {
            int num = position +1;
            holder.txtQuestionDesc.setText(num+" "+")"+" "+question.getName());

            if(resultat){
                if (isStarQuestion(question)) {
                    holder.listView.setVisibility(View.GONE);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.ratingBar.setId(question.getId());
                    List<Proposition> propositions = question.getPropositions();
                    for (Proposition proposition : propositions) {
                        // holder.ratingBar.setNumStars((int)proposition.getResultat());
                        holder.ratingBar.setRating((float)proposition.getResultat());
                    }

                } else {
                    final String [] propositions = getPropositions(question);
                    allReponses = getReponses(question);
                    Object tag = holder.listView.getTag();
                    Log.i("", "tag : " + tag);

                    if(question.getType()==1){
                           /* ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                    R.layout.item1,
                                    propositions); */
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                R.layout.item1,
                                propositions);
                        //holder.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        // holder.listView.setAdapter(adapter);

                        holder.listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,propositions) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                List<Proposition> propositions2 = question.getPropositions();
                                double max=0;
                                int a= 0;
                                int b=0;
                                for(Proposition proposition : propositions2){
                                    if(proposition.getResultat()>max){
                                        b=a;
                                        max=proposition.getResultat();
                                    }
                                    a++;
                                }
                                TextView textView = (TextView) super.getView(position, convertView, parent);
                                if(position==b) {
                                    textView.setTextColor(getContext().getResources().getColor(R.color.orange));
                                }
                                return  textView;
                            }
                        });
                        holder.listView.getChildAt(position);
                    }else{
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                propositions);
                        //holder.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        //holder.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        holder.listView.setAdapter(adapter2);
                    }

                    holder.listView.setTag(AVAILABLE_TAG);
                    holder.listView.setId(question.getId());
                    // holder.listView.setOnItemClickListener(this);
                    ViewGroup.LayoutParams params = holder.listView.getLayoutParams();

                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager windowManager = (WindowManager) context
                            .getSystemService(Context.WINDOW_SERVICE);
                    windowManager.getDefaultDisplay().getMetrics(metrics);
                    int density = metrics.densityDpi;
                    Double d = new Double((question.getPropositions().size())*(0.3)*density);
                    int valeur = d.intValue();
                    params.height = valeur;
                    holder.listView.setLayoutParams(params);
                    holder.listView.requestLayout();
                    holder.ratingBar.setVisibility(View.GONE);
                    /*
                    params.height = (question.getPropositions().size())*(density/(10/4));
                    holder.listView.setLayoutParams(params);
                    holder.listView.requestLayout();
                        */
                    //Toast.makeText(context, "DENSITY_MEDIUM... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
                        /*
                        if(density<270){
                            params.height = (question.getPropositions().size())*65;
                            holder.listView.setLayoutParams(params);
                            holder.listView.requestLayout();
                            // Toast.makeText(context, "DENSITY_HIGH... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
                        }else if(density<480){
                            params.height = (question.getPropositions().size())*98;
                            holder.listView.setLayoutParams(params);
                            holder.listView.requestLayout();
                            // Toast.makeText(context, "DENSITY_HIGH... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();
                        }else if(density <630 && density>480){
                            params.height = (question.getPropositions().size())*145;
                            holder.listView.setLayoutParams(params);
                            holder.listView.requestLayout();

                        }else{
                            params.height = (question.getPropositions().size())*195;
                            holder.listView.setLayoutParams(params);
                            holder.listView.requestLayout();
                        }
                        */

                }
            }else{//NOT RESULTAT
                if (isStarQuestion(question)) {
                    holder.listView.setVisibility(View.GONE);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.ratingBar.setId(question.getId());
                    holder.ratingBar.setOnRatingBarChangeListener(this);
                    // for highlight selected answer
                    List<Proposition> propositions = question.getPropositions();
                    for (Proposition proposition : propositions) {
                        // holder.ratingBar.setNumStars((int)proposition.getResultat());
                        holder.ratingBar.setRating((float)proposition.getResultat());
                    }
                } else {
                    String [] propositions = getPropositions(question);
                    allReponses = getReponses(question);
                    Object tag = holder.listView.getTag();
                    Log.i("", "tag : " + tag);

                    if(question.getType()==1){
                        holder.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        // check and put background color back for selected answer
                        holder.listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,propositions) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                List<Proposition> propositions2 = question.getPropositions();
                                int a=0;
                                int found = -1;
                                for(Proposition proposition : propositions2){
                                    if (found>-1) break;
                                    for(QuestionResponse response : finalResponses2){
                                        if(found>-1) break;
                                        if(response.getQuestionId()==question.getId() && response.getResponseId() == proposition.getId()){
                                            found = a;
                                            break;
                                        }// end if found
                                    }// end for response
                                    a++;
                                }// end for proposition
                                TextView textView = textView = (TextView) super.getView(position, convertView, parent);
                                if(position==found){
                                    textView.setBackgroundResource(R.drawable.qcm);
                                }
                                return textView;
                            }
                        });
                    }else{
                        holder.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        holder.listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,propositions) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                List<Proposition> propositions2 = question.getPropositions();
                                int a=0;
                                int[] found = new int[question.getPropositions().size()];
                                for(Proposition proposition : propositions2){
                                    for(QuestionResponse response : finalResponses){
                                        if(response.getQuestionId()==question.getId() && response.getResponseId() == proposition.getId()){
                                            found[a]=1;
                                        }// end if found
                                    }// end for response
                                    a++;
                                }// end for proposition
                                TextView textView = textView = (TextView) super.getView(position, convertView, parent);
                                // find the position that answer is selected to change background color
                                for(int index =0; index<found.length; index++){
                                    if(position==index && found[index]==1){
                                        textView.setBackgroundResource(R.drawable.qcm);
                                    }// end if position
                                }// end for index
                                return textView;
                            }// end function getView
                        });// end holer.listView.setAdapter
                    }// end else (multiple proposition)

                    holder.listView.setTag(AVAILABLE_TAG);
                    holder.listView.setId(question.getId());

                    holder.listView.setOnItemClickListener(this);
                    ViewGroup.LayoutParams params = holder.listView.getLayoutParams();

                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager windowManager = (WindowManager) context
                            .getSystemService(Context.WINDOW_SERVICE);
                    windowManager.getDefaultDisplay().getMetrics(metrics);
                    int density = metrics.densityDpi;
                    List<Proposition> listProposition = question.getPropositions();
                    Double i = 0.0;
                    for(Proposition proposition: listProposition){
                        i++;
                        if(proposition.getRank().length() > 80){
                            i+=0.25;
                        }
                    }
                    Double d = new Double(i*(0.3)*density);
                    int valeur = d.intValue();
                    params.height = valeur;
                    holder.listView.setLayoutParams(params);
                    holder.listView.requestLayout();

                    holder.ratingBar.setVisibility(View.GONE);
                    /*
                    params.height = (question.getPropositions().size())*(density/(10/4));
                    holder.listView.setLayoutParams(params);
                    holder.listView.requestLayout();
                        */
                    //Toast.makeText(context, "DENSITY_MEDIUM... Density is " + String.valueOf(density), Toast.LENGTH_LONG).show();


                }
            }
        }
        return view;
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        // get content height
        int contentHeight = holder.listView.getChildAt(0).getHeight();

        // set listview height
        ViewGroup.LayoutParams lp = holder.listView.getLayoutParams();
        lp.height = contentHeight;
        holder.listView.setLayoutParams(lp);
        holder.listView.requestLayout();
    }

    private void affecteritem(AdapterView<?> parent,int i){
        // parent.getChildAt(i).setOnClickListener(this);

    }

    private boolean isStarQuestion(Question question) {
        //TODO Check whether or not the question is a rating question?

        if(question.getType()==0){
            return true;
        }
        return false;
    }

    private String[] getPropositions(Question question) {
        String[] values = null;
        List<Proposition> propositions = question.getPropositions();
        if (propositions.size() > 0) {
            values = new String[propositions.size()];
            for (int i = 0; i < propositions.size(); i++) {
                values[i] = propositions.get(i).getRank();
            }
        }
        return values;
    }

    private int[] getReponses(Question question) {
        int[] values = null;
        List<Proposition> propositions = question.getPropositions();
        if (propositions.size() > 0) {
            values = new int[propositions.size()];
            for (int i = 0; i < propositions.size(); i++) {
                values[i] = propositions.get(i).getId();
            }
        }
        return values;
    }

    /**
     * @author Saly Sakey
     */
    public void setColorToSelectedQuestion(Question question, AdapterView<?> parent, int position){
        for(int i=0;i<question.getPropositions().size();i++){
            if(i==position){
                parent.getChildAt(i).setBackgroundResource(R.drawable.qcm);
                // parent.getChildAt(i).setBackgroundColor(getContext().getResources().getColor(R.color.vert));
            }else{
                parent.getChildAt(i).setBackgroundColor(getContext().getResources().getColor(R.color.white));
            }// end if i
        }// end for
    }// end function

    /**
     * This function is fire when user select the answer
     * The answer can be single(question.type ==1) answer or multiple answer (question.type==2)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int questionId = parent.getId();
        /* specificResponses is a list of answer for the question question*/
        List<QuestionResponse> specificResponses = getSpecificResponses(questionId);
        Question question = getQuestion(specificResponses.get(position).getQuestionId());
        globalnbrselection=question.getGloablereponse();

        /* if the question is only one proposition(answer) possible */
        if(question.getType()==1){
            finalResponses2.removeAll(specificResponses);
            setColorToSelectedQuestion(question, parent, position);
            finalResponses2.add(specificResponses.get(position));

        /* if the question is multiple answer possible */
        }else{
            if (finalResponses.contains(specificResponses.get(position))) {
                for(int i=0;i<question.getPropositions().size();i++){
                    parent.getChildAt(i).setActivated(true);
                }
                globalnbrselection--;
                question.setGloablereponse(globalnbrselection);
                parent.getChildAt(position).setBackgroundColor(getContext().getResources().getColor(R.color.white));
                finalResponses.remove(specificResponses.get(position));
            }else{
                if(question.getGloablereponse()<question.getType()) {

                    //ajouter question nombre de clique
                    globalnbrselection++;
                    question.setGloablereponse(globalnbrselection);

                    parent.getChildAt(position).setActivated(true);
                    parent.getChildAt(position).setBackgroundResource(R.drawable.qcm);
                    //parent.getChildAt(position).setBackgroundColor(getContext().getResources().getColor(R.color.vert));
                    finalResponses.add(specificResponses.get(position));

                    if(question.getGloablereponse()==question.getType()){
                        for(int i=0;i<question.getPropositions().size();i++){
                            if(!parent.getChildAt(i).isActivated()){
                                // parent.getChildAt(i).setEnabled(false);
                                parent.getChildAt(i).setActivated(false);
                                //    parent.getChildAt(i).setClickable(false);
                            }// end if
                        }// end for
                    }// end if question
                }// if
            }// end else
        } // end if question.getType
    }// end function

    private List<QuestionResponse> getSpecificResponses(int questionId) {
        List<QuestionResponse> specificResponses = new ArrayList<>();
        for (QuestionResponse questionResponse : formResponses) {
            if (questionResponse.getQuestionId() == questionId) {
                questionResponse.getResponseId();
                specificResponses.add(questionResponse);
            }
        }
        return specificResponses;
    }

    private QuestionResponse getSpecificResponsesQuestionReponse(int questionId) {
        QuestionResponse lala= null;
        List<QuestionResponse> specificResponses = new ArrayList<>();
        for (QuestionResponse questionResponse : formResponses) {
            if (questionResponse.getQuestionId() == questionId) {
                lala = questionResponse;
                specificResponses.add(questionResponse);
            }
        }
        return lala;
    }

    private Question getQuestion(long id) {
        for (Question question : questions) {
            if (id == question.getId()) {
                return question;
            }
        }
        return null;
    }


    public List<QuestionResponse> getReponses() {

        return finalResponses;
    }
    public List<QuestionResponse> getReponses2() {

        return finalResponses2;
    }



    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        int questionId = ratingBar.getId();
        int nombre = Math.round(rating);
        List<QuestionResponse> specificResponses = getSpecificResponses(questionId);
        QuestionResponse lala =getSpecificResponsesQuestionReponse(questionId);
        // RatingResponse ratingResponse = new RatingResponse(questionId,ratingBar.getId(), (int)rating);
        RatingResponseNUm banane =new RatingResponseNUm(questionId, lala.responseId, nombre);
        if(ratingResponses.contains(lala)){
            ratingResponses.removeAll(specificResponses);

            for(int i=0; i<ratingResponses2.size();i++){
                if(ratingResponses2.get(i).getId_propo()==banane.getId_propo()){
                    ratingResponses2.remove(ratingResponses2.get(i));
                }}
            ratingResponses.add(lala);
            ratingResponses2.add(banane);
        } else {
            ratingResponses2.removeAll(specificResponses);
            ratingResponses.removeAll(specificResponses);
            ratingResponses.add(lala);
            ratingResponses2.add(banane);

        }// end if
    }// end function


    public List<RatingResponseNUm> getRatingResponsesNum() {
        return ratingResponses2;
    }

    public static class ListViewHolder {
        public TextView txtQuestionDesc;
        public RatingBar ratingBar;
        public ListView listView;
    }

    public static class RatingResponseNUm {
        private int id_question;
        private long id_propo;
        private int ratingValue;

        public RatingResponseNUm(int id_question,long id_propo, int ratingValue) {
            this.id_question = id_question;
            this.id_propo = id_propo;
            this.ratingValue = ratingValue;
        }



        public int getId_question() {
            return id_question;
        }
        public long getId_propo() {
            return id_propo;
        }

        public int getRatingValue() {
            return ratingValue;
        }
    }

    public static class RatingResponse {
        private int id;

        private int ratingValue;

        public RatingResponse(int id, int ratingValue) {
            this.id = id;
            this.ratingValue = ratingValue;
        }

        public int getId() {
            return id;
        }

        public int getRatingValue() {
            return ratingValue;
        }
    }

    public static class ResultatsProposition {
        private int idpropo;
        private int positionpropo;

        public ResultatsProposition(int idpropo, int positionpropo) {
            this.idpropo = idpropo;
            this.positionpropo = positionpropo;
        }

        public int getIdPropo() {
            return idpropo;
        }

        public int getPositionpropo() {
            return positionpropo;
        }

    }

    public static class QuestionResponse {
        private long questionId;
        private long responseId;

        public QuestionResponse(long questionId, long responseId) {
            this.questionId = questionId;
            this.responseId = responseId;
        }

        public long getQuestionId() {
            return questionId;
        }

        public long getResponseId() {
            return responseId;
        }
    }

}
