package se.kjellstrand.robot.engine;

import android.graphics.Point;
import android.util.Log;

/**
 * A rectangular room with walls bounding the room. A Rect2DRoom room starts at
 * the 1,1 coordinate and extends out on the positive end of the x and y axis. A
 * call to Rect2DRoom(5, 5, new Point(1,1)) would create a room with the
 * interior coordinates 1,1,5,5 and the getWalls() call would return (0,0,6,6)
 * for the same room. It is ok to set the start position outside the room, this
 * will lead to the robot getting stuck and unable to move.
 */
public class Rect2DRoom implements Room {

    private static final String TAG = Rect2DRoom.class.getCanonicalName();

    private int mRoomWidth = 0;
    private int mRoomLength = 0;

    private Point mStartPosition = new Point();

    /**
     * Constructor, initialises a room.
     * 
     * @param width the width of the new room.
     * @param length the length of the new room.
     * @param pos the position the robot will be inserted into the room at.
     */
    public Rect2DRoom(int width, int length, Point pos) {
        this.mRoomWidth = width;
        this.mRoomLength = length;
        this.mStartPosition = pos;
        Log.d(TAG, "New room, size: " + width + " x " + length + " startPos: " + pos);
    }

    @Override
    public boolean contains(Point pos) {
        return pos.x > 0 && pos.x <= mRoomWidth && pos.y > 0 && pos.y <= mRoomLength;
    }

    @Override
    public Point getStartPosition() {
        Point p = new Point();
        p.x = mStartPosition.x;
        p.y = mStartPosition.y;
        return p;
    }

    /**
     * Get the width of the room.
     * 
     * @return the width of the room.
     */
    public int getRoomWidth() {
        return mRoomWidth;
    }

    /**
     * Get the length of the room.
     * 
     * @return the length of the room.
     */
    public int getRoomLength() {
        return mRoomLength;
    }

    /**
     * Get the walls of the room.
     * 
     * @return a list of points defining the walls of the room. The list is a
     *         open polygon where the first and last points makes up the final
     *         wall.
     */
    public Point[] getWalls() {
        Point[] walls = new Point[] {
                new Point(0, 0),
                new Point(mRoomWidth + 1, 0),
                new Point(mRoomWidth + 1, mRoomLength + 1),
                new Point(0, mRoomLength + 1)
        };
        return walls;
    }

}
