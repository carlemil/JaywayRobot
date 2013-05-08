package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    protected static final String TAG = VisualiserFragment.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.visualiser, null);
    }

}
