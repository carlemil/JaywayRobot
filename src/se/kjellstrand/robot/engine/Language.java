package se.kjellstrand.robot.engine;

/**
 * Enum of the different languages the Robot can accept programs in.
 * 
 */
public enum Language {
    SWEDISH, ENGLISH, ARROWS;

    /**
     * Returns the char used to display "Forward" in the selected language.
     * 
     * @param language
     * 
     * @return char used for displaying a forward command.
     */
    public static char getForwardChar(Language language) {
        switch (language) {
            case SWEDISH:
                return 'G';
            case ARROWS:
                return '\u2191';
            default:
                return 'F';
        }
    }

    /**
     * Returns the char used to display "Left" in the selected language.
     * 
     * @return char used for displaying a left command.
     */
    public static char getLeftChar(Language language) {
        switch (language) {
            case SWEDISH:
                return 'V';
            case ARROWS:
                return '\u21b6';
            default:
                return 'L';
        }
    }

    /**
     * Returns the char used to display "Right" in the selected language.
     * 
     * @return char used for displaying a right command.
     */
    public static char getRightChar(Language language) {
        switch (language) {
            case SWEDISH:
                return 'H';
            case ARROWS:
                return '\u21b7';
            default:
                return 'R';
        }
    }
}
