package se.kjellstrand.robot.activitys;

import java.util.ArrayList;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.Language;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.RobotLocation;
import se.kjellstrand.robot.settings.RobotSharedPreferences;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
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
     * A StringBuilder that holds the current program. Used to run the robot.
     */
    private StringBuilder mProgram = new StringBuilder();

    /**
     * Class listening for results from our robot.
     */
    private RobotRunResultListener mResultListener;

    /**
     * Default language is English.
     */
    private Language mLanguage = Language.ENGLISH;

    /**
     * Language specific char used to denote a forward command.
     */
    private char mForwardChar;

    /**
     * Language specific char used to denote a left command.
     */
    private char mLeftChar;

    /**
     * Language specific char used to denote a right command.
     */
    private char mRightChar;

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

        mLanguage = RobotSharedPreferences.getLanguage(getActivity());
        setLanguage(mLanguage);

        if (savedInstanceState != null) {
            // populate from savedInstanceState
            mProgram = new StringBuilder(savedInstanceState.getString(RobotSharedPreferences.PROGRAM_KEY));
            Log.d(TAG, "Read program from savedInstanceState: " + mProgram);
        } else {
            // Populate from sharedPrefs
            String program = RobotSharedPreferences.getProgram(getActivity());
            if (program != null) {
                mProgram.append(program);
            }
        }

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
        if (language != mLanguage) {
            Log.d(TAG, "New language set: " + language);
            setLanguage(language);
            // Reset our program since the language changed.
            mProgram = new StringBuilder();
        }
        showCurrentProgram();

        setComandButtonClickListener(view.findViewById(R.id.button_left), mLeftChar);
        setComandButtonClickListener(view.findViewById(R.id.button_right), mRightChar);
        setComandButtonClickListener(view.findViewById(R.id.button_forward), mForwardChar);
        setDeleteButtonClickListener(view.findViewById(R.id.button_delete));
        setPlayButtonClickListener(view.findViewById(R.id.button_play));

        runRobotAndUpdateVisualisation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(RobotSharedPreferences.PROGRAM_KEY, mProgram.toString());
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mProgram != null) {
            Log.d(TAG, "Write program to shared prefs: " + mProgram);
            RobotSharedPreferences.putProgram(getActivity(), mProgram.toString());
        }
    }

    private void setComandButtonClickListener(View view, final char c) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgram.append(c);
                showCurrentProgram();
            }
        });
    }

    private void setDeleteButtonClickListener(View button) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int l = mProgram.length();
                if (l > 0) {
                    mProgram.deleteCharAt(mProgram.length() - 1);
                }
                TextView textView = (TextView) getView().findViewById(R.id.text_view_program);
                textView.setText(mProgram);
            }
        });
    }

    private void setLanguage(Language language) {
        mLanguage = language;
        mForwardChar = Language.getForwardChar(language);
        mLeftChar = Language.getLeftChar(language);
        mRightChar = Language.getRightChar(language);
    }

    private void setPlayButtonClickListener(View button) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                runRobotAndUpdateVisualisation();
            }
        });
    }

    private void runRobotAndUpdateVisualisation() {
        Robot robot = new Robot(mLanguage);

        // Read room data from shared prefs.
        int startX = RobotSharedPreferences.getRobotStartX(getActivity());
        int startY = RobotSharedPreferences.getRobotStartY(getActivity());
        int roomWidth = RobotSharedPreferences.getRoomWidth(getActivity());
        int roomLength = RobotSharedPreferences.getRoomLength(getActivity());

        // Create new room
        Rect2DRoom room = new Rect2DRoom(roomWidth, roomLength, new Point(startX, startY));
        robot.putInRoom(room);
        robot.setProgram(mProgram.toString());

        ArrayList<Point> robotPath = new ArrayList<Point>();
        // Add the start position of the robot to the list of positions that the
        // robot have visited.
        robotPath.add(robot.getRoom().getStartPosition());

        RobotLocation res = null;
        // Run the program to the end. Save the intermediate locations
        // for visualising the path.
        while (robot.hasMoreMoves()) {
            res = robot.move();
            robotPath.add(res.getPosition());
        }

        // Show the resulting state
        if (res != null) {
            String resString = getString(R.string.halting_position_of_robot, res.toString(mLanguage));
            final TextView resultTextView = (TextView) getView().findViewById(R.id.robot_run_result);
            resultTextView.setText(resString);
        }

        // Let the result listener know that there is new data to display.
        mResultListener.robotRunResultReceived(robotPath.toArray(new Point[robotPath.size()]), robot.getRoom()
                .getWalls());
    }

    private void showCurrentProgram() {
        TextView textView = (TextView) getView().findViewById(R.id.text_view_program);
        textView.setText(mProgram.toString());
    }

}
