package se.kjellstrand.robot.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RobotRoomView extends View {

    private String TAG = RobotRoomView.class.getCanonicalName();

    private PathEffect roomPathEffect = new DashPathEffect(new float[] {
            10, 5, 5, 5
    }, 3);

    private Paint mPaint = new Paint();


    private Bitmap pathBitmap;

    private Canvas tempCanvas;

    private Path mWalls;

    private Matrix mMatrix;

    public void defineViewPort(int minX, int minY, int maxX, int maxY) {
        float roomWidth = maxX-minX;
        int roomHeight = maxY-minY;
        
        float xScale = roomWidth/getWidth();
        float yScale = roomHeight/getHeight();
        
        Log.d(TAG, "scales: "+xScale+" "+yScale);
        
        float scale = 10f;

        mMatrix = new Matrix();
        
        // Move the walls onto 1, 1
        mMatrix.setTranslate(2, 2);
        mMatrix.postScale(scale, scale);

    }

    public void setPath(Path walls) {
        if (mMatrix != null) {
            walls.transform(mMatrix);
        } else {
            Log.w(TAG, "No matrix set for scaling and translating!!!");
        }
        this.mWalls = walls;
    }

    public RobotRoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotRoomView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setStrokeJoin(Paint.Join.ROUND);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        // routePaint.setStyle(Style.STROKE);
        // routePaint.setXfermode(null);

    }

    @Override
    public void draw(android.graphics.Canvas canvas) {
 
        // Render routes to the temporary bitmap
        if (mWalls != null && !mWalls.isEmpty()) {
            canvas.drawPath(mWalls, mPaint); // render outer line
        }

    }

}
