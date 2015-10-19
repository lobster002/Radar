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
 * ��ͼ������ʾ����
 * 1.������ȷ����ͼ�Ŀ�͸�-->onMeasure()
 * 2.���֣�-->onLayout()
 * 3.���ƣ�-->onDraw()
 * */

public class RadarView extends View implements Runnable {
	private int w;// ȫ���Ŀ�
	private int h;// ȫ���ĸ�

	private Paint mPaint = null;
	private Paint ScanPaint = null;

	private Matrix mMatrix = null;

	private int Value;

	private static Handler mHandler = new Handler() {
	};

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.bg);// ���ñ���
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#A1A1A1"));
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);// ���ÿ����
		mPaint.setStyle(Style.STROKE);

		ScanPaint = new Paint();
		ScanPaint.setColor(Color.WHITE);
		ScanPaint.setAntiAlias(true);

		mHandler.post(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// ���ƽ���,��4��ͬ��Բ
		int radius = w / 2;
		int halfHeight = h / 2;
		canvas.drawCircle(radius, halfHeight, radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 3 * radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 5 * radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 7 * radius / 10, mPaint);
		canvas.drawCircle(radius, halfHeight, 9 * radius / 10, mPaint);

		// ʮ����
		canvas.drawLine(radius, halfHeight, radius, halfHeight - 9 * radius
				/ 10, mPaint);
		canvas.drawLine(radius, halfHeight, radius, halfHeight + 9 * radius
				/ 10, mPaint);
		canvas.drawLine(radius, halfHeight, radius - 9 * radius / 10,
				halfHeight, mPaint);
		canvas.drawLine(radius, halfHeight, radius + 9 * radius / 10,
				halfHeight, mPaint);

		// ɨ��Ч��
		Shader shader = new SweepGradient(radius, halfHeight,
				Color.TRANSPARENT, Color.parseColor("#AAAAAAAA"));// ȡɫ�������ƻ���Ч������͸������͸��
		ScanPaint.setShader(shader);
		canvas.concat(mMatrix);
		canvas.drawCircle(radius, halfHeight, 9 * radius / 10, ScanPaint);

		super.onDraw(canvas);
	}

	// ����ȫ������Ŀ�͸�
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

		RadarView.this.invalidate();// ���»��ƽ���
		mHandler.postDelayed(this, 10);
	}

}
