package ClientSide.entities;

/**
 * Definition of the internal states of the referee during his life cycle.
 */

public final class RefreeStates {
    /**
     * The initial state (transition) the referee will annouce new Game.
     */

    public static final int STARTOFTHEMATCH = 0;

    /**
     * Transition state.
     */

    public static final int STARTOFAGAME = 1;

    /**
     * The referee waits for the last coach to announce that the team is ready.
     */

    public static final int TEAMSREADY = 2;

    /**
     * The referee is waiting for the trial to end.
     */

    public static final int WAITFORTRIALCONCLUSION = 3;

    /**
     * The Referee will announce the new game or declare match the winner.
     */

    public static final int ENDOFAGAME = 4;

    /**
     * The Referee finish the match.
     */

    public static final int ENDOFTHEMATCH = 5;

    /**
     * It can not be instantiated.
     */

    private RefreeStates() {
    }

}