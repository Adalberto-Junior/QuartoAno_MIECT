package ClientSide.entities;
import ClientSide.stubs.*;
/**
 *    Contestant cloning.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */


public interface ContestantCloning {
     /**
     * Set Contestant state.
     *
     * @param state new Contestant state
     */

     public void setContestantState(int state);

    /**
     * Set Contestant Team ID.
     *
     * @param teamId Contestant Team ID
     */

    public void setContestantTeamId(int teamId);

    /**
     * Set Contestant ID.
     *
     * @param contID Contestant ID
     */

    public void setContestantID(int contID);

    /**
     * Set Contestant Strength.
     *
     * @param SG Contestant Strength
     */

    public void setContestantSG(int SG);

    /*
     * Set new Reference to the contestant bench.
     *
     * @param bench Contestant Strength
     */

    //public void setContestantBench(contestents_bench_stub bench);

    /**
     * Get Contestant state.
     *
     * @return Contestant state
     */

    public int getContestantState();

    /**
     * Get Contestant Team Id.
     *
     * @return Contestant Team Id
     */

    public int getContestantTeamId();

    /**
     * Get Contestant Id.
     *
     * @return Contestant Id
     */

    public int getContestantID();

    /**
     * Get Contestant Strength.
     *
     * @return Contestant Strength
     */

    public int getContestantSG();


    /**
     * Add Contestant Strength.
     *
     */
    public void addStreangth();

    /**
     * Sub Contestant Strength.
     *
     */
    public void subStreangth();

}
