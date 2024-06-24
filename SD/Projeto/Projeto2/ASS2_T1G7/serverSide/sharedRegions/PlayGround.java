package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import ClientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;


/**
 * Play Ground.
 *
 * It is responsible to simulate the play area
 * and is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 * 
 * 
 */
public class PlayGround {
   /**
   *   Reference to the stub of the general repository.
   */

   private final GeneralReposStub reposStub;


    /**
     * Varail booleans that indicates if the last contestant is done.
     */
    private boolean amDone;

    /**
     * Varail to indicate the last contestant is done.
     */
    private int totalContestant;

    /**
     * Varail booleans that indicates if the current trial is end.
     */
    private boolean assertTriallDecision;

    /**
     * Varail booleans that indicates if the trial is end and have a winnig.
     */
    private boolean trialDecision;

    /**
     * Varail that indicates the team 1' score.
     */
    private int win1;

      /**
     * Varail that indicates the team 2' score.
     */
    private int win2;


    /**
     * Variable that indicates whether the Match has ended.
     */
    private boolean endOfMatch;

    /**
     * Array with the Sum's strength of the contestant who was selected to play
     */
    private int[][] strength;

    /**
     * Array of Integer to keep the score of the trial.
     */
    private int[] score;

    /**
     * Position of the centre of the rope.
     */
    private int PS;

    /**
     * Varial to indicte if refree do the callTriall
     */
    private boolean callTrial;

    /**
     *   Number of entity groups requesting the shutdown.
     */

   private int nEntities;


    /**
     * Play Ground instantiation.
     *
     * @param reposStub reference to the stub of the general repository
     */

    public PlayGround(GeneralReposStub reposStub) {

        this.reposStub = reposStub;
        amDone = false;
        totalContestant = 0;
        endOfMatch = false;
        trialDecision = false;
        callTrial = false;
        strength = new int[SimulPar.N][1];
        score = new int[SimulPar.T];
        score[0] = 0;
        score[1] = 0;
        PS = 0;
        nEntities = 0;
        win1 = 0;
        win2 = 0;
    }

    /**
     * Operation Start Trial.
     *
     * It is called by the refree when he want to start the trial.
     * 
     */

    public synchronized void startTrial() {
        PlayGroundClientProxy refree = (PlayGroundClientProxy) Thread.currentThread();

        ((PlayGroundClientProxy) Thread.currentThread()).setRefreeState(RefreeStates.WAITFORTRIALCONCLUSION);
        reposStub.setRefreeState( ((PlayGroundClientProxy) Thread.currentThread()).getRefreeState());
        score[0] = 0;
        score[1] = 0;
        win1 = 0;
        win2 = 0;
    }
    
    /**
     * Operation GetRead.
     *
     * It is called by the contestants When they ready to start pulling the rope.
     * 
     */

    public synchronized void getRead() {


        if (((PlayGroundClientProxy) Thread.currentThread()).getContestantState() == ContestantStates.STANDINPOSITION) {

            ((PlayGroundClientProxy) Thread.currentThread()).setContestantState(ContestantStates.DOYOURBEST);
 
            reposStub.setContestantState(((PlayGroundClientProxy) Thread.currentThread()).getContestantTeamId(),
                                        ((PlayGroundClientProxy) Thread.currentThread()).getContestantID(),
                                        ((PlayGroundClientProxy) Thread.currentThread()).getContestantState());
        }

    }

    /**
     * Operation Pull The Rope.
     *
     * It is called by the contestants When they ready are pulling the rope.
     * 
     * 
     */

    public synchronized void pullTheRope() {

        if (((PlayGroundClientProxy) Thread.currentThread()).getContestantState() == ContestantStates.DOYOURBEST) {
            try {
                wait((long) (1 + 500 * Math.random()));
            } catch (InterruptedException e) {
            }

            ((PlayGroundClientProxy) Thread.currentThread()).subStreangth();
        }
    }

    /**
     * Operation Am Done.
     *
     * It is called by the contestants When they finish the trial.
     * The last contestant will wake up the refree;
     * 
     */

    public synchronized void amDone() {
        //PlayGroundClientProxy contestant = (PlayGroundClientProxy) Thread.currentThread();
        if (((PlayGroundClientProxy) Thread.currentThread()).getContestantState() == ContestantStates.DOYOURBEST)
            totalContestant++;

        if (totalContestant == 6) {
            amDone = true;
            totalContestant = 0;
            notifyAll();
        }

        while (!assertTriallDecision && !endOfMatch) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

    }

    /**
     * Operation assertTriallDecision.
     *
     * It is called by the refree When he finish the trial.
     * 
     * @return winTean The trail's winning team id;
     */
    public synchronized int assertTriallDecision() {

        int winTeam = 0;
        trialDecision = false;
        
        if (strength[0][0] > strength[1][0]) {
            this.score[0] += 1;
            win1++;
            PS--;
        } else if (strength[0][0] < strength[1][0]) {
            this.score[1] += 1;
            win2++;
            PS++;
        }

        if ((this.score[0] > 3 || this.score[1] > 3) || (this.score[0] == 3 && this.score[1] == 3)) {
            if (score[0] > 3)
                winTeam = 1;
            else
                winTeam = 2;
            trialDecision = true;
        }else{
            if ((win1 > 3 || win2 > 3) || (win1 == 3 && win2 == 3)) {
                if (score[0] > 3)
                    winTeam = 1;
                else
                    winTeam = 2;
                trialDecision = true;
            }
        }
        assertTriallDecision = true;
        notifyAll();

        return winTeam;
    }

    /**
     * Operation endOfMatch.
     *
     * is an auxiliary method that we created to update the endOfMatch
     * 
     * @param NG Number of the match(Game)
     * 
     */
    public synchronized void endOfMatch(int NG) {
        if (NG == 3)
            endOfMatch = true;
    }

    /**
     * Operation endOfMatch.
     *
     * is an auxiliary method that we created to take the endOfMatch
     * 
     * @return true if the the match end or not if it not end
     * 
     */
    public synchronized boolean getEndOfMatch() {
        
        return endOfMatch;
    }

    /**
     * Operation callTrial.
     *
     * is an method call by refree to wake up the coach when he make callTrial
     * 
     * 
     */
    public synchronized void callTrial() {
        callTrial = true;
        notifyAll();
    }

    /**
     * Operation declareMatchWinner.
     *
     * is an method call by refree when he make declareMatchWinner
     * 
     * 
     */

    public synchronized void declareMatchWinner() {
        endOfMatch = true;
        notifyAll();
    }

    /**
     * Operation setStrength.
     *
     * is an auxiliary method that we created to update the Strength array.
     * @param strength constestant's array of strength
     * 
     */
    public synchronized void setStrength(int[][] strength) {
        this.strength = strength;
    }

    /**
     * getPositionCenterRope.
     *
     * is an auxiliary method that return the center Rope
     * @return PS: position of center of the rope
     * 
     */
    public synchronized int getPositionCenterRope () {
        return this.PS;
    }

    /**
     * Operation getTrialDecision.
     *
     * is an auxiliary method we created to indicate if there is already
     * a winner and end the game if positive
     * 
     * @return true if there is already a winner and false if there is not
     */
    public synchronized boolean getTrialDecision() {
        return this.trialDecision;
    }

    /**
     * Operation timeToSleap.
     *
     * is an auxiliary method to bolck the refree after startTrial
     *
     */
    public synchronized void timeToSleap() {
        while (!amDone) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

     /**
     * Operation resetScore.
     *
     * is an auxiliary method to reset the score and Position of the Center of the Rope when the referee announce new game 
     *
     */
    public synchronized void resetScore() {
    	this.score[0] = 0;
        this.score[1] = 0;
        this.PS = 0;

        
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
            ServerGameOfTheRopePlayGround.waitConnection = false;
       notifyAll ();                                        // the play ground may now terminate
   }

}
