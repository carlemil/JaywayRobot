package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * The main activity holding the control panel and visualiser fragment.
 * 
 */
public class MainActivity extends Activity implements RobotResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void result(Point[] robotPath, Point[] room) {
        VisualiserFragment visualiserFragment = (VisualiserFragment) getFragmentManager().findFragmentById(
                R.id.visualiser_fragment);
        visualiserFragment.setRobotAndRoom(robotPath, room);
    }

}
