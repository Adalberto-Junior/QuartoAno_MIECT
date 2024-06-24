package ClientSide.entities;

import commInfra.*;
import ClientSide.main.SimulPar;
import ClientSide.stubs.*;

/**
 *    Coach thread.
 *
 *      It simulates the coach life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public class Coach extends Thread {

    /**
     * Coach state.
     */
    private int coachState;

    /**
     * Coach team.
     */
    private int teamId;

    /**
     * Reference to the play ground.
     */

    private PlayGroundStub plGrnd;

    /**
     * Reference to the Refree Site.
     */

    private RefreeSiteStub site;

    /**
     * Reference to the Contestant bench.
     */

    private contestents_bench_stub bench;


    /**
     * Instantiation of a coach thread.
     *
     * @param name       thread name
     * @param teamId     coach´s team (will also be used as Coach Id)
     * @param site       reference to the refree site
     * @param playGround reference to the play groun
     * @param bench      reference to the contestants's bench
     */

    public Coach(String name, int teamId, RefreeSiteStub site, PlayGroundStub playGround, contestents_bench_stub bench) {

        // this.stratagy = new Start(p, stratagy);

        // this.p = p;
        super(name);
        this.teamId = teamId;
        this.site = site;
        this.bench = bench;
        this.plGrnd = playGround;
        coachState = CoachStates.WAITFORREFEREECOMMAND;

    }

    /**
     * Set Coach state.
     *
     * @param state new Coach state
     */

    public void setCoachState(int state) {
        coachState = state;
    }

    /**
     * Set Coach Team Id.
     *
     * @param teamId new Refree state
     */

    public void setCoachTeamId(int teamId) {
        this.teamId = teamId;
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
     * Get Coach state.
     *
     * @return Coach state
     */

    public int getCoachState() {
        return coachState;
    }

    /**
     * Get Coach team Id.
     *
     * @return TeamId
     */

    public int getCoachTeamId() {
        return teamId;
    }

    
    @Override
    public void run() {
        boolean endOfMatch = false;

        while (!endOfMatch) {
            bench.reviewNotes();
            bench.callContestants();
            site.infomeRefree();
            endOfMatch = plGrnd.getEndOfMatch();
        }

    }

    private void sleep() {
        try {
            sleep((long) (1 + 100 * Math.random()));
        } catch (InterruptedException e) {
        }
    }

}