package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.RobotLocation;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * The main activity holding the control panel and visualiser fragment.
 * 
 */
public class MainActivity extends FragmentActivity implements RobotResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
    }

    @Override
    public void result(RobotLocation[] robotPath, Point[] room) {
        VisualiserFragment visualiserFragment = (VisualiserFragment) getSupportFragmentManager().findFragmentById(
                R.id.frag_2);
        visualiserFragment.setRobotAndRoom(robotPath, room);
    }

}
