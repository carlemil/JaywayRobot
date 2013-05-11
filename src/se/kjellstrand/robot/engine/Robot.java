package se.kjellstrand.robot.engine;

import android.util.Log;

/**
 * A robot that reads instructions from a "program" and moves according to the
 * instructions.
 * 
 */
public class Robot {

    /**
     * Tag used to enable easy filtering in logcat.
     */
    private static final String TAG = Robot.class.getCanonicalName();

    /**
     * The room that the robot is currently located in, or null if the robot is
     * in dev/null/ :)
     */
    private BoundingBoxRoom mRoom;

    /**
     * The position that the robot is located at, defined as a RobotLocation,
     * that consists of, x and y-coordinates and a direction.
     */
    private RobotLocation mRobotLocation = new RobotLocation();

    /**
     * Contains the program for a robot.
     */
    private StringBuilder mProgram;

    /**
     * Points to the current instruction of the program/robot.
     */
    private int mIntructionPointer = 0;

    /**
     * The char currently used to represent a move forward instruction in the
     * robots programming language.
     */
    public char mForwardChar;

    /**
     * The char currently used to represent a move left instruction in the
     * robots programming language.
     */
    public char mLeftChar;

    /**
     * The char currently used to represent a move right instruction in the
     * robots programming language.
     */
    public char mRightChar;

    /**
     * The language understood by this robot.
     */
    private Language mLanguage;

    /**
     * Default constructor. Sets the programming language to English.
     */
    public Robot() {
        this(Language.ENGLISH);
    }

    /**
     * Constructor that allows for initialising with a programming language
     * other than the default.
     * 
     * @param language The programming language that this robot will understand.
     */
    public Robot(Language language) {
        Log.d(TAG, "Robot started, language: " + language.toString());
        mLanguage = language;
        setLanguage(mLanguage);
    }

    /**
     * Returns the position of the robot.
     * 
     * @return the robots position.
     */
    public RobotLocation getRobotPosition() {
        return mRobotLocation;
    }

    /**
     * Returns true if the robot has not reached the end of its program. Returns
     * false once the end of the program have been reached.
     * 
     * @return true if the robot has more instructions in its program, else
     *         false.
     */
    public boolean hasMoreMoves() {
        if (mProgram != null && mProgram.length() > mIntructionPointer) {
            return true;
        }
        return false;
    }

    /**
     * Executes the next instruction (if there is one) in the robots program,
     * returns the location that the robot ends up at after executing the
     * instruction.
     * 
     * @return the robots location.
     */
    public RobotLocation move() {
        if (hasMoreMoves()) {
            char command = mProgram.charAt(mIntructionPointer++);

            if (command == mForwardChar) {
                Log.d(TAG, "Move forward");
                moveForward(mRobotLocation);
            } else if (command == mLeftChar) {
                Log.d(TAG, "Turn left, then move forward.");
                turnLeft(mRobotLocation);
                moveForward(mRobotLocation);
            } else if (command == mRightChar) {
                Log.d(TAG, "Turn right, then move forward.");
                turnRight(mRobotLocation);
                moveForward(mRobotLocation);
            }

            Log.d(TAG, "Robot after moving: " + mRobotLocation.toString(mLanguage));

            return new RobotLocation(mRobotLocation);
        }
        return null;
    }

    /**
     * Executes all the instructions in the robots program, returns the final
     * position of the robot.
     * 
     * @return the robots location.
     */
    public RobotLocation moveUntilEnd() {
        RobotLocation res = null;
        while (hasMoreMoves()) {
            res = move();
        }
        return res;
    }

    /**
     * Positions the robot in "room"
     * 
     * @param room the room to put the robot in.
     */
    public void putInRoom(BoundingBoxRoom room) {
        this.mRoom = room;
        this.mRobotLocation.setPosition(room.getStartPosition());
        this.mRobotLocation.setDirection(Direction.NORTH);
        this.mIntructionPointer = 0;
    }

    /**
     * Returns the robots language.
     * 
     * @return the language
     */
    public Language getLanguage() {
        return this.mLanguage;
    }

    /**
     * Sets the language of the robot, and resets the program.
     * 
     * @param mLanguage the language to set.
     */
    public void setLanguage(Language language) {
        this.mLanguage = language;
        mForwardChar = Language.getForwardChar(language);
        mLeftChar = Language.getLeftChar(language);
        mRightChar = Language.getRightChar(language);
        // Reset our program since the language changed.
        resetProgram(null);
    }

    /**
     * Returns the robots program.
     * 
     * @return the program
     */
    public StringBuilder getProgram() {
        return this.mProgram;
    }

    /**
     * Sets a new program to the robot.
     * 
     * @param program The program to set.
     */
    public void resetProgram(StringBuilder program) {
        if (program == null) {
            this.mProgram = new StringBuilder();
        } else {
            this.mProgram = program;
        }
        this.mIntructionPointer = 0;
    }

    /**
     * Appends a char to the program.
     * 
     * @param c The char to append to the program.
     */
    public void appendProgram(char c) {
        this.mProgram.append(c);
    }

    /**
     * Deletes a char from the program.
     * 
     */
    public void deleteCharFromProgram() {
        if (mProgram.length() > 0) {
            this.mProgram.deleteCharAt(0);
        }
    }

    /**
     * Gets the room that the robot is currently in.
     * 
     * @return the room the robot is in.
     */
    public BoundingBoxRoom getRoom() {
        return mRoom;
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
                Log.w(TAG, "Unknown Direction: " + dir.toString());
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
                Log.w(TAG, "Unknown Direction: " + dir.toString());
                break;
        }
    }

}
