package se.kjellstrand.robotconstructionkit;

public interface Room {
    android.graphics.Point getStartPosition();

    boolean contains(android.graphics.Point position);
}
