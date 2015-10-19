package com.sky.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/*
 * 视图界面显示过程
 * 1.测量：确定视图的宽和高-->onMeasure()
 * 2.布局：-->onLayout()
 * 3.绘制：-->onDraw()
 * */

public class RadarView extends View implements Runnable {
	private int w;// 全屏的宽
	private int h;// 全屏的高

	private Paint mPaint = null;
	private Paint ScanPaint = null;

	private Matrix mMatrix = null;

	private int Value;

	private static Handler mHandler = new Handler() {
	};

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.bg);// 设置背景
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#A1A1A1"));
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);// 设置抗锯齿
		mPaint.setStyle(Style.STROKE);

		ScanPaint = new Paint();
		ScanPaint.setColor(Color.WHITE);
		ScanPaint.setAntiAlias(true);

		mHandler.post(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// 绘制界面,画4个同心圆
		int radius = w / 2;
		int halfHeight = h / 2;
		canvas.drawCircle(radius, halfHeight, radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 3 * radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 5 * radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 7 * radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 9 * radius / 10, mPaint);

		// 十字线
		canvas.drawLine(radius, halfHeight, radius, halfHeight - 9 * radius
				/ 10, mPaint);
		canvas.drawLine(radius, halfHeight, radius, halfHeight + 9 * radius
				/ 10, mPaint);
		canvas.drawLine(radius, halfHeight, radius - 9 * radius / 10,
				halfHeight, mPaint);
		canvas.drawLine(radius, halfHeight, radius + 9 * radius / 10,
				halfHeight, mPaint);

		// 扫描效果
		Shader shader = new SweepGradient(radius, halfHeight,
				Color.TRANSPARENT, Color.parseColor("#AAAAAAAA"));// 取色器，控制画笔效果，从透明到半透明
		ScanPaint.setShader(shader);
		canvas.concat(mMatrix);
		canvas.drawCircle(radius, halfHeight, 9 * radius / 10, ScanPaint);

		super.onDraw(canvas);
	}

	// 设置全屏界面的宽和高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.w = getWidth();
		this.h = getHeight();
	}

	@Override
	public void run() {
		Value += 2;
		Value %= 360;
		mMatrix = new Matrix();
		mMatrix.setRotate(Value, w / 2, h / 2);

		RadarView.this.invalidate();// 重新绘制界面
		mHandler.postDelayed(this, 10);
	}

}
