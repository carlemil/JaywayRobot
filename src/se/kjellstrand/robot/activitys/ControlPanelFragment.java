package se.kjellstrand.robot.activitys;

import java.util.ArrayList;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.BoundingBoxRoom;
import se.kjellstrand.robot.engine.CircularRoom;
import se.kjellstrand.robot.engine.Language;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.RobotLocation;
import se.kjellstrand.robot.settings.RobotSharedPreferences;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment responsible for showing a control panel for the Robot to the user.
 * The user can input programs to the robot from here, and execute the same.
 * 
 */
public class ControlPanelFragment extends Fragment {

    /**
     * Tag used to enable easy filtering in logcat.
     */
    protected static final String TAG = ControlPanelFragment.class.getCanonicalName();

    /**
     * The ROBOT !!!
     */
    private Robot mRobot;

    /**
     * Class listening for results from our robot.
     */
    private RobotRunResultListener mResultListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mResultListener = (RobotRunResultListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_panel, null);

        Language language = RobotSharedPreferences.getLanguage(getActivity());
        mRobot = new Robot(language);

        StringBuilder program;
        if (savedInstanceState != null) {
            // populate from savedInstanceState
            program = new StringBuilder(savedInstanceState.getString(RobotSharedPreferences.PROGRAM_KEY));
            Log.d(TAG, "Read program from savedInstanceState: " + program);
        } else {
            // Populate from sharedPrefs
            program = RobotSharedPreferences.getProgram(getActivity());
        }
        mRobot.resetProgram(program);

        String resString = getString(R.string.halting_position_of_robot,
                getActivity().getString(R.string.unknown_position));
        ((TextView) view.findViewById(R.id.robot_run_result)).setText(resString);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();

        Language language = RobotSharedPreferences.getLanguage(getActivity());
        if (language != mRobot.getLanguage()) {
            Log.d(TAG, "New language set: " + language);
            mRobot.setLanguage(language);
        }
        showCurrentProgram();

        setComandButtonClickListener(view.findViewById(R.id.button_left), mRobot.mLeftChar);
        setComandButtonClickListener(view.findViewById(R.id.button_right), mRobot.mRightChar);
        setComandButtonClickListener(view.findViewById(R.id.button_forward), mRobot.mForwardChar);
        setDeleteButtonClickListener(view.findViewById(R.id.button_delete));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runRobotAndUpdateVisualisation();
            }
        }, 500);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(RobotSharedPreferences.PROGRAM_KEY, mRobot.getProgram().toString());
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mRobot.getProgram() != null) {
            Log.d(TAG, "Write program to shared prefs: " + mRobot.getProgram());
            RobotSharedPreferences.putProgram(getActivity(), mRobot.getProgram().toString());
        }
    }

    /**
     * Clear the robots program, and clears the program stored in the shared
     * prefs.
     * 
     */
    public void resetRobotProgram() {
        mRobot.resetProgram(null);
        RobotSharedPreferences.putProgram(getActivity(), null);
        showCurrentProgram();
        runRobotAndUpdateVisualisation();
    }

    private void setComandButtonClickListener(View view, final char c) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mRobot.getProgram().append(c);
                showCurrentProgram();
                runRobotAndUpdateVisualisation();
            }
        });
    }

    private void setDeleteButtonClickListener(View button) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder p = mRobot.getProgram();
                int l = p.length();
                if (l > 0) {
                    p.deleteCharAt(p.length() - 1);
                }
                showCurrentProgram();
                runRobotAndUpdateVisualisation();
            }
        });
    }

    private void runRobotAndUpdateVisualisation() {
        // Set the resulting state to unknown until the run completes.
        setResultString(getString(R.string.unknown_position));

        // Read room data from shared prefs.
        int startX = RobotSharedPreferences.getRobotStartX(getActivity());
        int startY = RobotSharedPreferences.getRobotStartY(getActivity());
        int roomWidth = RobotSharedPreferences.getRoomWidth(getActivity());
        int roomLength = RobotSharedPreferences.getRoomLength(getActivity());

        // Create new room
        BoundingBoxRoom room;
        switch (RobotSharedPreferences.getRoomShape(getActivity())) {
            case R.string.circular:
                room = new CircularRoom(roomWidth, new Point(startX, startY));
                break;

            default:
                room = new Rect2DRoom(roomWidth, roomLength, new Point(startX, startY));
                break;
        }
        mRobot.putInRoom(room);

        ArrayList<Point> robotPath = new ArrayList<Point>();
        // Add the start position of the robot to the list of positions that the
        // robot have visited.
        robotPath.add(mRobot.getRoom().getStartPosition());

        RobotLocation res = null;
        // Run the program to the end. Save the intermediate locations
        // for visualising the path.
        while (mRobot.hasMoreMoves()) {
            res = mRobot.move();
            robotPath.add(res.getPosition());
        }

        // Show the resulting state
        if (res != null) {
            setResultString(res.toString(mRobot.getLanguage()));
        }

        // Let the result listener know that there is new data to display.
        mResultListener.robotRunResultReceived(robotPath.toArray(new Point[robotPath.size()]),
                mRobot.getRoom(), mRobot.getRobotPosition());
    }

    private void setResultString(String result) {
        final TextView resultTextView = (TextView) getView().findViewById(R.id.robot_run_result);
        String resString = getString(R.string.halting_position_of_robot, result);
        resultTextView.setText(resString);
    }

    private void showCurrentProgram() {
        TextView textView = (TextView) getView().findViewById(R.id.text_view_program);
        textView.setText(mRobot.getProgram().toString());
    }

}
