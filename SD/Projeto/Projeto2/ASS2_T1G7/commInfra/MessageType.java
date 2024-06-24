package commInfra;

/**
 * Type of the exchanged messages.
 * the
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType {

  /**
   *  Initialization of the logging file name and the contestant strength (service request).
   */

   public static final int SETNFIC = 1;

  /**
   *  Logging file was initialized (reply).
   */

   public static final int NFICDONE = 2;


  /**
   *  Anounce New Game (service Request).
   */

   public static final int ANEWG = 3 ;

  /**
   *  Assert Trial Decision (service Request).
   */

   public static final int ASSTRDEC = 4;

   /**
   *  Call Trial (service Request).
   */

   public static final int CALTR = 5;

   /**
   *  Declare Game Winner (service Request).
   */

   public static final int DEGAMWI = 6;

   /**
   *  Declare Match Winner (service Request).
   */

   public static final int DEMACHWI = 7;

   /**
   *  Inform Referee (service Request).
   */

   public static final int INFRF = 8;


/**
   *  call Contestant (service Request).
   */

   public static final int CALCONT = 9;

   /**
   *  Follow Coach Adivice (service Request).
   */

   public static final int FOLWCOADV = 10;

   /**
   *  Review Notes (service Request).
   */

   public static final int REVNOT = 11;

   /**
   *  Seat Down (service Request).
   */

   public static final int SETDOW = 12;

   /**
   *  Set Contestant (service Request).
   */

   public static final int SETCONT = 13;

  /**
   *  WAKE UP COACH (service Request).
   */

   public static final int WAKUCOA = 14;

   /**
   *  WAKE UP CONTESTANT (service Request).
   */

   public static final int WAKUCONT = 15;
  
    /**
   *  Finsh push the Rope (Am Done) (service request).
   */

   public static final int AMDONE = 16;

  /**
   *  Eend Of The Match (Service request)
   */

   public static final int ENDOFMA = 17;


  /**
   *  Get End Of Match (service Request).
   */

   public static final int GETENDOFMA = 18;

  /**
   *  Get Position Of Center Rope (service Request).
   */

   public static final int GETPSCTR = 19;

   /**
   *  Get Ready (service Request).
   */

   public static final int GETREADY = 20;

   /**
   *  Get Trial Decision (service Request).
   */

   public static final int GETTRIDEC = 21;

   /**
   *  Pull The Rope (service Request).
   */

   public static final int PUTHROP = 22;

   /**
   *  Reset the Score (service Request).
   */

   public static final int RESTSCOR = 23;


/**
   *  Set Strength (service Request).
   */

   public static final int SETSTGT = 24;

   /**
   *  Start Trial (service Request).
   */

   public static final int STRTTR = 25;

   /**
   *  Referee goes to sleep (service Request).
   */

   public static final int REGOSLEP = 26;

   /**
   *  Print End Of Game (service Request).
   */

   public static final int PRITENDGM = 27;

   /**
   *  Print End Of MATCH (service Request).
   */

   public static final int PRITENDMCH = 28;

  /**
   *  Print Header of the Game (service Request).
   */

   public static final int PRITHEADGM = 29;

   /**
   *  SET Team Chapion (service Request).
   */

   public static final int SETCHAP = 30;


   /**
   *  Set Coach State (service Request).
   */

   public static final int SETCOAST = 31;

   /**
   *  Seat Contestant State (service Request).
   */

   public static final int SETCONTST= 32;


/**
   *  Set Contetant Strength (service Request).
   */

   public static final int SETCONSTGT = 33;

   /**
   *  Set Contetant Trial Id (service Request).
   */

   public static final int SETCONTRID = 34;

   /**
   *  Set Number of Current Game (service Request).
   */

   public static final int SETNBGM = 35;

   /**
   *  Set Position Of The Center Of The Rope (service Request).
   */

   public static final int SETPSCTRO = 36;

   /**
   *  Set Referee State (service Request).
   */

   public static final int SETREFST = 37;

  /**
   *  Set the Score of the trial  (service Request).
   */

   public static final int SETSCORE = 38;

   /**
   *  Set Trial Number (service Request).
   */

   public static final int SETNB = 39;

     /**
   *  Set the Winning Team (service Request).
   */

   public static final int SETWINTEAM = 40;

  /**
   *  Server shutdown (service request).
   */

   public static final int SHUT = 41;

  /**
   *  Setting acknowledged (reply).
   */
   public static final int SACK = 42;

  /**
   *  Refere Go sleeping after call Trial (Request).
   */

   public static final int SLEEPINCALTRAIL = 43;

  /**
   *  Set Position (service request).
   */

   public static final int SETPOS = 44;

   //REPLY:

   /**
   *  New Game Was anounce (reply).
   */

   public static final int ANEWGDONE = 45 ;

  /**
   *  Trial Decision Was Assert (reply).
   */

   public static final int ASSTRDECDONE = 46;

   /**
   *  Trial was called (reply).
   */

   public static final int CALTRDONE = 47;

   /**
   *  Game Winner Was Declared (reply).
   */

   public static final int DEGAMWIDONE = 48;

   /**
   *  Match Winner Was Declared (reply).
   */

   public static final int DEMACHWIDONE = 49;

   /**
   *  Referee Was Informed (reply).
   */

   public static final int INFRFDONE = 50;


/**
   *   Contestant Was Called(reply).
   */

   public static final int CALCONTDONE = 51;

   /**
   *   Coach Adivice Was Followed (reply).
   */

   public static final int FOLWCOADVDONE = 52;

   /**
   *   Notes Was Reviewed (reply ).
   */

   public static final int REVNOTDONE = 53;

   /**
   *  Contetant was Seated Down (reply ).
   */

   public static final int SETDOWDONE = 54;

   /**
   *   Contestant Was Seted (reply ).
   */

   public static final int SETCONTDONE = 55;

  /**
   *  COACH Was Waked up (reply ).
   */

   public static final int WAKUCOADONE = 56;

   /**
   *  CONTESTANT Was waked up (reply ).
   */

   public static final int WAKUCONTDONE = 57;
  
    /**
   *  Finsh push the Rope reply (Am Done) (reply ).
   */

   public static final int AMDONEREPLY = 58;

  /**
   *  End Of The Match Reply (reply)
   */

   public static final int ENDOFMAREPLY = 59;


  /**
   *  End Of Match Was got(reply).
   */

   public static final int GETENDOFMADONE = 60;

  /**
   *  Position Of Center Rope Was Got (reply).
   */

   public static final int GETPSCTRDONE = 61;

   /**
   *  Ready Was Got(reply).
   */

   public static final int GETREADYDONE = 62;

   /**
   *  Trial Decision Was Got (reply).
   */

   public static final int GETTRIDECDONE = 63;

   /**
   *  The Rope Was Pulled (reply).
   */

   public static final int PUTHROPDONE = 64;


  /**
   *  Refere Go sleeping after call Trial (Replay).
   */

   public static final int SLEEPINCALTRAILDONE = 43;


/**
   *  Strength Was Setted (reply).
   */

   public static final int SETSTGTDONE = 66;

   /**
   *  Trial Was Started (reply).
   */

   public static final int STRTTRDONE = 67;

   /**
   *  Referee goes to sleep reply (reply ).
   */

   public static final int REGOSLEPDONE = 68;

   /**
   *  Team Chapion Was Setted (reply ).
   */

   public static final int SETCHAPDONE = 69;


   /**
   *   Coach State Was Setted (reply).
   */

   public static final int SETCOASTDONE = 70;

   /**
   *  Contestant State Was Setted (reply).
   */

   public static final int SETCONTSTDONE = 71;


/**
   * Contetant Strength Was Setted (reply ).
   */

   public static final int SETCONSTGTDONE = 72;

   /**
   *  Contetant Trial Id Was Setted (reply).
   */

   public static final int SETCONTRIDDONE = 73;

   /**
   *   Number of Current Game Was Setted (reply).
   */

   public static final int SETNBGMDONE = 74;

   /**
   *  Position Of The Center Of The Rope Was Setted (reply).
   */

   public static final int SETPSCTRODONE = 75;

   /**
   *   Referee State Was Setted (reply).
   */

   public static final int SETREFSTDONE = 76;

  /**
   *  The Score of the trial Was Setted (reply).
   */

   public static final int SETSCOREDONE = 77;

   /**
   *  Trial Number Was Setted (reply).
   */

   public static final int SETNBDONE = 78;

     /**
   *  The Winning Team Was Setted (reply).
   */

   public static final int SETWINTEAMDONE = 79;

  /**
   *  Server was shutdown (reply).
   */

   public static final int SHUTDONE = 80;

  /**
   *  Position Was Setted (reply).
   */

   public static final int SETPOSDONE = 81;









   //TODO: APAGAR ESSAS MENSAGENS; ESTÃO ERRADAS: 

  /**
   * Transition state.
   */

  public static final int STARTOFAGAME = 1;

  /**
   * The referee waits for the last coach to announce that the team is ready.
   */

  public static final int TEAMSREADY = 2;

  /**
   * The referee is waiting for the trial to end.
   */

  public static final int WAITFORTRIALCONCLUSION = 3;

  /**
   * The Referee will announce the new game or declare match the winner.
   */

  public static final int ENDOFAGAME = 4;

  /**
   * The Referee finish the match.
   */

  public static final int ENDOFTHEMATCH = 5;

  public static final int WAITFORREFEREECOMMAND = 6;

  /**
   * The coaches are waiting for the last selected contestants to stand in
   * position.
   */

  public static final int ASSEMBLETEAM = 7;

  /**
   * The coaches are following the trial and waiting for the end.
   */

  public static final int WATCHTRIAL = 8;

  public static final int SEATATTHEBENCH = 9;

  /**
   * The Contestants are waiting for the Start Trial.
   */

  public static final int STANDINPOSITION = 10;

  /**
   * The Contestants are pulling the rope.
   */

  public static final int DOYOURBEST = 11;

  
}
