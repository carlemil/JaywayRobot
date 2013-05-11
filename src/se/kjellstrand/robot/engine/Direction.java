package se.kjellstrand.robot.engine;

/**
 * Enum of the different directions the Robot may travel in.
 * 
 */
public enum Direction {
    NORTH, WEST, SOUTH, EAST;

    /**
     * Returns the char used to display a direction in language.
     * 
     * @param direction The direction to get char for.
     * @param language The language to get a direction char for.
     * 
     * @return char used for displaying the direction in language.
     */
    public static char getChar(Direction direction, Language language) {
        switch (language) {
            case SWEDISH:
                switch (direction) {
                    case NORTH:
                        return 'N';
                    case WEST:
                        return 'V';
                    case SOUTH:
                        return 'S';
                    case EAST:
                        return 'Ö';
                }
            case ENGLISH:
                switch (direction) {
                    case NORTH:
                        return 'N';
                    case WEST:
                        return 'W';
                    case SOUTH:
                        return 'S';
                    case EAST:
                        return 'E';
                }
            case ARROWS:
                switch (direction) {
                    case NORTH:
                        return '\u2191';
                    case WEST:
                        return '\u2190';
                    case SOUTH:
                        return '\u2193';
                    case EAST:
                        return '\u2192';
                }
        }
        return ' ';
    }
}
