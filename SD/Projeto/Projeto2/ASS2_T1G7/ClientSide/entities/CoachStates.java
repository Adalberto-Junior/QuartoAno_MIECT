package ClientSide.entities;

/**
 * Definition of the internal states of the coache during his life cycle.
 */

public final class CoachStates {
  /**
   * The coaches are waiting for the referee's command to select the next team.
   */

  public static final int WAITFORREFEREECOMMAND = 6;

  /**
   * The coaches are waiting for the last selected contestants to stand in
   * position.
   */

  public static final int ASSEMBLETEAM = 7;

  /**
   * The coaches are following the trial and waiting for the end.
   */

  public static final int WATCHTRIAL = 8;

  /**
   * It can not be instantiated.
   */

  private CoachStates() {
  }

}
