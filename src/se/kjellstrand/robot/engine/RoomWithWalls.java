package se.kjellstrand.robot.engine;

public interface RoomWithWalls extends Room {
    android.graphics.Point[] getWalls();
}
