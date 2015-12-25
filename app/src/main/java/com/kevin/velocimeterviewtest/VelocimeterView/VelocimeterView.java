package com.kevin.velocimeterviewtest.VelocimeterView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.kevin.velocimeterviewtest.R;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.bottom.BottomVelocimeterPainter;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.bottom.BottomVelocimeterPainterImp;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.digital.Digital;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.digital.DigitalImp;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.needle.NeedlePainter;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.needle.NeedlePainterImp;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.progress.ProgressVelocimeterPainter;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.progress.ProgressVelocimeterPainterImp;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.velocimeter.InternalVelocimeterPainter;
import com.kevin.velocimeterviewtest.VelocimeterView.painter.velocimeter.InternalVelocimeterPainterImp;
import com.kevin.velocimeterviewtest.VelocimeterView.utils.DimensionUtils;


/**
 * @author Adrián García Lomas
 */
public class VelocimeterView extends View {

    //progress的动画
    private ValueAnimator progressValueAnimator;
    //指针的动画
    private ValueAnimator nidleValueAnimator;
    //动画插补器
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();

    //所有用到的画笔
    private InternalVelocimeterPainter internalVelocimeterPainter;
    private ProgressVelocimeterPainter progressVelocimeterPainter;
    //private ProgressVelocimeterPainter blurProgressVelocimeterPainter; //progress重影暂且不要
    //private InsideVelocimeterPainterImp insideVelocimeterPainter; //内部仪表盘暂且不要
    //private InsideVelocimeterMarkerPainter insideVelocimeterMarkerPainter;
    private BottomVelocimeterPainter bottomVelocimeterPainter;
    private NeedlePainter mNeedlePainter;

    //仪表盘的数字
    private Digital digitalPainter;

    private int min = 0;
    private float progressLastValue = min;
    private float nidleLastValue = min;
    private int max = 100;
    private float value;
    private int duration = 500;
    private long progressDelay = 100;
    private int margin = 15; //view的margin

    //颜色图片定义
    private int insideProgressColor = Color.parseColor("#4B4B59");//progress未走到的颜色
    private int externalProgressColor = Color.parseColor("#FE443B");//progress走过的颜色
    private int progressBlurColor = Color.parseColor("#FE443B");//progress走过之后留下的残留光晕
    private int bottomVelocimeterColor = Color.parseColor("#FE443B");//底部下面装饰半圆弧的颜色
    private boolean showBottomVelocimeter = false; //是否显示下部分的多余圆环
    private int internalVelocimeterColor = Color.RED;//中心仪表跟外部progress内部表盘颜色
    private Bitmap mImage;//指针的图片
    private int needleBlurColor = Color.GREEN;//指针的光晕颜色
    private int digitalNumberColor = Color.WHITE;//指针的数字颜色
    private int digitalNumberBlurColor = Color.GREEN;//指针数字的光晕
    private String units = "point"; //时速表的转速单位

    public VelocimeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VelocimeterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        } else {
            size = width;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        internalVelocimeterPainter.onSizeChanged(h, w);
        progressVelocimeterPainter.onSizeChanged(h, w);
        //insideVelocimeterPainter.onSizeChanged(h, w);
        //insideVelocimeterMarkerPainter.onSizeChanged(h, w);
        //blurProgressVelocimeterPainter.onSizeChanged(h, w);
        digitalPainter.onSizeChanged(h, w);
        mNeedlePainter.onSizeChanged(h, w);
        bottomVelocimeterPainter.onSizeChanged(h, w);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.VelocimeterView);
        initAttributes(attributes);

        int marginPixels = DimensionUtils.getSizeInPixels(margin, getContext());
        setLayerType(LAYER_TYPE_SOFTWARE, null);//不使用硬件加速

        mNeedlePainter = new NeedlePainterImp(mImage, max, getContext());
        internalVelocimeterPainter = new InternalVelocimeterPainterImp(insideProgressColor, marginPixels, getContext());
        progressVelocimeterPainter = new ProgressVelocimeterPainterImp(externalProgressColor, max, marginPixels, getContext());
        //insideVelocimeterPainter = new InsideVelocimeterPainterImp(internalVelocimeterColor, getContext());
        //insideVelocimeterMarkerPainter = new InsideVelocimeterMarkerPainterImp(internalVelocimeterColor, getContext());
        //blurProgressVelocimeterPainter = new BlurProgressVelocimeterPainter(progressBlurColor, max, marginPixels, getContext());
        initValueAnimator();

        digitalPainter = new DigitalImp(digitalNumberColor, getContext(),
                DimensionUtils.getSizeInPixels(45, context), DimensionUtils.getSizeInPixels(40, context),
                units);
        bottomVelocimeterPainter = new BottomVelocimeterPainterImp(bottomVelocimeterColor, marginPixels, getContext());
    }

    private void initAttributes(TypedArray attributes) {
        insideProgressColor = attributes.getColor(R.styleable.VelocimeterView_inside_progress_color, insideProgressColor);
        externalProgressColor = attributes.getColor(R.styleable.VelocimeterView_external_progress_color,
                externalProgressColor);
        progressBlurColor = attributes.getColor(R.styleable.VelocimeterView_progress_blur_color, progressBlurColor);
        bottomVelocimeterColor = attributes.getColor(R.styleable.VelocimeterView_bottom_velocimeter_color,
                bottomVelocimeterColor);
        showBottomVelocimeter = attributes.getBoolean(R.styleable.VelocimeterView_show_bottom_bar,
                showBottomVelocimeter);
        internalVelocimeterColor = attributes.getColor(R.styleable.VelocimeterView_internal_velocimeter_color,
                internalVelocimeterColor);
        mImage = BitmapFactory.decodeResource(getResources(), attributes.getResourceId(R.styleable.VelocimeterView_needle_image, 0));
        needleBlurColor = attributes.getColor(R.styleable.VelocimeterView_needle_blur_color, needleBlurColor);
        digitalNumberColor = attributes.getColor(R.styleable.VelocimeterView_digital_number_color, digitalNumberColor);
        digitalNumberBlurColor = attributes.getColor(R.styleable.VelocimeterView_digital_number_blur_color,
                digitalNumberBlurColor);
        max = attributes.getInt(R.styleable.VelocimeterView_max, max);
        units = attributes.getString(R.styleable.VelocimeterView_units);
        if (units == null) {
            units = "point";
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //blurProgressVelocimeterPainter.draw(canvas);
        internalVelocimeterPainter.draw(canvas);
        progressVelocimeterPainter.draw(canvas);
        //insideVelocimeterPainter.draw(canvas);
        //insideVelocimeterMarkerPainter.draw(canvas);
        mNeedlePainter.draw(canvas);
        if (showBottomVelocimeter) {
            bottomVelocimeterPainter.draw(canvas);
        }
        digitalPainter.draw(canvas);
        invalidate();
    }

    public void setValue(float value) {
        this.value = value;
        if (value <= max && value >= min) {
            animateProgressValue();
        }
    }

    public void setValue(float value, boolean animate) {
        this.value = value;
        if (value <= max && value >= min) {
            if (!animate) {
                updateValueProgress(value);
                updateValueNeedle(value);
            } else {
                animateProgressValue();
            }
        }
    }

    private void initValueAnimator() {
        progressValueAnimator = new ValueAnimator();
        progressValueAnimator.setInterpolator(interpolator);
        progressValueAnimator.addUpdateListener(new ProgressAnimatorListenerImp());
        nidleValueAnimator = new ValueAnimator();
        nidleValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        nidleValueAnimator.addUpdateListener(new NeedleAnimatorListenerImp());
    }

    private void animateProgressValue() {
        if (progressValueAnimator != null) {
            progressValueAnimator.setFloatValues(progressLastValue, value);
            progressValueAnimator.setDuration(duration + progressDelay);
            progressValueAnimator.start();
            nidleValueAnimator.setFloatValues(nidleLastValue, value);
            nidleValueAnimator.setDuration(duration);
            nidleValueAnimator.start();
        }
    }

    public void setProgress(Interpolator interpolator) {
        this.interpolator = interpolator;

        if (progressValueAnimator != null) {
            progressValueAnimator.setInterpolator(interpolator);
        }
    }

    public float getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private void updateValueProgress(float value) {
        progressVelocimeterPainter.setValue(value);
        //blurProgressVelocimeterPainter.setValue(value);
    }

    private void updateValueNeedle(float value) {
        mNeedlePainter.setValue(value);
        digitalPainter.setValue(value);
    }

    private class ProgressAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();
            updateValueProgress(value);
            progressLastValue = value;
        }
    }

    private class NeedleAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();
            updateValueNeedle(value);
            nidleLastValue = value;
        }
    }
}
