package se.kjellstrand.robotconstructionkit;

import android.graphics.Point;

public class RobotPosition {

    private Direction mDirection = Direction.NORTH;

    private Point mPosition = new Point();

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public Point getPosition() {
        return mPosition;
    }

    public void setPosition(Point position) {
        this.mPosition = position;
    }
}
