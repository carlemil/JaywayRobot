package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.Rect2DRoom;
import se.kjellstrand.robot.engine.Robot;
import se.kjellstrand.robot.engine.RobotLocation;
import se.kjellstrand.robot.engine.Room;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class ControlPanelFragment extends Fragment {

    protected static final String TAG = ControlPanelFragment.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_panel, null);

        final EditText program = (EditText) view.findViewById(R.id.edit_text_program);

        setComandButtonClickListener(program, view.findViewById(R.id.button_left), RobotApplication.getRobot()
                .getLeftChar());
        setComandButtonClickListener(program, view.findViewById(R.id.button_right), RobotApplication
                .getRobot().getRightChar());
        setComandButtonClickListener(program, view.findViewById(R.id.button_forward), RobotApplication
                .getRobot().getForwardChar());

        setDeleteButtonClickListener(program, view.findViewById(R.id.button_delete));

        setPlayButtonClickListener(program, view.findViewById(R.id.button_play));

        return view;
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

    private void setPlayButtonClickListener(final EditText program, View view) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Editable instructions = program.getText();
                Robot r = RobotApplication.getRobot();
                r.setInstructions(instructions.toString());
                Rect dim = new Rect(0, 0, 5, 5);
                Point pos = new Point(1, 2);
                Room room = new Rect2DRoom(dim, pos);
                r.putInRoom(room);

                 RobotLocation res = r.moveUntilEnd();
                
                Log.d(TAG, "Result: " + res);
            }
        });
    }

}
