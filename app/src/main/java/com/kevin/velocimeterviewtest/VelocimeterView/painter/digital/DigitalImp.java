package com.kevin.velocimeterviewtest.VelocimeterView.painter.digital;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.kevin.velocimeterviewtest.VelocimeterView.utils.DimensionUtils;

/**
 * @author Adrián García Lomas
 */
public class DigitalImp implements Digital {

    private float value;
    private Typeface typeface;
    protected Paint digitPaint1, digitPaint2;//分别小时小数点前和后的
    protected Paint textPaint, textPoint;
    private Context context;
    private float textSize;
    private int marginTop;
    private int color;
    private float centerX;
    private float centerY;
    private float correction;
    private String units;

    public DigitalImp(int color, Context context, int marginTop, int textSize, String units) {
        this.context = context;
        this.color = color;
        this.marginTop = marginTop;
        this.textSize = textSize;
        this.units = units;
        initTypeFace();
        initPainter();
        initValues();
    }

    private void initPainter() {
        digitPaint1 = new Paint();
        digitPaint1.setAntiAlias(true);
        digitPaint1.setTextSize(textSize);
        digitPaint1.setColor(color);
        //digitPaint.setTypeface(typeface);
        digitPaint1.setTextAlign(Paint.Align.CENTER);
        digitPaint2 = new Paint();
        digitPaint2.setAntiAlias(true);
        digitPaint2.setTextSize(textSize / 2 + 10);
        digitPaint2.setColor(color);
        //digitPaint.setTypeface(typeface);
        digitPaint1.setTextAlign(Paint.Align.CENTER);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize / 6);
        textPaint.setColor(color);
        //textPaint.setTypeface(typeface);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initValues() {
        correction = DimensionUtils.getSizeInPixels(10, context);
    }

    private void initTypeFace() {
        //typeface = Typeface.createFromAsset(context.getAssets(), "fonts/digit.TTF");
    }

    @Override
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("" + (int) (value / 10), centerX - correction * 2, (centerY) + marginTop / 3,
                digitPaint1);
        canvas.drawText(" . ", centerX + textSize - correction * 4, (centerY) + marginTop / 3,
                digitPaint1);
        canvas.drawText("" + (int) (value % 10), centerX + textSize - correction * 3 - correction / 2, (centerY) + marginTop / 3,
                digitPaint2);
        canvas.drawText(units, centerX + textSize * 1.0f - correction, (centerY) + marginTop / 3,
                textPaint);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.centerX = width / 2;
        this.centerY = height / 2;
    }
}
