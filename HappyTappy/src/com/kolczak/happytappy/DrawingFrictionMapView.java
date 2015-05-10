package com.kolczak.happytappy;

import java.util.ArrayList;

import nxr.tpad.lib.views.FrictionMapView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

class DrawingFrictionMapView extends FrictionMapView {
	private Path path;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;
	private IDrawingStageListener drawingStageListener;
	private int currentDrawingStage = 0;

	public DrawingFrictionMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
	
	public DrawingFrictionMapView(Context context) {
		super(context, null);
		init();
	}

	private void init() {
		this.setDrawingCacheEnabled(true);
		initPaint();
		path = new Path();
		mBitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(0xFFFFFF00);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(10);
	}		
	
	private ArrayList<PathWithPaint> _graphics1 = new ArrayList<PathWithPaint>();
	private Bitmap bitmap;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		PathWithPaint pp = new PathWithPaint();
		
		detectTouchedColor(event);
		
		mCanvas.drawPath(path, mPaint);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			path.moveTo(event.getX(), event.getY());
			path.lineTo(event.getX(), event.getY());
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			path.lineTo(event.getX(), event.getY());
			pp.setPath(path);
			pp.setmPaint(mPaint);
			_graphics1.add(pp);
		}
		invalidate();
		return true;
	}

	private void detectTouchedColor(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			this.buildDrawingCache();
			this.bitmap = this.getDrawingCache(true);
			int color = bitmap.getPixel((int)event.getX(), (int)event.getY());
			Log.d("KONRAD", "color: " + color);
			changeDrawingStage(color);
		}
	}

	private void changeDrawingStage(int color) {
		if (this.drawingStageListener == null)
			return;
		
		if (color == -16711936 && this.currentDrawingStage == 0)
		{
			this.currentDrawingStage = 1;
			this.drawingStageListener.onDrawingStageDone(1);
			Log.d("KONRAD", "First drawing stage done.");		
		}
		else if (color == -16721920 && this.currentDrawingStage == 1)
		{
			this.currentDrawingStage = 2;
			this.drawingStageListener.onDrawingStageDone(2);
			Log.d("KONRAD", "Second drawing stage done.");		
		}
		else if (color == -16721812 && this.currentDrawingStage == 2)
		{
			this.currentDrawingStage = 3;
			this.drawingStageListener.onDrawingStageDone(3);
			Log.d("KONRAD", "Third drawing stage done.");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Drawable d = getResources().getDrawable(R.drawable.dotted_dog3);
		d.setBounds(0, 0, getWidth(), getHeight());
		d.draw(canvas);
		
		if (_graphics1.size() > 0) {
			canvas.drawPath(
					_graphics1.get(_graphics1.size() - 1).getPath(),
					_graphics1.get(_graphics1.size() - 1).getmPaint());
		}
	}
	
	public IDrawingStageListener getDrawingStageListener() {
		return drawingStageListener;
	}

	public void setDrawingStageListener(IDrawingStageListener drawingStageListener) {
		this.drawingStageListener = drawingStageListener;
	}

	public class PathWithPaint {
		private Path path;

		public Path getPath() {
			return path;
		}

		public void setPath(Path path) {
			this.path = path;
		}

		private Paint mPaint;

		public Paint getmPaint() {
			return mPaint;
		}

		public void setmPaint(Paint mPaint) {
			this.mPaint = mPaint;
		}
	}
	
	public interface IDrawingStageListener
	{
		public void onDrawingStageDone(int stageId);
	}
}