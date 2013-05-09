package se.kjellstrand.robot.engine;

import android.graphics.Point;
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
        Point p = new Point();
        p.x = mStartPosition.x;
        p.y = mStartPosition.y;
        return p;
    }

    @Override
    public boolean contains(Point pos) {
        return pos.x > 0 && pos.x <= mRoomWidth && pos.y > 0 && pos.y <= mRoomHeight;
    }

    @Override
    public Point[] getWalls() {
        Point[] walls = new Point[] {
                new Point(0, 0),
                new Point(mRoomWidth + 1, 0),
                new Point(mRoomWidth + 1, mRoomHeight + 1),
                new Point(0, mRoomHeight + 1)
        };
        return walls;
    }

}
