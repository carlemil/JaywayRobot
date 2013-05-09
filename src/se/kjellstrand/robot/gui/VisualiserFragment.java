package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.RobotLocation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment responsible for showing a control panel for the Robot to the user.
 * The user can input command sequences/programs to the robot from here, and
 * execute the same.
 * 
 */
public class VisualiserFragment extends Fragment {

    protected static final String TAG = VisualiserFragment.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.visualiser, null);
    }

    public void setRobotPath(RobotLocation[] robotPath) {

    }

    public void setRoom(Point[] room) {

        RobotRoomView rrv = (RobotRoomView) getView().findViewById(R.id.robot_room_view);

        // A room with < 3 walls is no room.
        if (room.length >= 3) {
            Path walls = new Path();
            walls.moveTo(room[0].x, room[0].y);
            int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE, maxx = 0, maxy = 0;
            for (Point p : room) {
                walls.lineTo(p.x, p.y);
                // Store the bounding box of the room, used to scale up the room
                // and robot path to the screen.
                minx = Math.min(minx, p.x);
                miny = Math.min(miny, p.y);
                maxx = Math.max(maxx, p.x);
                maxy = Math.max(maxy, p.y);
            }
            // The last line (or was it the carpet?) that ties the room together. 
            walls.lineTo(room[0].x, room[0].y);

            // Important to define the viewport before adding walls. Scaling and
            // translating will not be correct if the order is reversed.
            rrv.defineViewPort(minx, miny, maxx, maxy, 1);

            rrv.setWalls(walls);
            rrv.invalidate();
        }

    }

}
