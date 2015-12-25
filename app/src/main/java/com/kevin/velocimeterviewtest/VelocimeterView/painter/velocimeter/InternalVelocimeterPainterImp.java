package com.kevin.velocimeterviewtest.VelocimeterView.painter.velocimeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;

import com.kevin.velocimeterviewtest.VelocimeterView.utils.DimensionUtils;

/**
 * @author Adrián García Lomas
 */
public class InternalVelocimeterPainterImp implements InternalVelocimeterPainter {

	private Paint	paint;
	private RectF	circle;
	private Context	context;
	private int		width;
	private int		height;
	private float	startAngle	= 140;
	private float	finishAngle	= 260;
	private int		strokeWidth;
	private int		blurMargin;
	private int		lineWidth;
	private int		lineSpace;
	private int		color;

	public InternalVelocimeterPainterImp(int color, int margin, Context context) {
		this.blurMargin = margin;
		this.context = context;
		this.color = color;
		initSize();
		initPainter();
	}

	private void initSize() {
		this.lineWidth = DimensionUtils.getSizeInPixels(4, context);
		this.lineSpace = DimensionUtils.getSizeInPixels(12, context);
		this.strokeWidth = DimensionUtils.getSizeInPixels(18, context);
	}

	private void initPainter() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		Path path = new Path();
		path.addRoundRect(new RectF(0, 0, lineWidth, strokeWidth), 5, 5, Path.Direction.CCW);
		paint.setPathEffect(new PathDashPathEffect(path, lineSpace, 0, PathDashPathEffect.Style.ROTATE));

	}

	private void initCircle() {
		int padding = strokeWidth / 2 + blurMargin / 2;
		circle = new RectF();
		circle.set(padding, padding, width - padding, height - padding);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawArc(circle, startAngle, finishAngle, false, paint);
	}

	@Override
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public int getColor() {
		return 0;
	}

	@Override
	public void onSizeChanged(int height, int width) {
		this.width = width;
		this.height = height;
		initCircle();
	}
}
