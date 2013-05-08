package se.kjellstrand.robotconstructionkit;

import android.graphics.Point;
import android.util.Log;

public class Robot {

    private static final String TAG = Robot.class.getCanonicalName();

    private Room mRoom;

    private RobotCoordinate mRobotPosition = new RobotCoordinate();

    private String mInstructions;
    private int mIntructionPointer = 0;

    private char mLeft;
    private char mRight;
    private char mForward;

    public Robot(Language language) {
        setLanguage(language);
    }

    public void setLanguage(Language language) {
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

    public void setInstructions(String instructions) {
        this.mInstructions = instructions;
        this.mIntructionPointer = 0;
    }

    public void putInRoom(Room room) {
        this.mRoom = room;
        this.mRobotPosition.setPosition(room.getStartPosition());
        this.mIntructionPointer = 0;
    }

    public RobotCoordinate getRobotPosition() {
        return mRobotPosition;
    }
    
    public String moveUntilEnd(){
        String res = null;
        while(hasMoreMoves()){
            res = move();
        }
        return res;
    }

    public String move() {
        if (hasMoreMoves()) {

            char command = mInstructions.charAt(mIntructionPointer++);

            Log.d(TAG, "Command: " + command + ", pos: " + mRobotPosition);

            if (command == mForward) {
                Log.d(TAG, "Move forward");
                moveForward(mRobotPosition);
            } else if (command == mLeft) {
                Log.d(TAG, "Turn left");
                turnLeft(mRobotPosition);
            } else if (command == mRight) {
                Log.d(TAG, "Turn right");
                turnRight(mRobotPosition);
            }
            Log.d(TAG, "After command, pos: " + mRobotPosition);

            Point p = mRobotPosition.getPosition();
            return p.x + " " + p.y + " " + mRobotPosition.getDirection().toString().charAt(0);

        }
        return null;
    }

    private void moveForward(RobotCoordinate coord) {
        Direction dir = coord.getDirection();
        switch (dir) {
            case EAST:
                Log.d(TAG, "move east");
                coord.getPosition().offset(1, 0);
                if (!mRoom.contains(coord.getPosition())) {
                    Log.d(TAG, "undo move east");
                    coord.getPosition().offset(-1, 0);
                }
                break;
            case NORTH:
                Log.d(TAG, "move north");
                coord.getPosition().offset(0, 1);
                if (!mRoom.contains(coord.getPosition())) {
                    Log.d(TAG, "undo move north");
                    coord.getPosition().offset(0, -1);
                }
                break;
            case WEST:
                Log.d(TAG, "move west");
                coord.getPosition().offset(-1, 0);
                if (!mRoom.contains(coord.getPosition())) {
                    Log.d(TAG, "undo move west");
                    coord.getPosition().offset(1, 0);
                }
                break;
            case SOUTH:
                Log.d(TAG, "move south");
                coord.getPosition().offset(0, -1);
                if (!mRoom.contains(coord.getPosition())) {
                    Log.d(TAG, "undo move south");
                    coord.getPosition().offset(0, 1);
                }
                break;

            default:
                Log.w(TAG, "Unknown Direction: " + dir.toString());
                break;
        }
    }

    private void turnLeft(RobotCoordinate coord) {
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

    private void turnRight(RobotCoordinate coord) {
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
        if (mInstructions != null && mInstructions.length() > mIntructionPointer) {
            return true;
        }
        return false;
    }

}
