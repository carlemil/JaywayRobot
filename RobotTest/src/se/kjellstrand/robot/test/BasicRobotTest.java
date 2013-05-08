package se.kjellstrand.robot.test;

import android.graphics.Point;
import android.graphics.Rect;
import se.kjellstrand.robotconstructionkit.Language;
import se.kjellstrand.robotconstructionkit.Rect2DRoom;
import se.kjellstrand.robotconstructionkit.Robot;
import se.kjellstrand.robotconstructionkit.Room;
import junit.framework.TestCase;

public class BasicRobotTest extends TestCase {

    private static final String N = "N";
    private static final String W = "W";
    private static final String E = "E";
    private static final String S = "S";


    public BasicRobotTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBasicRobotMoves(){
        Rect dim = new Rect(0, 0, 0, 0);
        Point startPos = new Point(0, 0);
        Room room = new Rect2DRoom(dim, startPos);
        Robot robot = new Robot(Language.SWEDISH);
        
        assertEquals(null, robot.move());

        robot.putInRoom(room);
        assertEquals(null, robot.move());

        // set instructions to, "one step forward".
        robot.setInstructions("G");
        // Still a 0,0 size room
        assertEquals("0 0 "+N, robot.move());

        // create a room 5x5, and put the robot in it.
        dim = new Rect(0, 0, 5, 5);
        room = new Rect2DRoom(dim, startPos);
        robot.putInRoom(room);
        assertEquals("0 1 "+N, robot.move());

        // set instructions to, "left".
        robot.setInstructions("V");
        assertEquals("0 1 "+W, robot.move());

        // set instructions to, "right".
        robot.setInstructions("H");
        assertEquals("0 1 "+N, robot.move());

        // set instructions to, "right".
        robot.setInstructions("H");
        assertEquals("0 1 "+E, robot.move());
        
    }

    public void testRobotMovesCommands1(){
        Rect dim = new Rect(0, 0, 5, 5);
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(dim, startPos);
        Robot robot = new Robot(Language.SWEDISH);
        robot.putInRoom(room);
              
        robot.setInstructions("GHGGVGVVGG");
        assertEquals("3 1 "+S, robot.moveUntilEnd());
    }

    public void testRobotMovesCommands2(){
        Rect dim = new Rect(0, 0, 5, 5);
        Point startPos = new Point(1, 2);
        Room room = new Rect2DRoom(dim, startPos);
        Robot robot = new Robot(Language.SWEDISH);
        robot.putInRoom(room);
              
        robot.setInstructions("HGHGGHGHG");
        assertEquals("1 1 "+N, robot.moveUntilEnd());
    }

    public void testRobotMovesCommandsRoomBoundingBox1(){
        Rect dim = new Rect(0, 0, 3, 3);
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(dim, startPos);
        Robot robot = new Robot(Language.SWEDISH);
        robot.putInRoom(room);
              
        robot.setInstructions("GGG");
        assertEquals("1 2 "+N, robot.moveUntilEnd());
    }
    
    public void testRobotMovesCommandsRoomBoundingBox2(){
        Rect dim = new Rect(0, 0, 3, 3);
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(dim, startPos);
        Robot robot = new Robot(Language.SWEDISH);
        robot.putInRoom(room);
              
        robot.setInstructions("HGGG");
        assertEquals("2 1 "+E, robot.moveUntilEnd());
    }
    
    public void testRobotMovesCommandsRoomBoundingBox3(){
        Rect dim = new Rect(0, 0, 3, 3);
        Point startPos = new Point(1, 1);
        Room room = new Rect2DRoom(dim, startPos);
        Robot robot = new Robot(Language.SWEDISH);
        robot.putInRoom(room);
              
        robot.setInstructions("HHGGGHGGG");
        assertEquals("0 0 "+W, robot.moveUntilEnd());
    }
    
}
