package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.engine.Language;
import se.kjellstrand.robot.engine.Robot;
import android.app.Application;

public class RobotApplication extends Application {

    private static Robot sRobot = new Robot(Language.SWEDISH);
    
    public static Robot getRobot() {
        return sRobot;
    }

    public static void setRobot(Robot robot) {
        sRobot = robot;
    }

}
