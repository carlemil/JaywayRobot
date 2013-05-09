package se.kjellstrand.robot.gui;

import java.util.ArrayList;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.Language;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.RobotLocation;
import se.kjellstrand.robot.engine.RoomWithWalls;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
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

    private RobotResultListener mResultListener;

    /**
     * Default language is English.
     */
    private Language mLanguage;

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
            mResultListener = (RobotResultListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.control_panel, null);

        mLanguage = RobotSharedPreferences.getLanguage(getActivity());
        Log.d(TAG, "Language: " + mLanguage);
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

        final EditText programEditText = (EditText) view.findViewById(R.id.edit_text_program);
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

        Language language = RobotSharedPreferences.getLanguage(getActivity());
        if (language != mLanguage) {
            Log.d(TAG, "New language set: " + language);
            setLanguage(language);
            // Reset our program since the language changed.
            mProgram = new StringBuilder();
            showCurrentProgram();
        }

        setComandButtonClickListener(view.findViewById(R.id.button_left), mLeftChar);
        setComandButtonClickListener(view.findViewById(R.id.button_right), mRightChar);
        setComandButtonClickListener(view.findViewById(R.id.button_forward), mForwardChar);

        setDeleteButtonClickListener(view.findViewById(R.id.button_delete));

        setPlayButtonClickListener(view.findViewById(R.id.button_play),
                (TextView) view.findViewById(R.id.robot_run_result));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(RobotSharedPreferences.PROGRAM_KEY, mProgram.toString());
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "Write program to shared prefs: " + mProgram);
        if (mProgram != null) {
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
                EditText edittext = (EditText) getView().findViewById(R.id.edit_text_program);
                edittext.setText(mProgram);
            }
        });
    }

    private void setLanguage(Language language) {
        mLanguage = language;
        mForwardChar = Language.getForwardChar(language);
        mLeftChar = Language.getLeftChar(language);
        mRightChar = Language.getRightChar(language);
    }

    private void setPlayButtonClickListener(View button, final TextView resultTextView) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Robot robot = new Robot(mLanguage);
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

                mResultListener.result(robotPath.toArray(new Point[robotPath.size()]), room.getWalls());
            }
        });
    }

    private void showCurrentProgram() {
        EditText edittext = (EditText) getView().findViewById(R.id.edit_text_program);
        edittext.setText(mProgram.toString());
    }

}
