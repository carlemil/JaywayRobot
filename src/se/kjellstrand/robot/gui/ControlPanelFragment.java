package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.RobotLocation;
import se.kjellstrand.robot.engine.Room;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.control_panel, null);
        final EditText programEditText = (EditText) view.findViewById(R.id.edit_text_program);

        CharSequence program = null;
        
        if (savedInstanceState != null) {
            // populate from savedInstanceState
            program = savedInstanceState.getString(RobotSharedPreferences.PROGRAM_KEY);
            Log.d(TAG, "Read program from savedInstanceState: " + program);
        } else {
            // Populate from sharedPrefs
            program = RobotSharedPreferences.getProgram(getActivity());
            Log.d(TAG, "Read program from RobotSharedPreferences: " + program);
        }
//        if(program!=null){
        programEditText.setText(program);
        //}
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Robot robot = RobotApplication.getRobot();
        View view = getView();

        final EditText programEditText = (EditText) view.findViewById(R.id.edit_text_program);

        setComandButtonClickListener(programEditText, view.findViewById(R.id.button_left), robot
                .getLeftChar());
        setComandButtonClickListener(programEditText, view.findViewById(R.id.button_right), robot.getRightChar());
        setComandButtonClickListener(programEditText, view.findViewById(R.id.button_forward), robot.getForwardChar());

        setDeleteButtonClickListener(programEditText, view.findViewById(R.id.button_delete));

        setPlayButtonClickListener(programEditText, view.findViewById(R.id.button_play),
                (TextView) view.findViewById(R.id.robot_run_result));
    }

    @Override
    public void onStop() {
        super.onStop();
        View view = getView();
        final EditText programEditText = (EditText) view.findViewById(R.id.edit_text_program);

        String program = programEditText.getText().toString();
        Log.d(TAG, "Write program to shared prefs: " + program);
        if (program != null) {
            RobotSharedPreferences.putProgram(getActivity(), program);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final EditText programEditText = (EditText) getView().findViewById(R.id.edit_text_program);
        String program = programEditText.getText().toString();
        outState.putString(RobotSharedPreferences.PROGRAM_KEY, program);
    }

    private void setComandButtonClickListener(final EditText program, View view, final char c) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                program.getText().append(c);
            }
        });
    }

    private void setDeleteButtonClickListener(final EditText program, View view) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Editable text = program.getText();
                int l = text.length();
                if (l > 0) {
                    text.delete(l - 1, l);
                }
            }
        });
    }

    private void setPlayButtonClickListener(final EditText program, View view, final TextView resultTextView) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Editable instructions = program.getText();
                Robot r = RobotApplication.getRobot();
                r.setProgram(instructions.toString());
                // TODO, config and not HARDCODED.
                Rect dim = new Rect(0, 0, 5, 5);
                Point pos = new Point(1, 2);

                Room room = new Rect2DRoom(dim, pos);
                r.putInRoom(room);

                // Run the program to the end.
                RobotLocation res = r.moveUntilEnd();

                // Show the resulting state
                String resString = getString(R.string.halting_position_of_robot, res.toString());
                resultTextView.setText(resString);
            }
        });
    }

}
