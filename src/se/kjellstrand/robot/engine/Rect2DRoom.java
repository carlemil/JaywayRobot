package se.kjellstrand.robot.engine;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Rect2DRoom implements RoomWithWalls {

    private static final String TAG = Rect2DRoom.class.getCanonicalName();

    private Rect mRoomDimensions = new Rect();

    private Point mStartPosition = new Point();

    public Rect2DRoom(Rect dim, Point pos) {
        this.mRoomDimensions = dim;
        this.mStartPosition = pos;
        Log.d(TAG, "New room: " + mRoomDimensions + " startPos: " + pos);
    }

    @Override
    public Point getStartPosition() {
        return mStartPosition;
    }

    @Override
    public boolean contains(Point pos) {
        return this.mRoomDimensions.contains(pos.x, pos.y);
    }

    @Override
    public Point[] getWalls() {
        Point[] walls = new Point[] {
                new Point(0, 0),
                new Point(mRoomDimensions.right, 0),
                new Point(mRoomDimensions.right, mRoomDimensions.bottom),
                new Point(0, mRoomDimensions.bottom)
        };
        return walls;
    }

}
