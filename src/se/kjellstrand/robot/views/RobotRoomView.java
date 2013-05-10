package se.kjellstrand.robot.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * View used for displaying the path that a robot moved through a room, and the
 * walls of the room.
 * 
 */
public class RobotRoomView extends View {

    /**
     * Tag used to enable easy filtering in logcat.
     */
    private String TAG = RobotRoomView.class.getCanonicalName();

    /**
     * Paint used for the room walls.
     */
    private Paint mRoomWallPaint = new Paint();

    /**
     * Paint used for the room floor.
     */
    private Paint mRoomFloorPaint = new Paint();

    /**
     * Paint used for the robot trail/path.
     */
    private Paint mRobotPathPaint = new Paint();

    /**
     * Width of the path to paint where the robot moved.
     */
    private int mRobotPathStrokeWidth;

    /**
     * Width of the walls.
     */
    private int mRoomWallsStrokeWidth;

    /**
     * A path containing the points that makes up the walls of the room.
     */
    private Path mWalls;

    /**
     * A path containing the points that the robot moved over while running its
     * program.
     */
    private Path mRobotPath;

    /**
     * Matrix used to scale/translate the paths to a size and position suitable
     * for displaying on screen.
     */
    private Matrix mMatrix;

    /**
     * Simple constructor to use when creating a view from code.
     * 
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public RobotRoomView(Context context) {
        super(context);
    }

    /**
     * Simple constructor to use when creating a view from code.
     * 
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public RobotRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Simple constructor to use when creating a view from code.
     * 
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view. If 0, no style
     *        will be applied (beyond what is included in the theme). This may
     *        either be an attribute resource, whose value will be retrieved
     *        from the current theme, or an explicit style resource.
     */
    public RobotRoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Defines the area/bounding box that should be drawn, in this view. The
     * walls of the room should typically be within this area, as should the
     * robots path. Some padding can be added to avoid drawing on the edge.
     * 
     * @param minX min x to draw.
     * @param minY min y to draw.
     * @param maxX max x to draw.
     * @param maxY max y to draw.
     * @param roomPadding padding to use around the rooms walls to give some
     *        space to the view.
     */
    public void defineViewPort(int minX, int minY, int maxX, int maxY, float roomPadding) {
        float roomWidth = (roomPadding * 2) + maxX - minX;
        float roomHeight = (roomPadding * 2) + maxY - minY;

        float xScale = roomWidth / getWidth();
        float yScale = roomHeight / getHeight();

        float scale = 1 / Math.max(xScale, yScale);

        mRobotPathStrokeWidth = (int) (scale * 0.5);
        mRoomWallsStrokeWidth = (int) (scale);

        mMatrix = new Matrix();

        // Move the walls onto 1, 1
        mMatrix.setTranslate(-minX + roomPadding, -minY + roomPadding);
        mMatrix.postScale(scale, scale);

    }

    @Override
    public void draw(android.graphics.Canvas canvas) {

        canvas.drawColor(Color.LTGRAY);

        if (mRobotPath != null && !mRobotPath.isEmpty()) {
            canvas.drawPath(mRobotPath, mRobotPathPaint);
        }

        if (mWalls != null && !mWalls.isEmpty()) {
            canvas.drawPath(mWalls, mRoomWallPaint);
        }
    }

    /**
     * Sets the path to draw as a representation of where the robot moved.
     * 
     * @param robotPath the robots path.
     */
    public void setRobotPath(Path robotPath) {
        mRobotPathPaint.setColor(Color.DKGRAY);
        mRobotPathPaint.setStyle(Paint.Style.STROKE);
        mRobotPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mRobotPathPaint.setStrokeWidth(mRobotPathStrokeWidth);

        // PathEffect pathEffect = new DashPathEffect(new float[] {
        // 10, 10
        // }, 3);
        // mRobotPathPaint.setPathEffect(pathEffect);

        if (mMatrix != null && robotPath != null) {
            robotPath.transform(mMatrix);
        } else {
            Log.w(TAG, "No matrix set for scaling and translating!!!");
        }
        this.mRobotPath = robotPath;
    }

    /**
     * Sets the path to draw as a representation of the walls.
     * 
     * @param walls the walls.
     */
    public void setWalls(Path walls) {
        mRoomFloorPaint.setColor(Color.LTGRAY);
        mRoomFloorPaint.setStyle(Paint.Style.FILL);

        mRoomWallPaint.setColor(Color.GRAY);
        mRoomWallPaint.setStyle(Paint.Style.STROKE);
        mRoomWallPaint.setStrokeJoin(Paint.Join.MITER);
        mRoomWallPaint.setStrokeCap(Paint.Cap.SQUARE);
        mRoomWallPaint.setStrokeWidth(mRoomWallsStrokeWidth);

        if (mMatrix != null && walls != null) {
            walls.transform(mMatrix);
        } else {
            Log.w(TAG, "No matrix set for scaling and translating!!!");
        }
        this.mWalls = walls;
    }

}
