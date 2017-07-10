package com.oumiao.monitor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.oumiao.monitor.MonitorApp;
import com.oumiao.monitor.R;
import com.oumiao.monitor.utils.StringUtils;
import com.oumiao.monitor.utils.TLog;


/**
 * 画虚线
 */
public class DashedLine extends View {
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private Rect mRect;
	private int width;
	private int lineColor;

	public DashedLine(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}

	public DashedLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		width = StringUtils.toInt(MonitorApp.getInstance().getDisplaySize()[0]);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DashedLine);
		lineColor = array.getColor(R.styleable.DashedLine_dashLineColor, Color.DKGRAY);
		array.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		TLog.log("line width:"+width);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);// 空心
		paint.setColor(lineColor);
		Path path = new Path();
		// 通过moveto，lineto的x，y坐标确定虚线实横，纵，还是倾斜
		path.moveTo(0, 0);// Set the beginning of the next contour to the point
							// (x,y)
		
		path.lineTo(width, 0);// Add a line from the last point to the specified
								// point (x,y).
		// DashPathEffect 可以使用DashPathEffect来创建一个虚线的轮廓(短横线/小圆点)，而不是使用实线
		// float[] { 5, 5, 5, 5 }值控制虚线间距，密度

		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}
}
