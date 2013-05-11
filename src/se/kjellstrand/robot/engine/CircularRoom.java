package se.kjellstrand.robot.engine;

import android.graphics.Point;
import android.util.Log;
import android.util.Pair;

/**
 * A rectangular room with walls bounding the room. A Rect2DRoom room starts at
 * the 1,1 coordinate and extends out on the positive end of the x and y axis. A
 * call to Rect2DRoom(5, 5, new Point(1,1)) would create a room with the
 * interior coordinates 1,1,5,5 and the getWalls() call would return (0,0,6,6)
 * for the same room. It is ok to set the start position outside the room, this
 * will lead to the robot getting stuck and unable to move.
 */
public class CircularRoom implements BoundingBoxRoom {

    /**
     * Tag used to enable easy filtering in logcat.
     */
    private static final String TAG = CircularRoom.class.getCanonicalName();

    /**
     * The radius of the room.
     */
    private int mRoomRadius = 0;

    /**
     * The position that the robot will be insert into the room at.
     */
    private Point mStartPosition = new Point();

    /**
     * Constructor, initialises a room.
     * 
     * @param width the width of the new room.
     * @param length the length of the new room.
     * @param pos the position the robot will be inserted into the room at.
     */
    public CircularRoom(int radius, Point pos) {
        this.mRoomRadius = radius;
        this.mStartPosition = pos;
        Log.d(TAG, "New circular room, radius: " + radius + " startPos: " + pos);
    }

    @Override
    public boolean contains(Point pos) {
        return Math.pow(pos.x, 2d) + Math.pow(pos.y, 2d) <= Math.pow(mRoomRadius, 2d);
    }

    @Override
    public Point getStartPosition() {
        Point p = new Point();
        p.x = mStartPosition.x;
        p.y = mStartPosition.y;
        return p;
    }

    @Override
    public Pair<Point, Point> getBoundingBox() {
        int r = mRoomRadius + 1;
        Point bottomLeft = new Point(-r, -r);
        Point upperRight = new Point(r, r);
        return new Pair<Point, Point>(bottomLeft, upperRight);
    }

}
