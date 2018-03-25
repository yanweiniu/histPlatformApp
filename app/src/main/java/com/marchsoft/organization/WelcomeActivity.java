package com.marchsoft.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    public Context context;
    public static int screenW,screenH;
    private static final int VIEW_NO_1 = 0;
    private static final int VIEW_NO_2 = 1;
    private static final int VIEW_NO_3 = 2;
    ImageView mOnePointer;
    private int preIndex = 0;
    private ViewPager mPager;
    private MyViewPagerAdapter mPagerAdapter;
    List<View> list = new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
        LayoutInflater inflater = LayoutInflater.from(this);
        View view0 = inflater.inflate(R.layout.guide_main_1, null, false);
        View view1 = inflater.inflate(R.layout.guide_main_2,null,false);
        View view2 = inflater.inflate(R.layout.guide_main_3,null,false);
        list.add(view0);
        list.add(view1);
        list.add(view2);
        mPager = (ViewPager) findViewById(R.id.container);
        mPagerAdapter = new MyViewPagerAdapter(list);
        mPager.setAdapter(mPagerAdapter);
        animal(VIEW_NO_1);
        mPager.setOnPageChangeListener(this);
    }

    public class MyViewPagerAdapter extends PagerAdapter{
        private List<View> mListViews;
        public MyViewPagerAdapter(List<View> mListViews){
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = mListViews.get(position);
            container.removeView(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mListViews.get(position);
            container.addView(view,0);
            switch (position){
                case VIEW_NO_3:
                    view.setOnTouchListener(mOnTouchListener);
                    break;
            }

            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        animal(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void animal(int position){
        preIndex = position;
    }
    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (preIndex == 2){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = (int) event.getX();
                        if ((x1 - x2) > 0) {
                            startApp();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = (int) event.getX();
                        if ((x1 - x2) >0) {
                            startApp();
                        }
                        break;
                }
            }
            return true;
        }
    };
    int x1 = 0, x2 = 0;

    private void startApp(){
        Intent intent = new Intent(context,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
