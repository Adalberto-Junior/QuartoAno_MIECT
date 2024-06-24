package serverSide.sharedRegions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import ClientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
/**
 * Contestant Bench.
 *
 * It is responsible to simulate the contestant's bench
 * and is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 * 
 */
public class Contestants_bench {

    /**
    *   Reference to the stub of the general repository.
    */

   private final GeneralReposStub reposStub;

    /**
    *   Reference to the stub of the play ground.
    */
    private final PlayGroundStub plGrndStub;
    /**
     * Array of Contestant 'strength'.
     */
    private int[][] allCont = new int[SimulPar.N][SimulPar.M];

    /**
     * Array of Contestant 'strength'.
     */
    private int[][] pastAllCont = new int[SimulPar.N][SimulPar.M];;

    /**
     * Array with the contestant's selected to play
     */
    private int[][] player;

    /**
     * Array with the contestant's selected to play in last trial
     */
    private int[][] played;

    /**
     * Array with the strength of the contestant who was selected to play
     */
    private int[][] strength;

    /**
     * variable that wakes up the coach after the callContestants()
     */
    private boolean followCoachAdv;

    /*
     * variable that wakes up the coach after the callTrial
     */
    private boolean callTrial;

    /*
     * variable that wakes up the contestant after the startTrial
     */
    private boolean startTrial;

    /*
     * variable that wakes up the contestant after the callContestants
     */
    private boolean callContestant;

    /*
     * Contestant's number playing
     */
    private int nContPlaying;

    /*
     * Total Coach's number
     */
    private int ntotalCoach;

     /**
     *   Number of entity groups requesting the shutdown.
     */

   private int nEntities;

   /**
     *   Variable used to choose players in the odd and even strategy.
     */
    private int chosen;

    /**
     *   Variable used to keep the last of the sum' strength in players (Team 1).
     */
    private int pastTeam1SumSG;

    /**
     *   Variable used to keep the last of the sum' strength in players (Team 2).
     */
    private int pastTeam2SumSG;



    // Contestant[] p = new Contestant[5];

    /**
     * Contestant bench instantiation.
     *
     * @param reposStub      reference to the stub of general repository
     * @param playGroundStub reference to the stub of  play ground
     */

    public Contestants_bench(GeneralReposStub reposStub, PlayGroundStub playGroundStub) {
        this.reposStub = reposStub;
        plGrndStub = playGroundStub;
        followCoachAdv = false;
        startTrial = false;
        callTrial = false;
        callContestant = false;
        nContPlaying = 0;
        ntotalCoach = 0;
    
        player = new int[SimulPar.N][SimulPar.M - 2];
        for (int i = 0; i < SimulPar.N; i++) {
            for (int k = 0; k < (SimulPar.M - 2); k++) {
                player[i][k] = 0;
            }
        }
        played = new int[SimulPar.N][SimulPar.M - 2];
        for (int i = 0; i < SimulPar.N; i++) {
            for (int k = 0; k < (SimulPar.M - 2); k++) {
                played[i][k] = 0;
            }
        }
        strength = new int[SimulPar.N][1];
        strength[0][0] = strength[1][0] = 0;
  
        nEntities = 0;
        chosen = 0;
    }

    
    /**
     * Operation setContestant.
     *
     * is an auxiliary method that we created to update the allCont variable
     * @param strength contestants's strength
     * 
     */
    public synchronized void setContestant(int [][] strength) {
        this.allCont = strength;
    }

    /**
     * Operation call Contestants.
     *
     * It is called by the coaches when they want to select the Contestant for the
     * next trial.
     * 
     */
    public synchronized void callContestants() {

  
        ((ContestantBenchClientProxy) Thread.currentThread()).setCoachState(CoachStates.ASSEMBLETEAM);
        reposStub.setCoachState(((ContestantBenchClientProxy) Thread.currentThread()).getCoachTeamId(),
                                ((ContestantBenchClientProxy) Thread.currentThread()).getCoachState());
        ContestantBenchClientProxy coach = (ContestantBenchClientProxy) Thread.currentThread();
    
        int coachTeamID = coach.getCoachTeamId();
        for(int i = 0; i < SimulPar.M;i++){
            if(allCont[coachTeamID][i] == 0){
                allCont[coachTeamID][i] = pastAllCont[coachTeamID][i];
            }
        }

        if (coachTeamID == 0) {
        
            int sel = 0;
            int nCont = SimulPar.M;
            int k_ = 0;
            int strengthOfPlayerTeam0 = 0;
          
            if(chosen % 2 == 0){
                for(int i = 0; i < SimulPar.M;i++){
                    
                    if(i % 2 == 0){
                        player[0][k_] = i;
                        strengthOfPlayerTeam0 += allCont[0][i];
                        k_++;
                    }
                    

                }  
            }else{
                for(int i = 0; i < SimulPar.M;i++){
                    if(i % 2 != 0){
                        player[0][k_] = i;
                        strengthOfPlayerTeam0 += allCont[0][i];
                        k_++;
                    }
                }  
            }
            chosen++;
            strength[0][0] = strengthOfPlayerTeam0;
            for(int i = 0; i < SimulPar.M;i++){
                if(allCont[0][i] != 0){
                    pastAllCont[0][i] = allCont[0][i];
                }
            }  
           

        } else {
            // Strategy take anyone (Random)
            Random random = new Random();
            ArrayList<Integer> invalid = new ArrayList<Integer>(); // Already selected
            int strengthOfPlayerTeam1 = 0;
            for (int i = 0; i < (SimulPar.M - 2); i++) {
                boolean valid = false;

                while (!valid) {
                    int randomNumber = random.nextInt(5);
                    if (!invalid.contains(randomNumber)) {
                        player[1][i] = randomNumber;
       
                        valid = true;
                        invalid.add(randomNumber);
                        strengthOfPlayerTeam1 += allCont[1][randomNumber];
                    }
                }
            }
            strength[1][0] = strengthOfPlayerTeam1;
            for(int i = 0; i < SimulPar.M;i++){
                if(allCont[1][i] != 0){
                    pastAllCont[1][i] = allCont[0][i];
                }
            }  
        }

        if(strength[0][0] == 0 && strength[1][0] == 0){
            strength[0][0] = 24;strength[1][0] = 24;
        }


        plGrndStub.setStrength(strength);
        ntotalCoach++;
        resetAllCont(coachTeamID);  //Faz o reset do allCon

        if (ntotalCoach == 2) {
            callContestant = true;
            ntotalCoach = 0;
            notifyAll();
        }

        while (!followCoachAdv) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Operation Review Notes.
     *
     * It is called by the coaches when they review the Notes of the trial.
     *
     * 
     */
    public synchronized void reviewNotes() {
      

        if (((ContestantBenchClientProxy) Thread.currentThread()).getCoachState() == CoachStates.WATCHTRIAL){
 
            ((ContestantBenchClientProxy) Thread.currentThread()).setCoachState(CoachStates.WAITFORREFEREECOMMAND);
            reposStub.setCoachState(((ContestantBenchClientProxy) Thread.currentThread()).getCoachTeamId(),
                                    ((ContestantBenchClientProxy) Thread.currentThread()).getCoachState());
            ContestantBenchClientProxy coach = (ContestantBenchClientProxy) Thread.currentThread();
            int coachTeamID = ((ContestantBenchClientProxy) Thread.currentThread()).getCoachTeamId();
 
            played = player;
            for(int i = 0; i < player[coachTeamID].length; i++){
                player[coachTeamID][i] = -1;
            }
        }
       
        while (!callTrial) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Operation wakeUpCoach.
     *
     * It is called by the refree when he make callTrial.
     *
     */
    public synchronized void wakeUpCoach() {
        callTrial = true;
        notifyAll();
    }

    /**
     * Operation Follow Call Advice.
     *
     * It is called by the contestant if he was selected to play.
     *
     * 
     */
    public synchronized void followCoachAdvice() {
        
    

       
        
        ContestantBenchClientProxy contestant = (ContestantBenchClientProxy) Thread.currentThread();
        int contID =  ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID ();
        int contTeamId = ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId ();

        boolean selected = false;
        int position = 0;
        boolean imPlaying = false;

        reposStub.setContestantStrength(((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId (),
        ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID (),
        ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantSG ());

        if (contTeamId == 0) {
            while ((player[contTeamId][0] == player[contTeamId][1]) && (player[contTeamId][0] == player[contTeamId][2])
                    && (player[contTeamId][1] == player[contTeamId][2])) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if ((player[0][0] != player[0][1]) && (player[0][0] != player[0][2]) && (player[0][1] != player[0][2])) {
                
                for (int i = 0; i < (SimulPar.M - 2); i++) {
                    
                    if (contID == player[0][i]) {
                        selected = true;
                        position = i;
                      
                    }
                }
            }

        } else {
            while ((player[contTeamId][0] == player[contTeamId][1]) && (player[contTeamId][0] == player[contTeamId][2])
                    && (player[contTeamId][1] == player[contTeamId][2])) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if ((player[1][0] != player[1][1]) && (player[1][0] != player[1][2]) && (player[1][1] != player[1][2])) {
                for (int i = 0; i < (SimulPar.M - 2); i++) {
                    
                    if (contID == player[1][i]) {
                        selected = true;
                        position = i;
                      
                    }
                }
            }
        }
        contID =  ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID ();
        contTeamId = ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId ();
        int newSG =  ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantSG ();
        if (selected) {
            ((ContestantBenchClientProxy) Thread.currentThread()).setContestantState(ContestantStates.STANDINPOSITION);

           
            int stateCont = contestant.getContestantState();
            reposStub.setContestantState(((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID (),
                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantState ());
            reposStub.setContestantTrialId(((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID (),
                                             position);
           
            
            imPlaying = true;
            nContPlaying++;
           
            if(newSG > 6)
                allCont[contTeamId][contID] = newSG - 1;
            else
                allCont[contTeamId][contID] = newSG;

           
        } else {
            ((ContestantBenchClientProxy) Thread.currentThread ()).addStreangth();
            if(newSG < 10)
                allCont[contTeamId][contID] = newSG + 1;
            else
            allCont[contTeamId][contID] = newSG;
        }


        if (nContPlaying == 6) {
            followCoachAdv = true;
            notifyAll();
        }

        while (!startTrial) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Operation wakeUpContestant.
     *
     * It is called by the refree when he start the new trial.
     *
     * 
     */
    public synchronized void wakeUpContestant() {
        startTrial = true;
        notifyAll();
    }

    /**
     * Operation Seat Down.
     *
     * It is called by the contestants When they finish the trial
     * and go to bench
     */

    public synchronized void seatDown() {
       

        if (((ContestantBenchClientProxy) Thread.currentThread ()).getContestantState () == ContestantStates.DOYOURBEST) {

            ((ContestantBenchClientProxy) Thread.currentThread()).setContestantState(ContestantStates.SEATATTHEBENCH);
            reposStub.setContestantState(((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                         ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID (),
                                         ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantState ());

        }
        while (!callContestant) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
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
            ServerGameOfTheRopeContestantBench.waitConnection = false;
       notifyAll ();                                        // the Bench may now terminate
   }

   private void resetAllCont(int coachTeamID){
        for(int i = 0; i < SimulPar.M; i++){
            allCont[coachTeamID][i] = 0;
        }
   }
   

}
