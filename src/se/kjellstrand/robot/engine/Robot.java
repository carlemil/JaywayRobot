package se.kjellstrand.robot.engine;

import android.util.Log;

public class Robot {

    /**
     * Tag used to enable easy filtering in logcat.
     */
    private static final String TAG = Robot.class.getCanonicalName();

    private Rect2DRoom mRoom;

    private RobotLocation mRobotLocation = new RobotLocation();

    /**
     * Contains the program for a robot.
     */
    private String mProgram;

    /**
     * Points to the current instruction of the program/robot.
     */
    private int mIntructionPointer = 0;

    private final char FORWARD;

    private final char LEFT;

    private final char RIGHT;

    public Robot() {
        this(Language.ENGLISH);
    }

    public Robot(Language language) {
        Log.d(TAG, "Robot started, languare: " + language.toString());
        FORWARD = Language.getForwardChar(language);
        LEFT = Language.getLeftChar(language);
        RIGHT = Language.getRightChar(language);
    }

    public String getProgram() {
        return this.mProgram;
    }

    public RobotLocation getRobotPosition() {
        return mRobotLocation;
    }

    public boolean hasMoreMoves() {
        if (mProgram != null && mProgram.length() > mIntructionPointer) {
            return true;
        }
        return false;
    }

    public RobotLocation move() {
        if (hasMoreMoves()) {
            char command = mProgram.charAt(mIntructionPointer++);

            if (command == FORWARD) {
                Log.d(TAG, "Move forward");
                moveForward(mRobotLocation);
            } else if (command == LEFT) {
                Log.d(TAG, "Turn left, then move forward.");
                turnLeft(mRobotLocation);
                moveForward(mRobotLocation);
            } else if (command == RIGHT) {
                Log.d(TAG, "Turn right, then move forward.");
                turnRight(mRobotLocation);
                moveForward(mRobotLocation);
            }

            Log.d(TAG, "Robot after moving: " + mRobotLocation.toString());

            return new RobotLocation(mRobotLocation);
        }
        return null;
    }

    private void moveForward(RobotLocation coord) {
        Direction dir = coord.getDirection();
        switch (dir) {
            case EAST:
                coord.getPosition().offset(1, 0);
                if (!mRoom.contains(coord.getPosition())) {
                    coord.getPosition().offset(-1, 0);
                    Log.d(TAG, "Robot ran into a wall");
                }
                break;
            case NORTH:
                coord.getPosition().offset(0, 1);
                if (!mRoom.contains(coord.getPosition())) {
                    coord.getPosition().offset(0, -1);
                    Log.d(TAG, "Robot ran into a wall");
                }
                break;
            case WEST:
                coord.getPosition().offset(-1, 0);
                if (!mRoom.contains(coord.getPosition())) {
                    coord.getPosition().offset(1, 0);
                    Log.d(TAG, "Robot ran into a wall");
                }
                break;
            case SOUTH:
                coord.getPosition().offset(0, -1);
                if (!mRoom.contains(coord.getPosition())) {
                    coord.getPosition().offset(0, 1);
                    Log.d(TAG, "Robot ran into a wall");
                }
                break;

            default:
                Log.w(TAG, "Unknown Direction: " + dir.toString());
                break;
        }
    }

    public RobotLocation moveUntilEnd() {
        RobotLocation res = null;
        while (hasMoreMoves()) {
            res = move();
        }
        return res;
    }

    public void putInRoom(Rect2DRoom room) {
        this.mRoom = room;
        this.mRobotLocation.setPosition(room.getStartPosition());
        this.mRobotLocation.setDirection(Direction.NORTH);
        this.mIntructionPointer = 0;
    }

    public void setProgram(String program) {
        this.mProgram = program;
        this.mIntructionPointer = 0;
    }

    private void turnLeft(RobotLocation coord) {
        Direction dir = coord.getDirection();
        switch (dir) {
            case EAST:
                coord.setDirection(Direction.NORTH);
                break;
            case NORTH:
                coord.setDirection(Direction.WEST);
                break;
            case WEST:
                coord.setDirection(Direction.SOUTH);
                break;
            case SOUTH:
                coord.setDirection(Direction.EAST);
                break;

            default:
                Log.w(TAG, "Unknown Direction set: " + dir.toString());
                break;
        }
    }

    private void turnRight(RobotLocation coord) {
        Direction dir = coord.getDirection();
        switch (dir) {
            case EAST:
                coord.setDirection(Direction.SOUTH);
                break;
            case NORTH:
                coord.setDirection(Direction.EAST);
                break;
            case WEST:
                coord.setDirection(Direction.NORTH);
                break;
            case SOUTH:
                coord.setDirection(Direction.WEST);
                break;

            default:
                Log.w(TAG, "Unknown Direction set: " + dir.toString());
                break;
        }
    }

    public Rect2DRoom getRoom() {
        return mRoom;
    }

}
