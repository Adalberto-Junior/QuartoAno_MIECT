package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Referee Site.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Referee Site and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class  RefereeSiteInterface 
    {

    /**
     *  Reference to the Referee site.
    */

   private final Refree_site site;

   /**
    *  Instantiation of an interface to the referee site.
    *
    *    @param site reference to the referee site
    */
 
    public RefereeSiteInterface (Refree_site site)
    {
       this.site = site;
    }
 
   /**
    *  Processing of the incoming messages.
    *
    *  Validation, execution of the corresponding method and generation of the outgoing message.
    *
    *    @param inMessage service request
    *    @return service reply
    *    @throws MessageException if the incoming message is not valid
    */
 
    public Message processAndReply (Message inMessage) throws MessageException
    {
       Message outMessage = null;                                     // outgoing message
 
      /* validation of the incoming message */
       switch (inMessage.getMsgType ())
       { case MessageType.ANEWG:   if ((inMessage.getGame () < 0) || (inMessage.getGame () > 3))
                                         throw new MessageException ("Invalid Number of Game!", inMessage);
                                    else if ((inMessage.getRefereeState () < RefreeStates.STARTOFTHEMATCH) || (inMessage.getRefereeState () > RefreeStates.ENDOFTHEMATCH))
                                         throw new MessageException ("Invalid Referee state!", inMessage);
                                     break;
        
         case MessageType.ASSTRDEC: // check nothing
                                    break;   
         case MessageType.CALTR:    if ((inMessage.getNB () <= 0) || (inMessage.getNB () > 6))
                                            throw new MessageException ("Invalid Trial Number!", inMessage);
                                    else if ((inMessage.getRefereeState () < RefreeStates.STARTOFTHEMATCH) || (inMessage.getRefereeState () > RefreeStates.ENDOFTHEMATCH))
                                            throw new MessageException ("Invalid Referee state!", inMessage);
                                    else if ((inMessage.getPositionCenterRop () < -7) || (inMessage.getPositionCenterRop () > 7))
                                            throw new MessageException ("Invalid Position of the center of the Rope!", inMessage);
                                    break;   
         case MessageType.DEGAMWI:  if ((inMessage.getWinningTeam () < 0) || (inMessage.getWinningTeam () > SimulPar.N))
                                        throw new MessageException ("Invalid Team id!", inMessage);
                                    else if ((inMessage.getRefereeState () < RefreeStates.STARTOFTHEMATCH) || (inMessage.getRefereeState () > RefreeStates.ENDOFTHEMATCH))
                                        throw new MessageException ("Invalid Referee state!", inMessage);
                                    break;
         case MessageType.DEMACHWI: if ((inMessage.getRefereeState () < RefreeStates.STARTOFTHEMATCH) || (inMessage.getRefereeState () > RefreeStates.ENDOFTHEMATCH))
                                        throw new MessageException ("Invalid Referee state!", inMessage);
                                    break;
         case MessageType.INFRF:    if ((inMessage.getCoachId () < 0) || (inMessage.getCoachId () >= SimulPar.N))
                                        throw new MessageException ("Invalid Coach id!", inMessage);
                                    else if ((inMessage.getCoachState () < CoachStates.WAITFORREFEREECOMMAND) || (inMessage.getCoachState () > CoachStates.WATCHTRIAL))
                                        throw new MessageException ("Invalid Coach state!", inMessage);
                                    break; 
         case MessageType.SLEEPINCALTRAIL:  // check nothing
                                         break;
         case MessageType.SHUT:     // check nothing
                                    break;
         default:                   throw new MessageException ("Invalid message type!", inMessage);
       }
 
      /* processing */
      switch (inMessage.getMsgType ())
      { case MessageType.ANEWG:  ((RefereeSiteClientProxy) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
                                 int Game = inMessage.getGame ();
                                 site.anounceNewGame (Game); 

                                 outMessage = new Message (MessageType.ANEWGDONE, Game,
                                                        ((RefereeSiteClientProxy) Thread.currentThread ()).getRefreeState ());                       
                                 break;  
       
        case MessageType.ASSTRDEC:  site.assertTriallDecision ();
                                    outMessage = new Message (MessageType.ASSTRDECDONE);
                                    break;  
        case MessageType.CALTR:    ((RefereeSiteClientProxy) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
                                    int NB = inMessage.getNB ();
                                    int PS = inMessage.getPositionCenterRop ();
                                    site.callTrial (NB,PS); 

                                    outMessage = new Message (MessageType.CALTRDONE, NB,PS,
                                                        ((RefereeSiteClientProxy) Thread.currentThread ()).getRefreeState ());                       
                                    break;   
        case MessageType.DEGAMWI:   ((RefereeSiteClientProxy) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
                                    int winTeam = inMessage.getWinningTeam ();
                                    site.declareGameWinner (winTeam); 

                                    outMessage = new Message (MessageType.DEGAMWIDONE, winTeam,
                                                        ((RefereeSiteClientProxy) Thread.currentThread ()).getRefreeState ());                       
                                    break; 
        case MessageType.DEMACHWI: ((RefereeSiteClientProxy) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
                                    site.declareMatchWinner (); 

                                    outMessage = new Message (MessageType.DEMACHWIDONE,
                                                        ((RefereeSiteClientProxy) Thread.currentThread ()).getRefreeState ());                       
                                    break; 
        case MessageType.INFRF:    ((RefereeSiteClientProxy) Thread.currentThread ()).setCoachTeamId (inMessage.getCoachId ());
                                   ((RefereeSiteClientProxy) Thread.currentThread ()).setCoachState (inMessage.getCoachState ());
                                   site.infomeRefree (); 

                                   outMessage = new Message (MessageType.INFRFDONE,
                                                            ((RefereeSiteClientProxy) Thread.currentThread ()).getCoachTeamId (),
                                                            ((RefereeSiteClientProxy) Thread.currentThread ()).getCoachState ());
                                   break;  
        case MessageType.SLEEPINCALTRAIL: site.sleepAfterCallTrial ();
                                        outMessage = new Message (MessageType.SLEEPINCALTRAILDONE);
                                        break; 
        case MessageType.SHUT:     site.shutdown ();
                                   outMessage = new Message (MessageType.SHUTDONE);
                                   break;
      }
 
      return (outMessage);
    }
}
