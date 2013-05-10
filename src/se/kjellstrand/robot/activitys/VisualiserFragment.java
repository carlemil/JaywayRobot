package se.kjellstrand.robot.activitys;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.views.RobotRoomView;
import android.app.Fragment;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment responsible for showing a control panel for the Robot to the user.
 * The user can input command sequences/programs to the robot from here, and
 * execute the same.
 * 
 */
public class VisualiserFragment extends Fragment {

    /**
     * Tag used to enable easy filtering in logcat.
     */
    protected static final String TAG = VisualiserFragment.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.visualiser, null);
    }

    /**
     * Updates the drawing of the room and the path used by the robot to move
     * through the room.
     * 
     * @param robotPathPoints a array for points, showing where the robot moved.
     * @param roomWallPoints a array of points, making up a polygon of the walls
     *        of the room.
     */
    public void updateRobotAndRoom(Point[] robotPathPoints, Point[] roomWallPoints) {
        RobotRoomView robotRoomView = (RobotRoomView) getView().findViewById(R.id.robot_room_view);

        int leftWall = Integer.MAX_VALUE, topWall = Integer.MAX_VALUE, rightWall = 0, bottomWall = 0;

        // A room with < 3 walls is no room.
        if (roomWallPoints.length >= 3) {
            Path walls = new Path();
            walls.moveTo(roomWallPoints[roomWallPoints.length - 1].x,
                    roomWallPoints[roomWallPoints.length - 1].y);
            for (Point p : roomWallPoints) {
                walls.lineTo(p.x, p.y);
                // Store the bounding box of the room, used to scale up the room
                // and robot path to the screen.
                leftWall = Math.min(leftWall, p.x);
                topWall = Math.min(topWall, p.y);
                rightWall = Math.max(rightWall, p.x);
                bottomWall = Math.max(bottomWall, p.y);
            }

            // Important to define the viewport before adding walls. Scaling and
            // translating will not be correct if the order is reversed.
            robotRoomView.defineViewPort(leftWall, topWall, rightWall, bottomWall, 0.5f);

            robotRoomView.setWalls(walls);
        }

        // Draw the path of the robot
        Path robotPath = new Path();
        robotPath.moveTo(robotPathPoints[0].x,
                bottomWall - robotPathPoints[0].y);

        for (Point p : robotPathPoints) {
            // TODO Change to quadTo
            robotPath.lineTo(p.x, bottomWall - p.y);
        }

        robotRoomView.setRobotPath(robotPath);

        robotRoomView.invalidate();
    }

}
