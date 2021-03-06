package com.guotai.mall.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.guotai.mall.R;
import com.guotai.mall.uitl.Common;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by zhangpan on 17/11/9.
 */

public class MultyPicView extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap = 5;
    private int columns;//
    private int rows;//
    private List<String> listData;
    private int totalWidth;
    OnClick onClick;
    int oldViewCount;

    public MultyPicView(Context context) {
        super(context);
    }

    public MultyPicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        totalWidth= getResources().getDisplayMetrics().widthPixels- Common.dip2px(context, 10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnClick(OnClick onClick){
        this.onClick = onClick;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    private void layoutChildrenView(){
        int childrenCount = listData.size();

        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);
            if(!listData.get(i).equals("default")){
                Picasso.with(getContext()).load(new File(listData.get(i))).resize(400, 400).centerCrop().into(childrenView);
            }
            else{
                Picasso.with(getContext()).load(R.mipmap.photo_add).resize(400, 400).centerInside().into(childrenView);
            }
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }

    }


    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<String> lists, OnClick onClick1) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        if(onClick1!=null){
            onClick = onClick1;
        }
        //初始化布局
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                ImageView iv = generateImageView();
                iv.setTag(i);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onClick!=null){
                            onClick.onClick(v);
                        }
                    }
                });
                iv.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(onClick!=null){
                            onClick.onLongClick(v);
                        }
                        return false;
                    }
                });
                addView(iv,generateDefaultLayoutParams());
                i++;
            }
        } else {
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
                for(int i=0; i<newViewCount; i++){
                    getChildAt(i).setTag(i);
                }
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    ImageView iv = generateImageView();
                    iv.setTag(i+oldViewCount);
                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onClick!=null){
                                onClick.onClick(v);
                            }
                        }
                    });
                    iv.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(onClick!=null){
                                onClick.onLongClick(v);
                            }
                            return false;
                        }
                    });
                    addView(iv,generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;
        oldViewCount = listData.size();
        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num        row        column
     * 1           1        1
     * 2           1        2
     * 3           1        3
     * 4           2        2
     * 5           2        3
     * 6           2        3
     * 7           3        3
     * 8           3        3
     * 9           3        3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
//            if (length == 4) {
//                columns = 2;
//            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private ImageView generateImageView() {
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    public interface OnClick{
        void onClick(View v);
        void onLongClick(View v);
    }

}