package se.kjellstrand.robot.engine;

import android.util.Log;

public class Robot {

    private static final String TAG = Robot.class.getCanonicalName();

    private Room mRoom;

    private RobotLocation mRobotLocation = new RobotLocation();

    /**
     * Contains the program for a robot.
     */
    private String mProgram;
    /**
     * Points to the current instruction of the program/robot.
     */
    private int mIntructionPointer = 0;

    private char mLeft;
    private char mRight;
    private char mForward;

    public Robot(Language language) {
        setLanguage(language);
    }
    
    public void setLanguage(Language language) {
        
        // TODO change to using English in the code, and hashmap for translations for display purposes ?
        
        switch (language) {
            case SWEDISH:
                mLeft = 'V';
                mRight = 'H';
                mForward = 'G';
                break;

            case ENGLISH:
                mLeft = 'L';
                mRight = 'R';
                mForward = 'F';
                break;

            default:
                Log.w(TAG, "Unknown language set: " + language.toString());
                break;
        }
    }

    public char getLeftChar() {
        return mLeft;
    }

    public char getRightChar() {
        return mRight;
    }

    public char getForwardChar() {
        return mForward;
    }

    public void setProgram(String program) {
        this.mProgram = program;
        this.mIntructionPointer = 0;
    }
    
    public String getProgram() {
        return this.mProgram;
    }

    public void putInRoom(Room room) {
        this.mRoom = room;
        this.mRobotLocation.setPosition(room.getStartPosition());
        this.mRobotLocation.setDirection(Direction.NORTH);
        this.mIntructionPointer = 0;
    }

    public RobotLocation getRobotPosition() {
        return mRobotLocation;
    }

    public RobotLocation moveUntilEnd() {
        RobotLocation res = null;
        while (hasMoreMoves()) {
            res = move();
        }
        return res;
    }

    public RobotLocation move() {
        if (hasMoreMoves()) {
            char command = mProgram.charAt(mIntructionPointer++);

            if (command == mForward) {
                Log.d(TAG, "Move forward");
                moveForward(mRobotLocation);
            } else if (command == mLeft) {
                Log.d(TAG, "Turn left");
                turnLeft(mRobotLocation);
            } else if (command == mRight) {
                Log.d(TAG, "Turn right");
                turnRight(mRobotLocation);
            }

            Log.d(TAG, "Robot after moving: " + mRobotLocation.toString());
            
            return mRobotLocation;
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

    public boolean hasMoreMoves() {
        if (mProgram != null && mProgram.length() > mIntructionPointer) {
            return true;
        }
        return false;
    }

}
