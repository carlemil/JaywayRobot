package se.kjellstrand.robot.test;

import android.graphics.Point;
import android.graphics.Rect;
import se.kjellstrand.robotconstructionkit.Language;
import se.kjellstrand.robotconstructionkit.Rect2DRoom;
import se.kjellstrand.robotconstructionkit.Robot;
import se.kjellstrand.robotconstructionkit.Room;
import junit.framework.TestCase;

public class BasicRobotTest extends TestCase {

    private static final String N = "NORTH";

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

        robot.setInstructions("G");
        // Still a 0,0 size room
        assertEquals("0 0 "+N, robot.move());

        // create a room 5x5, and put the robot in it.
        dim = new Rect(0, 0, 5, 5);
        room = new Rect2DRoom(dim, startPos);
        robot.putInRoom(room);
        assertEquals("0 1 "+N, robot.move());
        
    }
    
}
