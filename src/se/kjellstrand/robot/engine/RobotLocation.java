package se.kjellstrand.robot.engine;

import android.graphics.Point;

/**
 * Holds the position and direction of the robot at a given time. Used both for
 * the keeping track of the current position of the robot as well as the history
 * of the robots moves.
 * 
 */
public class RobotLocation {

    /**
     * The direction of the robot.
     */
    private Direction mDirection = Direction.NORTH;

    /**
     * The postition opf the robot.
     */
    private Point mPosition = new Point();

    /**
     * Empty constructor, used by the robot to create a uninitialised start
     * location.
     */
    public RobotLocation() {
    }

    /**
     * Creates a new RobotLocation, copying the values from the robotLocation
     * param.
     * 
     * @param robotLocation to copy values from to the new RobotLocation.
     */
    public RobotLocation(RobotLocation robotLocation) {
        mDirection = robotLocation.getDirection();
        mPosition.x = robotLocation.getPosition().x;
        mPosition.y = robotLocation.getPosition().y;
    }

    /**
     * Returns the direction.
     * 
     * @return the direction.
     */
    public Direction getDirection() {
        return mDirection;
    }

    /**
     * Returns the position.
     * 
     * @return the position.
     */
    public Point getPosition() {
        return mPosition;
    }

    /**
     * Sets the direction.
     * 
     * @param direction the direction to set.
     */
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    /**
     * Sets the position.
     * 
     * @param position the position to set.
     */
    public void setPosition(Point position) {
        this.mPosition = position;
    }

    /**
     * Returns a language specific string for this RobotLocation.
     * 
     * @param language the language to create a string for.
     * 
     * @return the string.
     */
    public String toString(Language language) {
        return mPosition.x + " " + mPosition.y + " " + Direction.getChar(mDirection, language);
    }
}
