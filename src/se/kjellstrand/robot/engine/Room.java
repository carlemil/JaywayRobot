package se.kjellstrand.robot.engine;

public interface Room {
    boolean contains(android.graphics.Point position);

    android.graphics.Point getStartPosition();
}
