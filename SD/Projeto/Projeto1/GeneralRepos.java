package sharedRegions;


import main.*;
import entities.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;

/**
 *  General Repository.
 *
 *    It is responsible to keep the visible internal state of the problem and to provide means for it
 *    to be printed in the logging file.
 *    It is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    There are no internal synchronization points.
 */

public class GeneralRepos
{
   /**
   *  Name of the logging file.
   */

   private final String logFileName;

   /**
   *  State of the referee.
   */

   private int refereeState;

   /**
   *  State of the coach of team.
   */

   private final int [] coachState;

   /**
   *  State of the contestant.
   */

   private final int [][] contestantState;

   /**
   *  Strength of the contestant
   */

   private int [][] contestantStrength;

   /**
   *   Contestant identification at the position ? at the end of the rope for present trial
   */

   private final int [][] contestantTrialId;

   /**
   *  Trial number
   */

   private int  NB;

   /**
   *  Position of the centre of the rope at the beginning of the trial
   */

   private int PS;

   /**
   *   Instantiation of a general repository object.
   *
   *     @param logFileName name of the logging file
   *     @param contestantStrengthe[][] Strength of the contestant
   *     
   */

   public GeneralRepos (String logFileName, int [][]contestantStrength)
   {
      if ((logFileName == null) || Objects.equals (logFileName, ""))
         this.logFileName = "logger";
      else this.logFileName = logFileName;
      
      refereeState = RefereeStates.STARTOFTHEMATCH;

      coachState = new int [SimulPar.N]; 
      for(int i = 0; i < SimulPar.N; i++)
         coachState[i] = CoachStates.WAITFORREFEREECOMMAND;
      
      contestantState = new int[SimulPar.T][SimulPar.M];
      for(int i = 0; i < SimulPar.T; i++){
         for(int k = 0; k < SimulPar.M; k++)
            contestantState[i][k] = ContestantStates.SEATATTHEBENCH;
      }

      this.contestantStrength = new int[SimulPar.T][SimulPar.M];
      this.contestantStrength = contestantStrength;

      contestantTrialId = new int[SimulPar.T][SimulPar.M-2];
      NB = 0;
      PS = 0; //center

      reportInitialStatus ();
   }

   /**
   *  Write the header to the logging file.
   *
   *  The barbers are sleeping and the customers are carrying out normal duties.
   *  Internal operation.
   */

   private void reportInitialStatus ()
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForWriting (".", logFileName))
      { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
        System.exit (1);
      }

      log.writelnString ("\t\t\t\t\t\tGame of the Rope - Description of the internal state");
      log.writelnString ("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5       Trial        ");
      log.writelnString ("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS");
      if (!log.close ())
      { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
        System.exit (1);
      }
      reportStatus ();
   }

   /**
   *   Set referee state.
   *
   *     @param state referee state
   */

   public synchronized void setRefereeState (int state)
   {
      refereeState = state;
      reportStatus ();
   }

   /**
   *   Set coach state.
   *
   *     @param id coach id
   *     @param state coach state
   */

   public synchronized void setCoachState (int id, int state)
   {
      coachState[id] = state;
      reportStatus ();
   }

   /**
   *   Set contestant state.
   *
   *     @param tId team id 
   *     @param id contestant id
   *     @param state contestant state
   */

   public synchronized void setContestantState (int tId, int id, int state)
   {
      contestantState[tId][id] = state;
      reportStatus ();
   }

   /**
   *   Set contestant Strength.
   *
   *     @param tId team id 
   *     @param id contestant id
   *     @param strength contestant Strength
   */

   public synchronized void setContestantStrength (int tId, int id, int strength)
   {
      contestantStrength[tId][id] = strength;
      reportStatus ();
   }

   /**
   *   Set contestant identification at the position ? at the end of the rope for present trial.
   *
   *     @param tId team id 
   *     @param id contestant id
   *     @param position contestant position
   */

   public synchronized void setContestantTrialId (int tId, int id, int position)
   {
      contestantTrialId[tId][position] = id;
      reportStatus ();
   }

   /**
   *   Set trial number.
   *
   *     @param number trial number
   */

   public synchronized void setTrialNumber (int number)
   {
      NB = number;
      reportStatus ();
   }

   /**
   *   Set  Position of the centre of the rope at the beginning of the trial.
   *
   *     @param position Position of the centre of the rope
   */

   public synchronized void setPositionCentreRope (int position)
   {
      PS = position;
      reportStatus ();
   }

   /**
   *  Write a state line at the end of the logging file.
   *
   *  The current state of entities is organized in a line to be printed.
   *  Internal operation.
   */

   private void reportStatus ()
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      String lineStatus = "";                              // state line to be printed

      if (!log.openForAppending (".", logFileName))
      { GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
        System.exit (1);
      }
      //TODO:
      //GAME # + CABEÇALHO
      switch (refereeState) {
         case RefereeStates.STARTOFTHEMATCH: lineStatus += "SOM ";
            break;
         case RefereeStates.STARTOFAGAME: lineStatus += "SOG ";
            break;
         case RefereeStates.TEAMSREADY: lineStatus += "TRY ";
            break;
         case RefereeStates.WAITFORTRIALCONCLUSION: lineStatus += "WTC ";
            break;
         case RefereeStates.ENDOFAGAME: lineStatus += "EOG ";
            break;
         case RefereeStates.ENDOFTHEMATCH: lineStatus += "EOM ";
            break;
         default:
            break;
      }

      for (int i = 0; i < SimulPar.N; i++){
         switch (coachState[i]) {
            case CoachStates.WAITFORREFEREECOMMAND : lineStatus += " WFRC ";
               break;
            case CoachStates.ASSEMBLETEAM: lineStatus += " ASTM ";
               break;
            case CoachStates.WATCHTRIAL: lineStatus += " WATL ";
               break;
            default:
               break;
         }
         for (int k = 0; k < SimulPar.M; k++){
            switch (contestantState[i][k]) {
               case ContestantStates.SEATATTHEBENCH : lineStatus += "SAB ";
                  break;
               case ContestantStates.STANDINPOSITION: lineStatus += "SIP ";
                  break;
               case ContestantStates.DOYOURBEST: lineStatus += "DYB ";
                  break;
               default:
                  break;
            }
            lineStatus += String.format("%2d ", contestantStrength[i][k]); 
         }

      }
      
      for (int i = 0; i < SimulPar.T; i++){
         for (int k = 0; k < (SimulPar.M-2); k++){
            lineStatus += String.format("%d %d %d ",contestantTrialId[i][k]);
         }
         if ( i == 0)
            lineStatus += ". ";
      }

      lineStatus += String.format("%d ", NB);
      lineStatus += String.format("%d ", PS);

      //Gama # com Vitoria

      //TODO


      log.writelnString (lineStatus);
      if (!log.close ())
      { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
        System.exit (1);
      }
   }

   /**
    *   Write the legend of the Internal operation.
    *
    */
   public void printSumUp() 
   {
      TextFile log = new TextFile ();  

      if (!log.openForAppending (".", logFileName))
      { 
         GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
         System.exit(1);
      }
      
      log.writelnString("\nLegend: \n" +
      "Ref Sta - state of the referee\n" +
      "Coa # Stat - state of the coach of team # (# - 1 .. 2)\n" +
      "Cont # Sta - state of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left\n" +
      "Cont # SG - strength of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left\n" +
      "TRIAL - ? - contestant identification at the position ? at the end of the rope for present trial (? - 1 .. 3)\n" +
      "TRIAL - NB - trial number\n" +
      "TRIAL - PS - position of the centre of the rope at the beginning of the trial\n");

      if (!log.close ())
      { 
         GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
         System.exit (1);
      }
   }

   
}
 