package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Play Ground.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Play Ground and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class PlayGroundInterface {

    /**
     *  Reference to the play ground.
    */

   private final PlayGround plGrnd;

   /**
    *  Instantiation of an interface to the play ground.
    *
    *    @param plGrnd reference to the play ground
    */
 
    public PlayGroundInterface (PlayGround plGrnd)
    {
       this.plGrnd = plGrnd;
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
        boolean isEmpty = false; // variavel de controlo;
       switch (inMessage.getMsgType ())
       { case MessageType.AMDONE:   if ((inMessage.getContestantState () < ContestantStates.SEATATTHEBENCH) || (inMessage.getContestantState () > ContestantStates.DOYOURBEST))
                                        throw new MessageException ("Invalid Contestant state!", inMessage);
                                    break;
         case MessageType.ASSTRDEC:  // check nothing
                                    break;
         case MessageType.CALTR:    // check nothing
                                    break;
         case MessageType.DEMACHWI:  // check nothing
                                    break;
         case MessageType.ENDOFMA:  if ((inMessage.getGame () < 0) || (inMessage.getGame () > 3))
                                     throw new MessageException ("Invalid Number of Game!", inMessage);
                                    break;
         case MessageType.GETENDOFMA:  // check nothing
                                    break;   
         case MessageType.GETPSCTR:  // check nothing
                                   break;  
         case MessageType.GETREADY:  if ((inMessage.getContestantTeamId () < 0) || (inMessage.getContestantTeamId () >= SimulPar.N))
                                        throw new MessageException ("Invalid Contestant Team id!", inMessage);
                                    else if ((inMessage.getContestantId () < 0) || (inMessage.getContestantId () >= SimulPar.M))
                                        throw new MessageException ("Invalid Contestant id!", inMessage);
                                    else if ((inMessage.getContestantState () < ContestantStates.SEATATTHEBENCH) || (inMessage.getContestantState () > ContestantStates.DOYOURBEST))
                                            throw new MessageException ("Invalid Contestant state!", inMessage);
                                    break;
         case MessageType.GETTRIDEC:  // check nothing
                                   break;
         case MessageType.PUTHROP:  if ((inMessage.getContestantState () < ContestantStates.SEATATTHEBENCH) || (inMessage.getContestantState () > ContestantStates.DOYOURBEST))
                                       throw new MessageException ("Invalid Contestant state!", inMessage);
                                    break;
         case MessageType.RESTSCOR:  // check nothing
                                    break;
         case MessageType.SETSTGT:  int[][] strength = inMessage.getSGTotal ();
                                    if(strength[0][0] < 5 && strength[1][0] < 5 )
                                         isEmpty = true;
                            
                                    if (isEmpty)
                                            throw new MessageException ("Sum's Strength of Contestants is not present!", inMessage);
                                    break;
         case MessageType.STRTTR:  if ((inMessage.getRefereeState () < RefreeStates.STARTOFTHEMATCH) || (inMessage.getRefereeState () > RefreeStates.ENDOFTHEMATCH))
                                        throw new MessageException ("Invalid Referee state!", inMessage);
                                    break;
         case MessageType.REGOSLEP:  // check nothing
                                    break;
         case MessageType.SHUT:     // check nothing
                                    break;
         default:                   throw new MessageException ("Invalid message type!", inMessage);
       }
 
      /* processing */
       
      switch (inMessage.getMsgType ())
      { case MessageType.AMDONE: ((PlayGroundClientProxy) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());
                                 plGrnd.amDone ();
                                 outMessage = new Message (MessageType.AMDONEREPLY);
                                 break; 
        case MessageType.ASSTRDEC:  int winTeam = plGrnd.assertTriallDecision ();
                                    outMessage = new Message (MessageType.ASSTRDECDONE,winTeam);
                                    break; 
        case MessageType.CALTR:     plGrnd.callTrial ();
                                    outMessage = new Message (MessageType.CALTRDONE);
                                    break; 
        case MessageType.DEMACHWI: plGrnd.declareMatchWinner ();
                                   outMessage = new Message (MessageType.DEMACHWIDONE);
                                   break; 
        case MessageType.ENDOFMA:  plGrnd.endOfMatch (inMessage.getGame());
                                   outMessage = new Message (MessageType.ENDOFMAREPLY,inMessage.getGame());
                                   break;
        case MessageType.GETENDOFMA:boolean asserted = plGrnd.getEndOfMatch ();
                                    outMessage = new Message (MessageType.GETENDOFMADONE,asserted);
                                    break; 
        case MessageType.GETPSCTR:  int PS = plGrnd.getPositionCenterRope ();
                                    outMessage = new Message (MessageType.GETPSCTRDONE,PS);
                                    break;  
        case MessageType.GETREADY:  ((PlayGroundClientProxy) Thread.currentThread ()).setContestantTeamId (inMessage.getContestantTeamId ());
                                    ((PlayGroundClientProxy) Thread.currentThread ()).setContestantID (inMessage.getContestantId ());
                                    ((PlayGroundClientProxy) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());
                                    plGrnd.getRead (); 
        
                                    outMessage = new Message (MessageType.GETREADYDONE,
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantID (),
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantState ());
                                   break;
        case MessageType.GETTRIDEC: boolean decision = plGrnd.getTrialDecision ();
                                    outMessage = new Message (MessageType.GETTRIDECDONE,decision);
                                    break;  
        case MessageType.PUTHROP:
                                   ((PlayGroundClientProxy) Thread.currentThread ()).setContestantTeamId (inMessage.getContestantTeamId ());
                                   ((PlayGroundClientProxy) Thread.currentThread ()).setContestantID (inMessage.getContestantId ());
                                   ((PlayGroundClientProxy) Thread.currentThread ()).setContestantSG (inMessage.getContestantStrength ());
                                   ((PlayGroundClientProxy) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());

                                   plGrnd.pullTheRope (); 

                                   outMessage = new Message (MessageType.PUTHROPDONE,
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantID (),
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantSG (),
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getContestantState ());
                                                               
                                    break;
        case MessageType.RESTSCOR:  plGrnd.resetScore ();
                                    outMessage = new Message (MessageType.SACK);
                                    break; 
        case MessageType.SETSTGT:  plGrnd.setStrength (inMessage.getSGTotal ());
                                   outMessage = new Message (MessageType.SETSTGTDONE,inMessage.getSGTotal ());
                                   break; 
        case MessageType.STRTTR:  ((PlayGroundClientProxy) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
                                  plGrnd.startTrial (); 

                                  outMessage = new Message (MessageType.STRTTRDONE,
                                                            ((PlayGroundClientProxy) Thread.currentThread ()).getRefreeState ());
                                  break;
        case MessageType.REGOSLEP:  plGrnd.timeToSleap ();
                                    outMessage = new Message (MessageType.REGOSLEPDONE);
                                    break; 
        case MessageType.SHUT:    plGrnd.shutdown ();
                                  outMessage = new Message (MessageType.SHUTDONE);
                                  break;
      }
 
      return (outMessage);
    }
}
