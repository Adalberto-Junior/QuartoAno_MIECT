package ClientSide.entities;

import commInfra.*;
import ClientSide.main.SimulPar;
import ClientSide.stubs.*;


/**
 *    Contestant thread.
 *
 *      It simulates the Contestant life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public class Contestant extends Thread {

    /**
     * Contestant state.
     */
    private int contestantState;

    /**
     * Contestant Team ID.
     */
    private int contestantTeamId;

    /**
     * Contestant ID.
     */
    private int contestantID;

    /**
     * Contestant Strength
     */
    private int contestantSG;

    /**
     * Reference to the play ground.
     */

    private PlayGroundStub plGrnd;

    /**
     * Reference to the contestant Bench.
     */

    private contestents_bench_stub bench;

    // int streangth = 0;

    boolean playing = false;

    /**
     * Instantiation of Contestant thread.
     *
     * @param name       thread name
     * @param id         contestant Id
     * @param teamId     contestant team id
     * @param strength   contestant strength
     * @param bench      reference to the contestant bench
     * @param playGround reference to the play groun
     */
    public Contestant(String name, int id, int teamId, int strength, contestents_bench_stub bench, PlayGroundStub playGround) {
        super(name);
        this.contestantID = id;
        contestantTeamId = teamId;
        this.contestantSG = strength;
        this.bench = bench;
        this.plGrnd = playGround;
        contestantState = ContestantStates.SEATATTHEBENCH;
    }

    /**
     * Set Contestant state.
     *
     * @param state new Contestant state
     */

    public void setContestantState(int state) {
        contestantState = state;
    }

    /**
     * Set Contestant Team ID.
     *
     * @param teamId Contestant Team ID
     */

    public void setContestantTeamId(int teamId) {
        contestantTeamId = teamId;
    }

    /**
     * Set Contestant ID.
     *
     * @param contID Contestant ID
     */

    public void setContestantID(int contID) {
        contestantID = contID;
    }

    /**
     * Set Contestant Strength.
     *
     * @param SG Contestant Strength
     */

    public void setContestantSG(int SG) {
        contestantSG = SG;
    }

    /**
     * Set new Reference to the contestant bench.
     *
     * @param bench Contestant Strength
     */

    public void setContestantBench(contestents_bench_stub bench) {
        this.bench = bench;
    }

    /**
     * Get Contestant state.
     *
     * @return Contestant state
     */

    public int getContestantState() {
        return contestantState;
    }

    /**
     * Get Contestant Team Id.
     *
     * @return Contestant Team Id
     */

    public int getContestantTeamId() {
        return contestantTeamId;
    }

    /**
     * Get Contestant Id.
     *
     * @return Contestant Id
     */

    public int getContestantID() {
        return contestantID;
    }

    /**
     * Get Contestant Strength.
     *
     * @return Contestant Strength
     */

    public int getContestantSG() {
        return contestantSG;
    }

    @Override
    public void run() {

        boolean endOfMatch = false;

        while (!endOfMatch) {
            bench.seatDown();
            bench.followCoachAdvice();
            plGrnd.getRead();
            plGrnd.pullTheRope();
            plGrnd.amDone();
            endOfMatch = plGrnd.getEndOfMatch();
        }

    }

    public void addStreangth() {
        if(this.contestantSG < 10)
             this.contestantSG += 1;
    }

    public void subStreangth() {
        if (this.contestantSG > 6) {
            this.contestantSG -= 1;
        }
    }

}