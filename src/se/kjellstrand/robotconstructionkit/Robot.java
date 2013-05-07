package se.kjellstrand.robotconstructionkit;

import android.util.Log;

public class Robot {

    private static final String TAG = Robot.class.getSimpleName();

    private Room mRoom = new Robot2DRoom();

    private RobotPosition mRobotPosition;

    private String mInstructions;
    private int mIntructionPointer = 0;

    private char mLeft;
    private char mRight;
    private char mForward;

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

    public String getInstructions() {
        return mInstructions;
    }

    public void setInstructions(String instructions) {
        this.mInstructions = instructions;
    }

    public RobotPosition getRobotPosition() {
        return mRobotPosition;
    }

    public void setRobotPosition(RobotPosition robotPosition) {
        this.mRobotPosition = robotPosition;
    }

    public boolean move() {
        if (hasNextMove()) {

            char command = mInstructions.charAt(mIntructionPointer++);

            if (command == mForward) {
                moveForward(mRobotPosition);
            } else if (command == mLeft) {
                turnLeft(mRobotPosition);
            } else if (command == mRight) {
                turnRight(mRobotPosition);
            }
            return true;
        }
        return false;
    }

    private void moveForward(RobotPosition robotPosition) {
        Direction dir = robotPosition.getDirection();
        switch (dir) {
            case EAST:
                
                // TODO set the move offsets 
                
                robotPosition.getPosition().offset(0, 0);
                if (!mRoom.contains(robotPosition.getPosition())) {
                    robotPosition.getPosition().offset(0, 0);
                }
                break;
            case NORTH:
                robotPosition.getPosition().offset(0, 0);
                if (!mRoom.contains(robotPosition.getPosition())) {
                    robotPosition.getPosition().offset(0, 0);
                }
                break;
            case WEST:
                robotPosition.getPosition().offset(0, 0);
                if (!mRoom.contains(robotPosition.getPosition())) {
                    robotPosition.getPosition().offset(0, 0);
                }
                break;
            case SOUTH:
                robotPosition.getPosition().offset(0, 0);
                if (!mRoom.contains(robotPosition.getPosition())) {
                    robotPosition.getPosition().offset(0, 0);
                }
                break;

            default:
                Log.w(TAG, "Unknown Direction set: " + dir.toString());
                break;
        }
    }

    private void turnLeft(RobotPosition robotPosition) {
        Direction dir = robotPosition.getDirection();
        switch (dir) {
            case EAST:
                robotPosition.setDirection(Direction.NORTH);
                break;
            case NORTH:
                robotPosition.setDirection(Direction.WEST);
                break;
            case WEST:
                robotPosition.setDirection(Direction.SOUTH);
                break;
            case SOUTH:
                robotPosition.setDirection(Direction.EAST);
                break;

            default:
                Log.w(TAG, "Unknown Direction set: " + dir.toString());
                break;
        }
    }

    private void turnRight(RobotPosition robotPosition) {
        Direction dir = robotPosition.getDirection();
        switch (dir) {
            case EAST:
                robotPosition.setDirection(Direction.SOUTH);
                break;
            case NORTH:
                robotPosition.setDirection(Direction.EAST);
                break;
            case WEST:
                robotPosition.setDirection(Direction.NORTH);
                break;
            case SOUTH:
                robotPosition.setDirection(Direction.WEST);
                break;

            default:
                Log.w(TAG, "Unknown Direction set: " + dir.toString());
                break;
        }
    }

    public boolean hasNextMove() {
        if (mInstructions != null && mInstructions.length() > mIntructionPointer) {
            return true;
        }
        return false;
    }

}
