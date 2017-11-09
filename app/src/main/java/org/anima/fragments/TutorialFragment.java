package org.anima.fragments;
import org.anima.animacite.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;
//import com.zimberland.jtm.R;
//import com.zimberland.jtm.helper.IntentHelper;
//import com.zimberland.jtm.preferences.PrefManager;

import org.anima.activities.ImagePickActivity;

public class TutorialFragment extends Fragment {

    static final public String TAG = "TutorialFragment";

    private ImageView close;

    private final int[] resources = {
            R.layout.tuto_screen_1,
            R.layout.tuto_screen_2,
            R.layout.tuto_screen_3,
            R.layout.tuto_screen_4
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_tutorial, container, false);

        close = (ImageView)view.findViewById(R.id.tuto_close);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        CirclePageIndicator mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        mViewPager.setAdapter(new TutorialAdapter(getActivity().getApplicationContext(), resources));
        mIndicator.setViewPager(mViewPager);

        mIndicator.setOnPageChangeListener(new TutorialOnPageChangeListener());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTutorial();
            }
        });
    }

    private class TutorialOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    private void closeTutorial() {
        //PrefManager.setTutorialDone(getActivity());
        Intent intent = new Intent(getActivity(),ImagePickActivity.class);
        this.startActivity(intent);
        getActivity().finish();
    }


    public class TutorialAdapter extends PagerAdapter {

        private Context context;
        private int[] resources;

        public TutorialAdapter(Context context, int... resources) {
            this.context = context;
            this.resources = resources;
        }

        @Override
        public int getCount() {
            return resources.length;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(resources[position], null, false);
            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}