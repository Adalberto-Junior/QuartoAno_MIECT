package commInfra;

import java.io.*;
import genclass.GenericIO;
import ClientSide.main.SimulPar;
import ClientSide.entities.Contestant;

/**
 * Internal structure of the exchanged messages.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable {
   /**
    * Serialization key.
    */

   private static final long serialVersionUID = 2021L;

   /**
    * Message type.
    */

   private int msgType = -1;

   /**
    * Referee state.
    */

   private int refereeState = -1;

   /**
    * Coach identification
    */

   private int coaId = -1;

   /**
    * Coach state.
    */

   private int coaState = -1;


   private int coachTeamID = -1;


   private int coachState = -1;

   /**
    * Contestant identification.
    */

   private int contId = -1;

   /**
    * Contestant team identification.
    */

   private int contTeamId = -1;

   /**
    * Contestant state.
    */

   private int contState = -1;

   /**
    * Contestant strength.
    */

   private int contStrength = -1;

   /**
    * All Contestant strength.
    */
   private int[][] strength = new int[SimulPar.T][SimulPar.M];

    /**
     * Array of Contestant.
     */
    private int[][] allCont = new int[SimulPar.T][SimulPar.M];

   /**
    * End of Match ().
    */

   private boolean endOfMatch = false;

   /**
    * The trail's winning team id.
    */
   private int winTeam = -1;

    /**
    * Contestant identification at the position ? at the end of the rope for
    * present trial
    */

    private int[][] contPos = new int[SimulPar.T][SimulPar.k] ;

    /**
    * Sum of the strengths of each team's players on the field
    */

    private int[][] SGTotal = new int[SimulPar.T][1] ;

    /**
    * Contestant identification at the position at the end of the rope for
    * present trial
    */
    private int position = -1;

    /**
     * Trial number
     */
 
    private int NB = -1;
 
    /**
     * Number of the corrent Game;
     */
 
    private int Game = -1;
 
    
    /**
     * The champion team;
     */
    private int champion = -1;
 
    /**
     * The score of the match;
     */
    private int[] score = new int[SimulPar.T];
 

   /**
    * The position of the center io rope.
    */
   private int PS = -100;
   /**
    * The assert Trial Decision.
    */
   private boolean assertDecision = false;

   
   /**
    * Name of the logging file.
    */

   private String fName = null;

   /**
    * Message instantiation (form 1).
    *
    * @param type message type
    */

   public Message(int type) {
      msgType = type;
   }

   /**
    * Message instantiation (form 2).
    *
    * @param type  message type
    * @param value    coach team identification/nofGame/NB/winner'Team/Contestant Strength
    * @param state coach state/Contestant state
    */
   public Message (int type, int value, int state) {
      msgType = type;
      if((msgType == MessageType.CALCONT) || (msgType == MessageType.CALCONTDONE) || (msgType == MessageType.SETCOAST) || (msgType == MessageType.SETCOASTDONE) || (msgType == MessageType.REVNOT) || (msgType == MessageType.REVNOTDONE) || (msgType == MessageType.INFRF) || (msgType == MessageType.INFRFDONE)){
         coachTeamID = value;
         coaId = value;
         coachState = state;
      }else if((msgType == MessageType.ANEWG) || (msgType == MessageType.ANEWGDONE)){
            refereeState = state;
            Game = value;
      }else if((msgType == MessageType.CALTR) || (msgType == MessageType.CALTRDONE)){
         refereeState = state;
         NB = value;
      }else if((msgType == MessageType.DEGAMWI) || (msgType == MessageType.DEGAMWIDONE)){
         refereeState = state;
         winTeam = value;
      }else if((msgType == MessageType.PUTHROPDONE) || (msgType == MessageType.PUTHROP)){
         contState = state;
         contStrength = value;
      }else{
         GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
         System.exit (1);
      }
   }


   /**
    * Message instantiation (form 3).
    *
    * @param type  message type
    * @param value referee state/winning team/PS/contestant trial position/Number of the current Game/champion team/Contestant state
    */

   public Message(int type, int value) {
      msgType = type;
      if((msgType == MessageType.SETREFST) || (msgType == MessageType.STRTTRDONE) || (msgType == MessageType.STRTTR) || 
         (msgType == MessageType.DEMACHWI) || (msgType == MessageType.DEMACHWIDONE) ){
        
         refereeState = value;
      }
      else if((msgType == MessageType.ASSTRDEC) || (msgType == MessageType.ASSTRDECDONE) || (msgType == MessageType.SETWINTEAM) || (msgType == MessageType.SETWINTEAMDONE)){
         winTeam = value;
      }
      else if((msgType == MessageType.GETPSCTR) || (msgType == MessageType.GETPSCTRDONE) || (msgType == MessageType.SETPSCTRO) || (msgType == MessageType.SETPSCTRODONE)){
         PS = value;
      }else if((msgType == MessageType.SETPOS) || (msgType == MessageType.SETPOSDONE)){
         position = value;
      }else if((msgType == MessageType.SETNB) || (msgType == MessageType.SETNBDONE)){
         NB = value;
      }else if((msgType == MessageType.SETNBGM) || (msgType == MessageType.ENDOFMA) || (msgType == MessageType.ENDOFMAREPLY)){
         Game = value;
      }else if((msgType == MessageType.SETCHAP) || (msgType == MessageType.SETCHAPDONE)){
         champion = value;
      }else if((msgType == MessageType.PUTHROPDONE) || (msgType == MessageType.PUTHROP)){
         contState = value;
      }else{
         GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
                        System.exit (1);
      }
   }

   /**
    * Message instantiation (form 4).
    *
    * @param type   message type
    * @param id     contetant identification/Number of trial(NB)
    * @param teamId contetant team identification/Position of the Center of the Rope
    * @param value  contestant state/strength/ trial position/Referee state
    */

   public Message(int type,int id, int teamId, int value) {
      msgType = type;
      if((msgType == MessageType.SETCONTST) || (msgType == MessageType.GETREADYDONE) || (msgType == MessageType.PUTHROP) || (msgType == MessageType.PUTHROPDONE) ||
         (msgType == MessageType.GETREADY) || (msgType == MessageType.SETDOW) || (msgType == MessageType.SETDOWDONE) || (msgType == MessageType.AMDONE) || (msgType == MessageType.AMDONEREPLY)){
         contId = id;
         contTeamId = teamId;
         contState = value;
      }
      else if((msgType == MessageType.SETCONSTGT) || (msgType == MessageType.SETCONSTGTDONE)){
         contId = id;
         contTeamId = teamId;
         contStrength = value;
      }
      else if((msgType == MessageType.SETCONTRID) || (msgType == MessageType.SETCONTRIDDONE)){
         contId = id;
         contTeamId = teamId;
         position = value;
      } else if((msgType == MessageType.CALTR) || (msgType == MessageType.CALTRDONE)){
         NB = id;
         PS = teamId;
         refereeState = value;
      }else{
         GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
                        System.exit (1);
      }
   }

   /**
    * Message instantiation (form 5).
    *
    * @param type     message type
    * @param score    score of the trial
    * 
    */
    public Message(int type, int[]score) {
      msgType = type;
      this.score = score;
   }
   
   /**
    * Message instantiation (form 6).
    *
    * @param type     message type
    * @param name     name of the logging file
    * @param strength strength of all contestant
    */

   public Message(int type, String name, int[][] strength) {
      msgType = type;
      fName = name;
      this.strength = strength;
   }

   /**
    * Message instantiation (form 7).
    *
    * @param type     message type
    * @param decision  End of trial/end of the match    
    */

    public Message(int type, boolean decision) {
      msgType = type;
      if((msgType == MessageType.GETTRIDEC) || (msgType == MessageType.GETTRIDECDONE)){
         assertDecision = decision;
      }
      else if((msgType == MessageType.GETENDOFMA) || (msgType == MessageType.GETENDOFMADONE)){
         endOfMatch = decision;
      }else{
         GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
                        System.exit (1);
      }
   }

   /**
    * Message instantiation (form 8).
    *
    * @param type     message type
    * @param strength strength of all contestant
    */

    public Message(int type,  int[][] strength) {
      msgType = type;
      if ((msgType == MessageType.SETSTGT) || (msgType == MessageType.SETSTGTDONE)) {
         this.SGTotal = strength;
      }else{
         
         this.strength = strength;
      }
   }

   /**
    * Message instantiation (form 9).
    *
    * @param type   message type
    * @param id     contetant identification
    * @param teamId contetant team identification
    * @param SG     contestant strength
    * @param state  contestant state
    */

    public Message(int type,int id, int teamId, int SG, int state) {
         msgType = type;
         contId = id;
         contTeamId = teamId;
         contState = state;
         contStrength = SG;
   }

   /**
    * Message instantiation (form 10).
    *
    * @param type     message type
    * @param allCont  all contestant
    *

    public Message(int type,  Contes[][] allCont) {
      msgType = type;
      this.allCont = allCont;
   }
    */

   /**
    * Getting message type.
    *
    * @return message type
    */

   public int getMsgType() {
      return (msgType);
   }

   /**
    * Getting referee state.
    *
    * @return referee state
    */

   public int getRefereeState() {
      return (refereeState);
   }
   public int getRefreeState() {
      return refereeState;
   }


   /**
    * Getting coach identification .
    *
    * @return coach identification
    */

   public int getCoachId() {
      return coaId;
   }

   /**
    * Getting coach state.
    *
    * @return coach state
    */
   public int getCoachState() {
      return coachState;
   }

   /**
    * Getting contestant id.
    *
    * @return contestant id
    */

   public int getContestantId() {
      return contId;
   }

   /**
    * Getting contestant teamId.
    *
    * @return contestant teamId
    */

   public int getContestantTeamId() {
      //GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": InvaldTEam Id!: " + contTeamId);
      //GenericIO.writelnString("ContId: "+contId);
      //GenericIO.writelnString("ContSG: "+contStrength);

      return contTeamId;
   }

   /**
    * Getting contestant state.
    *
    * @return contestant state
    */
   public int getContestantState() {
      return contState;
   }

   /**
    * Getting contestant strength.
    *
    * @return contestant strength
    */

   public int getContestantStrength() {
      return contStrength;
   }

   /**
    * Getting team winner.
    *
    * @return winning team
    */

    public int getWinningTeam() {
      return winTeam;
   }

   /**
    * Getting contestant trial position.
    *
    * @return position
    */

    public int getContPosition() {
      return position;
   }

   /**
    * Getting trial Number.
    *
    * @return NB
    */

    public int getNB() {
      return NB;
   }

   /**
    * Getting Game Number.
    *
    * @return Game
    */

    public int getGame() {
      return Game;
   }

   /**
    * Getting team champion.
    *
    * @return champion team
    */

    public int getChampion() {
      return champion;
   }

   /**
    * Getting trial score.
    *
    * @return trial score 
    */

    public int[] getScore() {
      return score;
   }

   /**
    * Getting Position of Center of the Rope.
    *
    * @return posittion of the center of the rope
    */

    public int getPositionCenterRop () {
      return PS;
   }

   /**
    * Getting End of the Match.
    *
    * @return end of the match(true or false)
    */

    public boolean getEndOfMatch() {
      return endOfMatch;
   }

   /**
    * Getting the trual decision.
    *
    * @return trial decision(trial is end or not)
    */

    public boolean getAssertDecision() {
      return assertDecision;
   }

    /**
    * Getting the strength of the all contestant.
    *
    * @return Strengths
    */

    public int[][] getStrengths() {
      return this.strength;
   }

  /** Getting THE Sum of the strengths of each team's players on the field.
   *
   * @return SUm of Strengths
   */

   public int[][] getSGTotal() {
     return this.SGTotal;
  }

   /**
    * Getting the array of the all contestant.
    *
    * @return all contestant
    */

    public int[][] getAllCont() {
      return this.allCont;
   }

   


   
   /**
    * Getting name of logging file.
    *
    * @return name of the logging file
    */

   public String getLogFName() {
      return (fName);
   }

   /**
    * Printing the values of the internal fields.
    *
    * It is used for debugging purposes.
    *
    * @return string containing, in separate lines, the pair field name - field
    *         value
    */

   @Override
   public String toString() {
      return ("Message type = " + msgType +
            "\nRefereeSate = " + refereeState +
            "\nCoachId = " + coaId +
            "\ncoach State = " + coaState +
            "\nContestant Id = " + contId +
            "\nContestant Team Id = " + contTeamId +
            "\nContestant State = " + contState +
            "\nContestant Strength = " + contStrength +
            "\nContestant Trial Pos = " + position +
            "\nName of logging file = " + fName +
            "\nWin Team = " + winTeam +
            "\nPosition of the rope = " + PS +
            "\nAssert Decision = " + assertDecision +
            "\nNumber of Game = " + Game + 
            "\nSUMSG = " + SGTotal[0][0] + "; " + SGTotal[1][0] +
            "\nEnd of the Macth = " + endOfMatch);
   }
}
