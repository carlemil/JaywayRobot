package se.kjellstrand.robot.engine;

import android.util.Log;

/**
 * A robot that reads instructions from a "program" and moves according to the instructions.
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
    private Rect2DRoom mRoom;

    /**
     * The position that the robot is located at, defined as a RobotLocation,
     * that consists of, x and y-coordinates and a direction.
     */
    private RobotLocation mRobotLocation = new RobotLocation();

    /**
     * Contains the program for a robot.
     */
    private String mProgram;

    /**
     * Points to the current instruction of the program/robot.
     */
    private int mIntructionPointer = 0;

    /**
     * The char currently used to represent a move forward instruction in the
     * robots programming language.
     */
    private final char FORWARD;

    /**
     * The char currently used to represent a move left instruction in the
     * robots programming language.
     */
    private final char LEFT;

    /**
     * The char currently used to represent a move right instruction in the
     * robots programming language.
     */
    private final char RIGHT;

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
        FORWARD = Language.getForwardChar(language);
        LEFT = Language.getLeftChar(language);
        RIGHT = Language.getRightChar(language);
    }

    public String getProgram() {
        return this.mProgram;
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
    public void putInRoom(Rect2DRoom room) {
        this.mRoom = room;
        this.mRobotLocation.setPosition(room.getStartPosition());
        this.mRobotLocation.setDirection(Direction.NORTH);
        this.mIntructionPointer = 0;
    }

    /**
     * Sets a new program to the robot.
     * 
     * @param program The program to set.
     */
    public void setProgram(String program) {
        this.mProgram = program;
        this.mIntructionPointer = 0;
    }

    /**
     * Gets the room that the robot is currently in.
     * 
     * @return the room the robot is in.
     */
    public Rect2DRoom getRoom() {
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

}
