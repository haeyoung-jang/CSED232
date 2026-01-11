package edu.postech.csed232;

/**
 * A rabbit in the hopping rabbits game, where EMPTY represents an empty cell.
 */
public enum Rabbit {
    X, O, EMPTY;

    public Rabbit other() {
        return switch (this) {
            case X -> O;
            case O -> X;
            default -> EMPTY;
        };
    }
}
