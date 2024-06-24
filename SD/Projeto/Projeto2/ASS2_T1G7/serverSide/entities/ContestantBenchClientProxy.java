package serverSide.entities;

import serverSide.sharedRegions.*;
import ClientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Service provider agent for access to the Contestant Bench.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ContestantBenchClientProxy extends Thread implements RefereeCloning, CoachCloning, ContestantCloning
{
  /**
   *  Number of instantiayed threads.
   */

   private static int nProxy = 0;

  /**
   *  Communication channel.
   */

   private ServerCom sconi;

  /**
   *  Interface to the contestant bench.
   */

   private ContestantBenchInterface benchInter;

   /**
     * Refree state.
     */
    private int refereeState;

  /**
     * Coach state.
     */
    private int coachState;

    /**
     * Coach team.
     */
    private int teamId;

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
   *  Instantiation of a client proxy.
   *
   *     @param sconi communication channel
   *     @param benchInter interface to the contestant bench
   */

   public ContestantBenchClientProxy (ServerCom sconi, ContestantBenchInterface benchInter)
   {
      super ("ContestantBenchProxy_" + ContestantBenchClientProxy.getProxyId ());
      this.sconi = sconi;
      this.benchInter = benchInter;
   }

  /**
   *  Generation of the instantiation identifier.
   *
   *     @return instantiation identifier
   */

   private static int getProxyId ()
   {
      Class<?> cl = null;                                            // representation of the ContestantBenchClientProxy object in JVM
      int proxyId;                                                   // instantiation identifier

      try
      { cl = Class.forName ("serverSide.entities.ContestantBenchClientProxy");
      }
      catch (ClassNotFoundException e)
      { GenericIO.writelnString ("Data type ContestantBenchClientProxy was not found!");
        e.printStackTrace ();
        System.exit (1);
      }
      synchronized (cl)
      { proxyId = nProxy;
        nProxy += 1;
      }
      return proxyId;
   }

  /**
     * Set Referee state.
     *
     * @param state new Referee state
     */

     public void setRefreeState(int state) {
        refereeState = state;
    }

    /**
     * Get Refree state.
     *
     * @return Refree state
     */

    public int getRefreeState() {
        return refereeState;
    }

    /**
     * Set Coach state.
     *
     * @param state Coach state
     */

    public void setCoachState(int state) {
        coachState = state;
    }

    /**
     * Set Coach Team Id.
     *
     * @param teamId coach team id
     */

    public void setCoachTeamId(int teamId) {
        this.teamId = teamId;
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

    public void addStreangth() {

        if (this.contestantSG < 10) 
            this.contestantSG += 1;
    }

    public void subStreangth() {
        if (this.contestantSG > 6) {
            this.contestantSG -= 1;
        }
    }


  /**
   *  Life cycle of the service provider agent.
   */

   @Override
   public void run ()
   {
      Message inMessage = null,                                      // service request
              outMessage = null;                                     // service reply

     /* service providing */

      inMessage = (Message) sconi.readObject ();                     // get service request
      try
      { outMessage = benchInter.processAndReply (inMessage);         // process it
      }
      catch (MessageException e)
      { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
        GenericIO.writelnString (e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // send service reply
      sconi.close ();                                                // close the communication channel
   }
}


