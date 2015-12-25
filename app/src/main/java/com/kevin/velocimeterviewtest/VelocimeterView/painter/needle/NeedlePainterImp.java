package com.kevin.velocimeterviewtest.VelocimeterView.painter.needle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.kevin.velocimeterviewtest.VelocimeterView.utils.DimensionUtils;

/**
 * 指针用一个图片
 */
public class NeedlePainterImp implements NeedlePainter {
	protected Paint	mImagePaint;
	protected Paint	mStringPaint;
	private Context	context;
	private Bitmap	mBitmap;
	private Rect	mRect;				// 绘制指针区域
	private int		color;				// 暂时没用上
	private int		width;
	private int		height;
	private int		centerX;
	private int		centerY;
	private int		margin;
	private int		startAngle	= 160;
	private int		radius;
	private float	plusAngle	= 0;
	private float	max;
	private int		strokeWidth;
	private float	maxAngle	= 260f;	// 这里就控制了圆环对应的指针所能转最大距离

	public NeedlePainterImp(Bitmap bitmap, float max, Context context) {
		this.mBitmap = bitmap;
		this.max = max;
		this.context = context;
		init();
	}

	public NeedlePainterImp(Bitmap bitmap, float max, Context context, int startAngle, int maxAngle) {
		this.mBitmap = bitmap;
		this.max = max;
		this.context = context;
		this.startAngle = startAngle;
		this.maxAngle = maxAngle;
		init();
	}

	private void init() {
		initSize();
		initPainter();

	}

	private void initSize() {
		radius = DimensionUtils.getSizeInPixels(40, context);// 半径
		margin = DimensionUtils.getSizeInPixels(15, context);
		strokeWidth = DimensionUtils.getSizeInPixels(2, context);
	}

	private void initPainter() {
		mImagePaint = new Paint();
		mImagePaint.setStrokeWidth(strokeWidth);
		mStringPaint = new Paint();
		mStringPaint.setARGB(255, 255, 255, 255);// 白色
		mRect = new Rect();
	}

	@Override
	public void draw(Canvas canvas) {
		int temp = (int) (width * 1.0f / 4);
		mRect.left = (int) (centerX - temp);
		mRect.top = (int) (centerY - temp);
		mRect.right = (int) (centerX + temp);
		mRect.bottom = (int) (centerY + temp);
		canvas.save();
		canvas.rotate(plusAngle, centerX, centerY);
		canvas.drawBitmap(mBitmap, null, mRect, mImagePaint);
		canvas.restore();
	}

	@Override
	public void onSizeChanged(int height, int width) {
		this.height = height;
		this.width = width;
		centerX = width / 2;
		centerY = height / 2;// 获得中心位置
	}

	// TODO remove magic string
	public void setValue(float value) {
		this.plusAngle = (maxAngle * value) / max;
	}

	public void setColor(int color) {

	}

	public int getColor() {
		return color;
	}
}
