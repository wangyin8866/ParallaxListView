package com.wy.parallaxlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by wy on 2016/8/22.
 */
public class ParallaxLIstView extends ListView {
    private ImageView mImageView;
    private int mImageViewHeight = -1;
    View header;
    public ParallaxLIstView(Context context) {
        this(context, null);
    }

    public ParallaxLIstView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxLIstView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());

    }

    public void setParallaxImageView(ImageView view) {
        mImageView = view;
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        header = (View) mImageView.getParent();
    }

    /**
     * 实现下拉的时候可以放大图片
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //监听ListView下拉过度
        /**
         * deltaY：是在超出滑动的时候每一毫秒滑动的距离
         * -：往下拉；+：往上拉
         */
        Log.e("tag:  ", deltaY + "");
        if (deltaY < 0) {//下拉过度
            //不断地控制头部header放大----不断修改imageview的高度
            mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
            mImageView.requestLayout();
        } else {//上拉过度的情况下，然后往上推
            if (mImageView.getHeight() > mImageViewHeight) {
                mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
                mImageView.requestLayout();
            }

        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * 实现往上推的时候可以缩小图片
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //监听listview的滑动
        if (header.getTop()<0&&mImageView.getHeight() > mImageViewHeight) {
            // header.getTop();//头部滑出去的距离 <0;
            mImageView.getLayoutParams().height = mImageView.getHeight() + header.getTop();
            header.layout(header.getLeft(), 0, header.getRight(), header.getBottom());
            mImageView.requestLayout();
        }
    }

    /**
     * 实现松手回到原来大小
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //松手，然后动画的减小高度
            //自定义动画
            BackAnimation animation = new BackAnimation(mImageView, mImageViewHeight);
            animation.setDuration(300);
            mImageView.startAnimation(animation);

        }
        return super.onTouchEvent(ev);
    }

    public class BackAnimation extends Animation {
        private ImageView mImageView;
        private int targetHeight;
        private int originalHeight;
        private int extraHeight;
        public BackAnimation(ImageView imageView,int targetHeight) {
            this.mImageView = imageView;
            this.targetHeight = targetHeight;//到那个高度
            this.originalHeight=mImageView.getHeight();//当前高度
            this.extraHeight = originalHeight - targetHeight;//高度差
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            /**
             * interpolatedTime:时间变化比例（范围：0.0~1.0）
             * 当前的高度---->原始的高度
             * 0ms----------->300ms
             */
            //不断减小Imageview的高度

            mImageView.getLayoutParams().height = (int) (originalHeight - extraHeight*interpolatedTime);
            mImageView.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }

}
