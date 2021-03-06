package com.guotai.mall.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.guotai.mall.R;
import com.guotai.mall.model.HotspotInfoList;
import com.guotai.mall.model.News;
import com.guotai.mall.widget.HomeHeadImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ez on 2017/6/20.
 */

public class HomePagerAdapter extends PagerAdapter {

    List<HotspotInfoList> list;
    Context context;
    HomeClickListener homeClickListener;

    public HomePagerAdapter(Context context, List<HotspotInfoList> list){
        this.context = context;
        this.list = list;
    }

    public void update(List<HotspotInfoList> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setHomeClickListener(HomeClickListener homeClickListener){
        this.homeClickListener = homeClickListener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list==null?0:Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(list==null || list.size()==0){
            return null;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = width/2+100;
        position = position%list.size();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_item, null);
        HomeHeadImage product_iv = (HomeHeadImage) view.findViewById(R.id.product_iv);
        Picasso.with(context).load(list.get(position%list.size()).ImagePath).resize(width, height).centerCrop().into(product_iv);
        container.addView(view);
        final int finalPosition = position;
        product_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeClickListener!=null){
                    homeClickListener.OnClick(list.get(finalPosition %list.size()));
                }
            }
        });
        return view;
    }

}
