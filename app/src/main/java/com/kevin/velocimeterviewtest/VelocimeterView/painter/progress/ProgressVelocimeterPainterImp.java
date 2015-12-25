package com.kevin.velocimeterviewtest.VelocimeterView.painter.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;

import com.kevin.velocimeterviewtest.VelocimeterView.utils.DimensionUtils;

/**
 * @author Adrián García Lomas
 */
public class ProgressVelocimeterPainterImp implements ProgressVelocimeterPainter {

	private RectF	circle;
	protected Paint	paint;
	private int		color;
	private int		startAngle	= 140;
	private int		width;
	private int		height;
	private float	plusAngle	= 0;
	private float	max;
	private int		strokeWidth;
	private int		blurMargin;
	private int		lineWidth;
	private int		lineSpace;
	private Context	context;
	private float	maxAngle	= 260f;	// 这里就控制了圆环所能转最大距离

	public ProgressVelocimeterPainterImp(int color, float max, int margin, Context context) {
		this.color = color;
		this.max = max;
		this.blurMargin = margin;
		this.context = context;
		initSize();
		init();
	}

	private void initSize() {
		this.lineWidth = DimensionUtils.getSizeInPixels(4, context);
		this.lineSpace = DimensionUtils.getSizeInPixels(12, context);
		this.strokeWidth = DimensionUtils.getSizeInPixels(18, context);
	}

	private void init() {
		initPainter();
	}

	private void initPainter() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		Path path = new Path();
		path.addRoundRect(new RectF(0, 0, lineWidth, strokeWidth), 5, 5, Path.Direction.CCW);
		paint.setPathEffect(new PathDashPathEffect(path, lineSpace, 0, PathDashPathEffect.Style.ROTATE));
	}

	private void initExternalCircle() {
		int padding = strokeWidth / 2 + blurMargin / 2;
		circle = new RectF();
		circle.set(padding, padding, width - padding, height - padding);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawArc(circle, startAngle, plusAngle, false, paint);
	}

	@Override
	public void setColor(int color) {
		this.color = color;
		paint.setColor(color);
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public void onSizeChanged(int height, int width) {
		this.width = width;
		this.height = height;
		initExternalCircle();
	}

	public void setValue(float value) {
		this.plusAngle = (maxAngle * value) / max;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}
}
