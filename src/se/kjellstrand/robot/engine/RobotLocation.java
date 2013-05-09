package se.kjellstrand.robot.engine;

import android.graphics.Point;

public class RobotLocation {

    private Direction mDirection = Direction.NORTH;

    private Point mPosition = new Point();

    public RobotLocation() {

    }

    public RobotLocation(RobotLocation robotLocation) {
        mDirection = robotLocation.getDirection();
        mPosition.x = robotLocation.getPosition().x;
        mPosition.y = robotLocation.getPosition().y;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public Point getPosition() {
        return mPosition;
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setPosition(Point position) {
        this.mPosition = position;
    }

    @Override
    public String toString() {
        return mPosition.x + " " + mPosition.y + " " + mDirection.toString().charAt(0);
    }
}
