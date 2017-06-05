package com.kidc.bannertest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener{
    ViewPager viewPager;
    LinearLayout dotLayout;
    ArrayList<Integer> data;
    ArrayList<ImageView> dotList;
    private int pageCount = 0;
    private ScheduledThreadPoolExecutor ste;
    private Handler handler;
    PagerScrollSpeed scrollSpeed;

    public Banner(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.pager_item, this, true);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        setScrollSpeed();
        dotLayout = (LinearLayout) view.findViewById(R.id.dot_layout);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        viewPager.setCurrentItem(pageCount);
                        break;
                }

                return false;
            }
        });

    }

    public void setImageData(ArrayList<Integer> data){
        this.data = data;

        BannerPageAdapter adapter = new BannerPageAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        setDotLayout();

        startAutoPlay();
    }

    private void setDotLayout(){
        dotList = new ArrayList<>();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
        lp.leftMargin = 10;
        lp.rightMargin = 10;

        for (int d = 0; d < data.size(); d++){
            ImageView dotView = new ImageView(getContext());
            dotView.setImageResource(R.drawable.red_dot_night);
            dotView.setLayoutParams(lp);
            dotLayout.addView(dotView);
            dotList.add(dotView);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pageCount = position;

        for (int d = 0; d < data.size(); d++){
            if (getRealPosition(position) == d){
                dotList.get(d).setImageResource(R.drawable.red_dot);
            }else{
                dotList.get(d).setImageResource(R.drawable.red_dot_night);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE){
            int currentPos = viewPager.getCurrentItem();
            if (currentPos == 0){
                viewPager.setCurrentItem(data.size(), false);
            }else if (currentPos == data.size() + 1){
                viewPager.setCurrentItem(1, false);
            }
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                scrollSpeed.setScrollSpeed(100);
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private class BannerPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return data.size() + 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView iv = new ImageView(getContext());
            iv.setImageResource(data.get(getRealPosition(position)));
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "你点击了第" + getRealPosition(position) + "图",
                            Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(iv);

            return iv;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int currentPos = viewPager.getCurrentItem();
            if (currentPos == 0){
                viewPager.setCurrentItem(data.size(), false);
            }
        }
    }

    private void startAutoPlay(){
        ste = new ScheduledThreadPoolExecutor(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    pageCount++;
                    Message msg  = handler.obtainMessage();
                    msg.what = 1;
                    msg.sendToTarget();
                }catch (Exception e){
                    Log.i(TAG, "ScheduledThreadPoolExecutorError: " + e.getMessage());
                }

            }
        };

        ste.scheduleWithFixedDelay(runnable, 1, 2, TimeUnit.SECONDS);
    }

    //利用反射设置ViewPager滚动速度
    private void setScrollSpeed(){
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scrollSpeed = new PagerScrollSpeed(getContext(), new AccelerateInterpolator());
            field.set(viewPager, scrollSpeed);

        }catch (Exception e){
            Log.i(TAG, "setScrollSpeedError: " + e.getMessage());
        }
    }

    private void stopAutoPlay(){
        ste.shutdown();
    }

    private int getRealPosition(int pos){
        return pos % data.size();
    }



}
