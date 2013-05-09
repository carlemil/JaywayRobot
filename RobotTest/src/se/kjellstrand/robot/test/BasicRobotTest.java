package se.kjellstrand.robot.test;

import android.graphics.Point;
import android.graphics.Rect;
import se.kjellstrand.robot.engine.Language;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.Room;
import junit.framework.TestCase;

public class BasicRobotTest extends TestCase {

    private static final char N = 'N';
    private static final char W = 'W';
    private static final char E = 'E';
    private static final char S = 'S';

    public BasicRobotTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBasicRobotMoves() {
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(0, 0, startPos);
        Robot robot = new Robot();

        assertEquals(null, robot.move());

        robot.putInRoom(room);
        assertEquals(null, robot.move());

        // set instructions to, "one step forward".
        robot.setProgram("F");
        // Still a 0,0 size room
        assertEquals("1 1 " + N, robot.move().toString());

        // create a room 5x5, and put the robot in it.
        room = new Rect2DRoom(5, 5, startPos);
        robot.putInRoom(room);
        assertEquals("1 2 " + N, robot.move().toString());

        // set instructions to, "left".
        robot.setProgram("L");
        assertEquals("1 2 " + W, robot.move().toString());

        // set instructions to, "right".
        robot.setProgram("R");
        assertEquals("1 3 " + N, robot.move().toString());

        // set instructions to, "right".
        robot.setProgram("R");
        assertEquals("2 3 " + E, robot.move().toString());

    }

    public void testRobotMovesCommands1() {

        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(5, 5, startPos);
        Robot robot = new Robot();
        robot.putInRoom(room);

        robot.setProgram("FRFFLFLLFF");
        assertEquals("3 1 " + S, robot.moveUntilEnd().toString());
    }

    public void testRobotMovesCommands2() {
        Point startPos = new Point(1, 2);
        Room room = new Rect2DRoom(5, 5, startPos);
        Robot robot = new Robot();
        robot.putInRoom(room);

        robot.setProgram("RFRFFRFRF");
        assertEquals("1 3 " + N, robot.moveUntilEnd().toString());
    }

    public void testRobotMovesCommandsRoomBoundingBox1() {

        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(3, 3, startPos);
        Robot robot = new Robot();
        robot.putInRoom(room);

        robot.setProgram("FFF");
        assertEquals("1 3 " + N, robot.moveUntilEnd().toString());
    }

    public void testRobotMovesCommandsRoomBoundingBox2() {
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(3, 3, startPos);
        Robot robot = new Robot();
        robot.putInRoom(room);

        robot.setProgram("RFFFFFF");
        assertEquals("3 1 " + E, robot.moveUntilEnd().toString());
    }

    public void testRobotMovesCommandsRoomBoundingBox3() {
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(6, 8, startPos);
        Robot robot = new Robot();
        robot.putInRoom(room);

        robot.setProgram("RRFFFRFRF");
        assertEquals("1 3 " + N, robot.moveUntilEnd().toString());
    }

}
