package ClientSide.entities;

/**
 * Definition of the internal states of the contestants during his life cycle.
 */

public final class ContestantStates {
    /**
     * The Contestants are waiting to be selected for next trial.
     */

    public static final int SEATATTHEBENCH = 9;

    /**
     * The Contestants are waiting for the Start Trial.
     */

    public static final int STANDINPOSITION = 10;

    /**
     * The Contestants are pulling the rope.
     */

    public static final int DOYOURBEST = 11;

    /**
     * It can not be instantiated.
     */

    private ContestantStates() {
    }

}
