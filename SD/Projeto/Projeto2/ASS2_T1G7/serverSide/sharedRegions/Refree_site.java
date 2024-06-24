package serverSide.sharedRegions;

import javax.swing.text.Position;


import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import ClientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import serverSide.main.SimulPar;




/**
 * Refree Site.
 *
 * It is responsible to simulate the refree area
 * and is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class Refree_site {

    /**
     * Array of Integer to keep the score of the Game.
     */
    private int[] finalScore;

    /**
     * Variable to wake up the refree when the teams are ready to proceed.
     */
    private boolean infomeRefree;

    /**
     * Variable that indicates whether the trial has ended.
     */
    private boolean assertTriallDecision;

    /**
     * Variable to increment the coach and indicate the last of the 2.
     */
    private int nOfCoach;

   /**
   *   Number of entity groups requesting the shutdown.
   */

   private int nEntities;

    /**
    *   Reference to the stub of the general repository.
    */

   private final GeneralReposStub reposStub;

   /**
     * Reference to the stub of Contestants bench.
     */

     private final contestents_bench_stub benchStub;


   /**
   *   Reference to the stub of the play ground.
   */
   private final PlayGroundStub plGrndStub;

   
   /**
   *  Variable to signal the coach that the match is over.
   */
  private boolean matchIsOver;


    

    /**
     * Refree site instantiation.
     *
     * @param reposStub reference to the stub of general repository
     * @param benchStub reference to the stub of  Contestant bench
     * @param plGrndStub reference to the stub of paly ground
     */

    public Refree_site(GeneralReposStub reposStub, contestents_bench_stub benchStub, PlayGroundStub plGrndStub) {
        this.reposStub = reposStub;
        this.benchStub = benchStub;
        this.plGrndStub = plGrndStub;
        finalScore = new int[SimulPar.T];
        infomeRefree = false;
        assertTriallDecision = false;
        matchIsOver = false;
        nOfCoach = 0;
        nEntities = 0;
    }

    /**
     * Operation callTrial. 
     *
     * It is called by the refree When he start a new Trial.
     * 
     * @param NB Current Trial number;
     * @param PS Position of center of the rope;
     */
    public synchronized void callTrial(int NB,int PS) {
        reposStub.setTrialNumber(NB);

        reposStub.setPositionCentreRope(PS);
        ((RefereeSiteClientProxy) Thread.currentThread()).setRefreeState(RefreeStates.TEAMSREADY);

        reposStub.setRefreeState(((RefereeSiteClientProxy) Thread.currentThread()).getRefreeState());
    }

    /**
     * Operation Sleep after callTrial.
     *
     * It is called by the refree When he finish call trial and wake up the coach.
     * 
     */
    public synchronized void sleepAfterCallTrial() {
        while (!infomeRefree) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }



    /**
     * Operation declareMatchWinner.
     *
     * It is called by the refree When he finish the Match.
     * 
     */
    public synchronized void declareMatchWinner() {
        int champion = 0; // Team's champion

        ((RefereeSiteClientProxy) Thread.currentThread()).setRefreeState(RefreeStates.ENDOFTHEMATCH);
        reposStub.setRefreeState(((RefereeSiteClientProxy) Thread.currentThread()).getRefreeState());
        reposStub.setScore(finalScore);
        if (finalScore[0] > finalScore[1])
            champion = 1;
        else
            champion = 2;

        reposStub.setChampion(champion);
        matchIsOver = true;
        notifyAll();

    }

    /**
     * Operation declareGameWinner.
     *
     * It is called by the refree When he finish the current Game.
     * @param winTeam  Winner Team
     */
    public synchronized void declareGameWinner(int winTeam) {

        ((RefereeSiteClientProxy) Thread.currentThread()).setRefreeState(RefreeStates.ENDOFAGAME);
        reposStub.setRefreeState(((RefereeSiteClientProxy) Thread.currentThread()).getRefreeState());

        if (winTeam == 1)
            finalScore[0] += 1;
        else
            finalScore[1] += 1;
        reposStub.setWinTeam(winTeam);
    }

    /**
     * Operation informe Referee.
     *
     * It is called by the Coach When he finish Assemble the team.
     *
     */
    public synchronized void infomeRefree() {

        ((RefereeSiteClientProxy) Thread.currentThread()).setCoachState(CoachStates.WATCHTRIAL);
        reposStub.setCoachState(((RefereeSiteClientProxy) Thread.currentThread()).getCoachTeamId(),
                                ((RefereeSiteClientProxy) Thread.currentThread()).getCoachState());
        //reposStub.setCoachState(coachTeamID, coachState);

        nOfCoach++;
        if (nOfCoach == 2) {
            infomeRefree = true;
            nOfCoach = 0;
            notifyAll();
        }

        while (!assertTriallDecision && !matchIsOver) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

    }

    /**
     * Operation anounceNewGame.
     *
     * It is called by the refree When he start a new Game.
     * 
     * @param nOfGame Current game number;
     */
    public synchronized void anounceNewGame(int nOfGame) {
        reposStub.setGame(nOfGame);

        ((RefereeSiteClientProxy) Thread.currentThread()).setRefreeState(RefreeStates.STARTOFAGAME);
        reposStub.setRefreeState(((RefereeSiteClientProxy) Thread.currentThread()).getRefreeState());
    }

    /**
     * Operation assertTriallDecision().
     *
     * It is called by the refree When he finish the trial and wake up the Coach.
     * 
     */
    public synchronized void assertTriallDecision() {
        assertTriallDecision = true;
        notifyAll();
    }

     /**
   *   Operation server shutdown.
   *
   *   New operation.
   */

   public synchronized void shutdown ()
   {
       nEntities += 1;
       if (nEntities >= SimulPar.E)
       ServerGameOfTheRopeRefereeSite.waitConnection = false;
       notifyAll ();                                        // the site may now terminate
   }
   

}
