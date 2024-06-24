package ClientSide.entities;

import commInfra.*;
import ClientSide.main.SimulPar;
import ClientSide.stubs.*;

/**
 *    Referee thread.
 *
 *      It simulates the Referee life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public class Refree extends Thread {

    /**
     * Refree state.
     */
    private int refreeState;

    /**
     * Reference to the play ground.
     */

    private final PlayGroundStub plGrnd;

    /**
     * Reference to the Refree Site.
     */

    private RefreeSiteStub site;

    /**
     * Reference to the Contestant bench.
     */

    private contestents_bench_stub bench;

    int matches_per_game = 0;

    int matches_done = 0;

    /**
     * Instantiation of Refree thread.
     *
     * @param name       thread name
     * @param site       reference to the refree site
     * @param playGround reference to the play groun
     * @param bench      reference to the contestant bench
     */

    public Refree(String name, RefreeSiteStub site, PlayGroundStub playGround, contestents_bench_stub bench) {
        super(name);
        refreeState = RefreeStates.STARTOFTHEMATCH;
        plGrnd = playGround;
        this.site = site;
        this.bench = bench;
        // this.matches_per_game = matches_per_game; não é preciso
    }

    /**
     * Set Refree state.
     *
     * @param state new Refree state
     */

    public void setRefreeState(int state) {
        refreeState = state;
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
     * Get Refree state.
     *
     * @return Refree state
     */

    public int getRefreeState() {
        return refreeState;
    }

    // private final Playing_Area play;

    public void add_one() {

        this.matches_done++;

    }

    public boolean are_done() {

        if (this.matches_done == matches_per_game) {

            return true;

        } else {

            return false;

        }

    }

    @Override
    public void run() {

        for (int n = 0; n < 3; n++) {
            site.anounceNewGame((n + 1));
            plGrnd.resetScore () ;
            boolean assertDecision = false;
            int NB = 0;
            int winTeam = 0;
            do {

                NB++;
                int PS = plGrnd.getPositionCenterRope ();
                site.callTrial(NB,PS);
                bench.wakeUpCoach();        // wake up the Coach after a callTrial()
                site.sleepAfterCallTrial (); //Sleep after wake up the coach in call Trial;
                //plGrnd.callTrial();
                plGrnd.startTrial();
                bench.wakeUpContestant(); // wake up the Contestant after a startTrial()
                plGrnd.timeToSleap();

                winTeam = plGrnd.assertTriallDecision();
                site.assertTriallDecision();

                assertDecision = plGrnd.getTrialDecision();

            } while (!assertDecision && NB < 6);
            site.declareGameWinner(winTeam);
        }
        plGrnd.declareMatchWinner();
        site.declareMatchWinner();
        plGrnd.endOfMatch(3);
    }

}