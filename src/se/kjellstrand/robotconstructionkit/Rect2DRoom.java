package se.kjellstrand.robotconstructionkit;

import android.graphics.Point;
import android.graphics.Rect;

public class Rect2DRoom implements Room {

    private Rect mRoomDimensions = new Rect();

    private Point mStartPosition = new Point();

    public Rect2DRoom(Rect dim, Point pos) {
        this.mRoomDimensions = dim;
        this.mStartPosition = pos;
    }

    @Override
    public Point getStartPosition() {
        return mStartPosition;
    }

    @Override
    public boolean contains(Point pos) {
        // android.graphics.Rect puts origo in the upper left corner, we want it
        // in the lower left, so we will do our own contains test.
        return this.mRoomDimensions.bottom <= pos.y &&
                this.mRoomDimensions.top > pos.y &&
                this.mRoomDimensions.left <= pos.x &&
                this.mRoomDimensions.right > pos.x;
    }

}
