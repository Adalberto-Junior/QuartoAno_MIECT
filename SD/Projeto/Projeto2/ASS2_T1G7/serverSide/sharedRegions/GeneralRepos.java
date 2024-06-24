package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;
import commInfra.*;

/**
 * General Repository.
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it
 * to be printed in the logging file.
 * It is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * There are no internal synchronization points.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralRepos {
   /**
    * Name of the logging file.
    */

   private String logFileName;

   /**
    * State of the referee.
    */

   private int refreeState;

   /**
    * State of the coach of team.
    */

   private  int[] coachState;

   /**
    * State of the contestant.
    */

   private  int[][] contestantState;

   /**
    * Strength of the contestant
    */

   private int[][] contestantStrength;

   /**
    * Contestant identification at the position ? at the end of the rope for
    * present trial
    */

   private  int[][] contestantTrialId;

   /**
    * Trial number
    */

   private int NB;

   /**
    * Position of the centre of the rope at the beginning of the trial
    */

   private int PS;
   /**
    * Number of the corrent Game;
    */

   private int Game;

   /**
    * winning team;
    */

   private int winTeam;

   /**
    * The champion team;
    */
   private int champion;

   /**
    * The score of the match;
    */
   private int[] score;

   /**
   *   Number of entity groups requesting the shutdown.
   */

   private int nEntities;

   /**
    * Instantiation of a general repository object.
    *
    * 
    */

   public GeneralRepos() {
   
      this.logFileName = "logger";
      

      refreeState = RefreeStates.STARTOFTHEMATCH;

      coachState = new int[SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
         coachState[i] = CoachStates.WAITFORREFEREECOMMAND;

      contestantState = new int[SimulPar.T][SimulPar.M];
      for (int i = 0; i < SimulPar.T; i++) {
         for (int k = 0; k < SimulPar.M; k++)
            contestantState[i][k] = ContestantStates.SEATATTHEBENCH;
      }

      this.contestantStrength = new int[SimulPar.T][SimulPar.M];
      /*
      for (int i = 0; i < SimulPar.T; i++) {
         for (int k = 0; k < SimulPar.M; k++)
            this.contestantStrength[i][k] = 0;
      }
      */

      contestantTrialId = new int[SimulPar.T][SimulPar.M - 2];
      score = new int[SimulPar.T];

      NB = 0; // Invalid
      PS = -100; // Invalid
      Game = 0; // Invalid
      winTeam = -1; // Invalid
      champion = -1; // Invalid
      nEntities = 0;
   }

   /**
   *   Operation initialization of simulation.
   *
   *   New operation.
   *
   *     @param logFileName name of the logging file
   *     @param strength array with the contestant'strength
   */

   public synchronized void initSimul (String logFileName, int [][]strength)
   {
      if (!Objects.equals (logFileName, ""))
         this.logFileName = logFileName;
      this.contestantStrength = strength;
      reportInitialStatus ();
   }

  /**
   *   Operation server shutdown.
   *
   *   New operation.
   */

   public synchronized void shutdown ()
   {
       nEntities += 1;
       if (nEntities >= SimulPar.E){
         ServerGameOfTheRopeGeneralRepos.waitConnection = false;
       }
       notifyAll();
   }

   /**
    * Write the header to the logging file.
    *
    * The barbers are sleeping and the customers are carrying out normal duties.
    * Internal operation.
    */

   private void reportInitialStatus() {
      TextFile log = new TextFile(); // instantiation of a text file handler

      if (!log.openForWriting(".", logFileName)) {
         GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
         System.exit(1);
      }

      log.writelnString("\t\t\t\t\t\tGame of the Rope - Description of the internal state\n");
      log.writelnString(
            "Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5       Trial        ");
      log.writelnString(
            "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS");
      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
      reportStatus();
   }

   /**
    * Set referee state.
    *
    * @param state referee state
    */

   public synchronized void setRefreeState(int state) {
      refreeState = state;
      reportStatus();
   }

   /**
    * Set coach state.
    *
    * @param teamId coach team id
    * @param state  coach state
    */

   public synchronized void setCoachState(int teamId, int state) {
      coachState[teamId] = state;
      reportStatus();
   }

   /**
    * Set contestant state.
    *
    * @param tId   team id
    * @param id    contestant id
    * @param state contestant state
    */

   public synchronized void setContestantState(int tId, int id, int state) {
      contestantState[tId][id] = state;
      reportStatus();
   }

   /**
    * Set contestant Strength.
    *
    * @param tId      team id
    * @param id       contestant id
    * @param strength contestant Strength
    */

   public synchronized void setContestantStrength(int tId, int id, int strength) {
      contestantStrength[tId][id] = strength;
      //reportStatus();
   }

   /**
    * Set contestant identification at the position ? at the end of the rope for
    * present trial.
    *
    * @param tId      team id
    * @param id       contestant id
    * @param position contestant position
    */

   public synchronized void setContestantTrialId(int tId, int id, int position) {
      contestantTrialId[tId][position] = id+1;
      //reportStatus();
   }

   /**
    * Set trial number.
    *
    * @param number trial number
    */

   public synchronized void setTrialNumber(int number) {
      NB = number;
      //reportStatus();
   }

   /**
    * Set Position of the centre of the rope at the beginning of the trial.
    *
    * @param position Position of the centre of the rope
    */

   public synchronized void setPositionCentreRope(int position) {
      PS = position;
      //reportStatus();
   }

   /**
    * Set Number of the current Game.
    *
    * @param game Number of the current Game
    */

   public synchronized void setGame(int game) {
      Game = game;
      printHeader();
   }

   /**
    * Set the winning team.
    *
    * @param winTeam Number of winning team
    */

   public synchronized void setWinTeam(int winTeam) {
      this.winTeam = winTeam;
      printEndOfGame();
   }

   /**
    * Set the winning team.
    *
    * @param score The Score of the match;
    */

   public synchronized void setScore(int[] score) {
      this.score = score;
   }

   /**
    * Set the champion team.
    *
    * @param champion The champion team;
    */

   public synchronized void setChampion(int champion) {
      this.champion = champion;
      printEndOfMatch();
   }

   /**
    * Write a state line at the end of the logging file.
    *
    * The current state of entities is organized in a line to be printed.
    * Internal operation.
    */

   private void reportStatus() {
      TextFile log = new TextFile(); // instantiation of a text file handler

      String lineStatus = ""; // state line to be printed

      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }

      switch (refreeState) {
         case RefreeStates.STARTOFTHEMATCH:
            lineStatus += "SOM ";
            break;
         case RefreeStates.STARTOFAGAME:
            lineStatus += "SOG ";
            break;
         case RefreeStates.TEAMSREADY:
            lineStatus += "TRY ";
            break;
         case RefreeStates.WAITFORTRIALCONCLUSION:
            lineStatus += "WTC ";
            break;
         case RefreeStates.ENDOFAGAME:
            lineStatus += "EOG ";
            break;
         case RefreeStates.ENDOFTHEMATCH:
            lineStatus += "EOM ";
            break;
         default:
            break;
      }

      for (int i = 0; i < SimulPar.N; i++) {
         switch (coachState[i]) {
            case CoachStates.WAITFORREFEREECOMMAND:
               lineStatus += " WFRC ";
               break;
            case CoachStates.ASSEMBLETEAM:
               lineStatus += " ASTM ";
               break;
            case CoachStates.WATCHTRIAL:
               lineStatus += " WATL ";
               break;
            default:
               break;
         }
         for (int k = 0; k < SimulPar.M; k++) {
            switch (contestantState[i][k]) {
               case ContestantStates.SEATATTHEBENCH:
                  lineStatus += "SAB ";
                  break;
               case ContestantStates.STANDINPOSITION:
                  lineStatus += "SIP ";
                  break;
               case ContestantStates.DOYOURBEST:
                  lineStatus += "DYB ";
                  break;
               default:
                  break;
            }
            lineStatus += String.format("%2d ", contestantStrength[i][k]);
         }

      }
      for (int i = 0; i < SimulPar.T; i++) {
         if (i == 0) {
            for (int k = 0; k < (SimulPar.M - 2); k++) {
               if (NB == 0 ||  contestantTrialId[i][k] == 0 ) { //NB == 1 ||
                  lineStatus += String.format("%s ", "-");
               } else {
                  lineStatus += String.format("%d ", contestantTrialId[i][k]);
               }
            }

            lineStatus += ". ";
         } else {
            for (int k = 0; k < (SimulPar.M - 2); k++) {
               if (NB == 0 ||  contestantTrialId[i][k] == 0) //NB == 1 ||
                  lineStatus += String.format("%s ", "-");
               else
                  lineStatus += String.format("%d ", contestantTrialId[i][k]);
            }
         }

      }
      /*
      for (int i = 0; i < SimulPar.T; i++) {
         if (i == 0) {
            for (int k = 0; k < (SimulPar.M - 2); k++) {
               if (NB == 0) { //NB == 1 ||
                  lineStatus += String.format("%s ", "-");
               } else {
                  lineStatus += String.format("%d ", contestantTrialId[i][k]);
               }
            }

            lineStatus += ". ";
         } else {
            for (int k = 0; k < (SimulPar.M - 2); k++) {
               if (NB == 0) //NB == 1 ||
                  lineStatus += String.format("%s ", "-");
               else
                  lineStatus += String.format("%d ", contestantTrialId[i][k]);
            }
         }

      }*/
      if (NB == 0)
         lineStatus += String.format("%s ", "--");
      else
         lineStatus += String.format("%2d ", NB);

      if (PS == -100)
         lineStatus += String.format("%s ", "--");
      else
         lineStatus += String.format("%2d ", PS);

      log.writelnString(lineStatus);

      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   /**
    * Write the header of the Game.
    *
    */
    private void printHeader() {
      TextFile log = new TextFile();

      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
         System.exit(1);
      }

      log.writelnString("Game " + Game);
      log.writelnString(
            "Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5       Trial        ");
      log.writelnString(
            "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS");

      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
      //reportStatus();

   }

   /**
    * Write the end of the game.
    *
    */
    private void printEndOfGame() {

      TextFile log = new TextFile();

      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
         System.exit(1);
      }
      if(NB == 6 && PS < 0){
         log.writelnString("Game " + Game + " was won by team " + "1" + " by points. ");
      }else if(NB == 6 && PS > 0){
         log.writelnString("Game " + Game + " was won by team " + "2" + " by points. ");
      }else if((NB == 5 || NB == 4) && PS < 0){
         log.writelnString("Game " + Game + " was won by team " + "1" + " by knock out in " + NB
         + " trials.");
      } else if((NB == 5 || NB == 4) && PS > 0){
         log.writelnString("Game " + Game + " was won by team " + "2" + " by knock out in " + NB
         + " trials.");
      } else {
         log.writelnString("Game " + Game + " was a draw. ");  
      }
      
      

      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   /**
    * Write the end of the Match.
    *
    */
    private void printEndOfMatch() {
      TextFile log = new TextFile();

      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
         System.exit(1);
      }
      if(score[0] == score[1]){
         log.writelnString(
            "Match was a draw.\n");
      }else{
         log.writelnString(
            "Match was won by team " + champion + "(" + score[0] + "-" + score[1] + ").\n");
      }
     

      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

   /**
    * Write the legend of the Internal operation.
    *
    */
    private void printSumUp() {
      TextFile log = new TextFile();

      if (!log.openForAppending(".", logFileName)) {
         GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }

      log.writelnString("\nLegend: \n" +
            "Ref Sta - state of the referee\n" +
            "Coa # Stat - state of the coach of team # (# - 1 .. 2)\n" +
            "Cont # Sta - state of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left\n"
            +
            "Cont # SG - strength of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left\n"
            +
            "TRIAL - ? - contestant identification at the position ? at the end of the rope for present trial (? - 1 .. 3)\n"
            +
            "TRIAL - NB - trial number\n" +
            "TRIAL - PS - position of the centre of the rope at the beginning of the trial\n");

      if (!log.close()) {
         GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
         System.exit(1);
      }
   }

}