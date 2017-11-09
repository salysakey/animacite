package org.anima.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

//import com.zimberland.jtm.fragments.TutorialFragment;
//import com.zimberland.jtm.helper.FragmentHelper;

import org.anima.fragments.TutorialFragment;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

        if (getActionBar() != null)
            getActionBar().hide();
        try {
           addFragment(this,
                   android.R.id.content,
                   false,
                   TutorialFragment.class,
                   TutorialFragment.TAG,
                   null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Fragment> T addFragment(FragmentActivity activity
            , final int rootViewId
            , boolean addToBackStack
            , Class<T> aFragmentSubClass
            , String fragmentTag
            , Bundle arguments)
            throws IllegalAccessException, InstantiationException {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        T attachedFragment = (T) fragmentManager.findFragmentByTag(fragmentTag);

        if (attachedFragment == null) {
            attachedFragment = aFragmentSubClass.newInstance();
            attachedFragment.setArguments(arguments);
        }
        if (!attachedFragment.isAdded()) {
            fragmentTransaction.add(rootViewId, attachedFragment, fragmentTag);
            if(addToBackStack){
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
        return attachedFragment;
    }

}
