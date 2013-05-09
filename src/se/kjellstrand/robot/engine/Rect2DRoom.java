package se.kjellstrand.robot.engine;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * A rectangular room with walls bounding the room.
 * 
 */
public class Rect2DRoom implements RoomWithWalls {

    private static final String TAG = Rect2DRoom.class.getCanonicalName();

    private int mRoomWidth = 0;
    private int mRoomHeight = 0;

    private Point mStartPosition = new Point();

    public Rect2DRoom(int width, int height, Point pos) {
        this.mRoomWidth = width;
        this.mRoomHeight = height;
        this.mStartPosition = pos;
        Log.d(TAG, "New room: " + width + " x " + height + " startPos: " + pos);
    }

    @Override
    public Point getStartPosition() {
        return mStartPosition;
    }

    @Override
    public boolean contains(Point pos) {
        return pos.x > 0 && pos.x < mRoomWidth && pos.y > 0 && pos.y < mRoomHeight;
    }

    @Override
    public Point[] getWalls() {
        Point[] walls = new Point[] {
                new Point(-1, -1),
                new Point(mRoomWidth, -1),
                new Point(mRoomWidth, mRoomHeight),
                new Point(-1, mRoomHeight)
        };
        return walls;
    }

}
