package se.kjellstrand.robot.engine;

public interface Room {
    android.graphics.Point getStartPosition();

    boolean contains(android.graphics.Point position);
}
