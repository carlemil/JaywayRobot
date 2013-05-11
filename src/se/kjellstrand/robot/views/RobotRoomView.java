package se.kjellstrand.robot.views;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.BoundingBoxRoom;
import se.kjellstrand.robot.engine.RobotLocation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
     * Room that the robot is located in. Used for drawing walls/floor.
     */
    private BoundingBoxRoom mRoom;

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
     * The location of the robot.
     */
    private RobotLocation mLocation;

    /**
     * Bitmaps used for indicating the location and direction of the robot.
     */
    private Bitmap mRobotBitmap[];

    /**
     * Simple constructor to use when creating a view from code.
     * 
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public RobotRoomView(Context context) {
        super(context);
        loadRobotDrawable();
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
        loadRobotDrawable();
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
        loadRobotDrawable();
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
    private void defineViewPort(int minX, int minY, int maxX, int maxY, float roomPadding) {
        float roomWidth = (roomPadding * 2) + maxX - minX;
        float roomHeight = (roomPadding * 2) + maxY - minY;

        float xScale = roomWidth / getWidth();
        float yScale = roomHeight / getHeight();

        float scale = 1 / Math.max(xScale, yScale);

        mRobotPathStrokeWidth = (int) (scale * 0.6);

        mMatrix = new Matrix();

        // Move the walls onto 1, 1
        mMatrix.setTranslate(-minX + roomPadding, -minY + roomPadding);
        mMatrix.postScale(scale, scale);
        int horisontalOffset = (getWidth() / 2) - (int) ((roomWidth * scale) / 2);
        mMatrix.postTranslate(horisontalOffset, 0);
    }

    @Override
    public void draw(android.graphics.Canvas canvas) {
        drawRoom(canvas);
        drawRobotPath(canvas);
        drawRobotLocation(canvas);
    }

    /**
     * Sets the path to draw as a representation of where the robot moved.
     * 
     * @param robotPath the robots path.
     */
    public void setRobotPath(Path robotPath) {
        mRobotPathPaint.setARGB(0xff, 0, 0xff, 0);
        mRobotPathPaint.setStyle(Paint.Style.STROKE);
        mRobotPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mRobotPathPaint.setStrokeWidth(mRobotPathStrokeWidth);

        if (mMatrix != null && robotPath != null) {
            robotPath.transform(mMatrix);
        }
        this.mRobotPath = robotPath;
    }

    /**
     * Sets the bounding box room.
     * 
     * @param room the room to set.
     */
    public void setRoom(BoundingBoxRoom room) {
        this.mRoom = room;
        mRoomFloorPaint.setColor(Color.LTGRAY);
        mRoomFloorPaint.setStyle(Paint.Style.FILL);

        mRoomWallPaint.setColor(Color.GRAY);
        mRoomWallPaint.setStyle(Paint.Style.FILL);

        defineViewPort(mRoom.getBoundingBox().first.x, mRoom.getBoundingBox().first.y,
                mRoom.getBoundingBox().second.x, mRoom.getBoundingBox().second.y, 1f);
    }

    /**
     * Sets the location that should be marked as the location of the robot.
     * 
     * @param location the location to mark.
     */
    public void setRobotLocation(RobotLocation location) {
        this.mLocation = location;
    }

    private void drawRobotPath(android.graphics.Canvas canvas) {
        if (mRobotPath != null && !mRobotPath.isEmpty()) {
            canvas.drawPath(mRobotPath, mRobotPathPaint);
        }
    }

    private void drawRobotLocation(Canvas canvas) {
        if (mLocation != null && mRobotBitmap != null) {
            int direction = mLocation.getDirection().ordinal();

            int x = mLocation.getPosition().x;
            int y = mRoom.getBoundingBox().first.y + (mRoom.getBoundingBox().second.y - mLocation.getPosition().y);
            float size = 0.6f;
            RectF dst = new RectF(x - size, y - size, x + size, y + size);
            mMatrix.mapRect(dst);
            Rect src = new Rect(0, 0, mRobotBitmap[direction].getWidth(), mRobotBitmap[direction].getHeight());
            canvas.drawBitmap(mRobotBitmap[direction], src, dst, null);
        }
    }

    private void drawRoom(android.graphics.Canvas canvas) {
        if (mRoom != null) {
            int left = mRoom.getBoundingBox().first.x;
            int right = mRoom.getBoundingBox().second.x;
            int bottom = mRoom.getBoundingBox().first.y;
            int top = mRoom.getBoundingBox().second.y;

            Point position = new Point();
            RectF floorTile = new RectF();

            for (int x = left; x <= right; x++) {
                for (int y = bottom; y <= top; y++) {
                    position.set(x, y);
                    floorTile.set(x - 0.5f, y - 0.5f, x + 0.5f, y + 0.5f);
                    mMatrix.mapRect(floorTile);
                    if (mRoom.contains(position)) {
                        // Draw a floor tile
                        canvas.drawRect(floorTile, mRoomFloorPaint);
                    } else {
                        // Draw a wall tile.
                        canvas.drawRect(floorTile, mRoomWallPaint);
                    }
                }
            }
        }
    }

    private void loadRobotDrawable() {
        Matrix m = new Matrix();
        mRobotBitmap = new Bitmap[4];
        Bitmap tmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        m.setRotate(180);
        mRobotBitmap[0] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m, true);
        m.setRotate(90);
        mRobotBitmap[1] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m, true);
        m.setRotate(0);
        mRobotBitmap[2] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m, true);
        m.setRotate(270);
        mRobotBitmap[3] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m, true);
    }

}
