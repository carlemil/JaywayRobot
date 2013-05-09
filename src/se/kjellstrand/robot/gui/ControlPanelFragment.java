package se.kjellstrand.robot.gui;

import java.util.ArrayList;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.RobotLocation;
import se.kjellstrand.robot.engine.Room;
import se.kjellstrand.robot.engine.RoomWithWalls;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Fragment responsible for showing a control panel for the Robot to the user.
 * The user can input programs to the robot from here, and execute the same.
 * 
 */
public class ControlPanelFragment extends Fragment {

    protected static final String TAG = ControlPanelFragment.class.getCanonicalName();

    private StringBuilder mProgram = new StringBuilder();

    private RobotResultListener resultListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            resultListener = (RobotResultListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.control_panel, null);
        final EditText programEditText = (EditText) view.findViewById(R.id.edit_text_program);

        if (savedInstanceState != null) {
            // populate from savedInstanceState
            mProgram = new StringBuilder(savedInstanceState.getString(RobotSharedPreferences.PROGRAM_KEY));
            Log.d(TAG, "Read program from savedInstanceState: " + mProgram);
        } else {
            // Populate from sharedPrefs
            mProgram = new StringBuilder(RobotSharedPreferences.getProgram(getActivity()));
            Log.d(TAG, "Read program from RobotSharedPreferences: " + mProgram);
        }

        programEditText.setText(mProgram);

        String resString = getString(R.string.halting_position_of_robot,
                getActivity().getString(R.string.unknown_position));
        ((TextView) view.findViewById(R.id.robot_run_result)).setText(resString);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();

        setComandButtonClickListener(view.findViewById(R.id.button_left), Robot.TURN_LEFT);
        setComandButtonClickListener(view.findViewById(R.id.button_right), Robot.TURN_RIGHT);
        setComandButtonClickListener(view.findViewById(R.id.button_forward), Robot.MOVE_FORWARD);

        setDeleteButtonClickListener(view.findViewById(R.id.button_delete));

        setPlayButtonClickListener(view.findViewById(R.id.button_play),
                (TextView) view.findViewById(R.id.robot_run_result));
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "Write program to shared prefs: " + mProgram);
        if (mProgram != null) {
            RobotSharedPreferences.putProgram(getActivity(), mProgram.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(RobotSharedPreferences.PROGRAM_KEY, mProgram.toString());
    }

    private void setComandButtonClickListener(View view, final char c) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mProgram.append(c);
                EditText edittext = (EditText) getView().findViewById(R.id.edit_text_program);
                edittext.setText(mProgram.toString());
            }
        });
    }

    private void setDeleteButtonClickListener(View button) {
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int l = mProgram.length();
                if (l > 0) {
                    mProgram.deleteCharAt(mProgram.length() - 1);
                }
                EditText edittext = (EditText) getView().findViewById(R.id.edit_text_program);
                edittext.setText(mProgram);
            }
        });
    }

    private void setPlayButtonClickListener(View button, final TextView resultTextView) {
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Robot robot = new Robot();
                robot.setProgram(mProgram.toString());

                // TODO, config and not HARDCODED.
                RoomWithWalls room = new Rect2DRoom(9, 5, new Point(1, 2));

                robot.putInRoom(room);

                ArrayList<Point> robotPath = new ArrayList<Point>();
                robotPath.add(room.getStartPosition());

                RobotLocation res = null;
                // Run the program to the end. Save the intermediate locations
                // for visualising the path.
                while (robot.hasMoreMoves()) {
                    res = robot.move();
                    robotPath.add(res.getPosition());
                }

                // Show the resulting state
                if (res != null) {
                    String resString = getString(R.string.halting_position_of_robot, res.toString());
                    resultTextView.setText(resString);
                }

                resultListener.result(robotPath.toArray(new Point[robotPath.size()]), room.getWalls());
            }
        });
    }

}
