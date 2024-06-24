package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Contestant Bench.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Contestant Bench and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class  ContestantBenchInterface 
    {

    /**
     *  Reference to the contestant bench.
    */

   private final Contestants_bench bench;

   /**
    *  Instantiation of an interface to the contestant bench.
    *
    *    @param bench reference to the contestant bench
    */
 
    public ContestantBenchInterface (Contestants_bench bench)
    {
       this.bench = bench;
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
       { case MessageType.CALCONT:   if ((inMessage.getCoachId () < 0) || (inMessage.getCoachId () >= SimulPar.N))
                                        throw new MessageException ("Invalid Coach id!", inMessage);
                                    else if ((inMessage.getCoachState () < CoachStates.WAITFORREFEREECOMMAND) || (inMessage.getCoachState () > CoachStates.WATCHTRIAL))
                                                throw new MessageException ("Invalid Coach state!", inMessage);
                                    break;
         case MessageType.FOLWCOADV:if ((inMessage.getContestantTeamId () < 0) || (inMessage.getContestantTeamId () >= SimulPar.N))
                                        throw new MessageException ("Invalid Contestant Team id!", inMessage);
                                    else if ((inMessage.getContestantId () < 0) || (inMessage.getContestantId () >= SimulPar.M))
                                        throw new MessageException ("Invalid Contestant id!", inMessage);
                                    else if ((inMessage.getContestantStrength () < 6) || (inMessage.getContestantStrength () > 10))
                                        throw new MessageException ("Invalid Contestant strength!", inMessage);
                                    else if ((inMessage.getContestantState () < ContestantStates.SEATATTHEBENCH) || (inMessage.getContestantState () > ContestantStates.DOYOURBEST))
                                            throw new MessageException ("Invalid Contestant state!", inMessage);
                                    break;
         case MessageType.REVNOT:   if ((inMessage.getCoachId () < 0) || (inMessage.getCoachId () >= SimulPar.N))
                                        throw new MessageException ("Invalid Coach id!", inMessage);
                                    else if ((inMessage.getCoachState () < CoachStates.WAITFORREFEREECOMMAND) || (inMessage.getCoachState () > CoachStates.WATCHTRIAL))
                                                throw new MessageException ("Invalid Coach state!", inMessage);
                                    break;
         case MessageType.SETDOW:  if ((inMessage.getContestantTeamId () < 0) || (inMessage.getContestantTeamId () >= SimulPar.N))
                                        throw new MessageException ("Invalid Contestant Team id!", inMessage);
                                    else if ((inMessage.getContestantId () < 0) || (inMessage.getContestantId () >= SimulPar.M))
                                        throw new MessageException ("Invalid Contestant id!", inMessage);
                                    else if ((inMessage.getContestantState () < ContestantStates.SEATATTHEBENCH) || (inMessage.getContestantState () > ContestantStates.DOYOURBEST))
                                            throw new MessageException ("Invalid Contestant state!", inMessage);
                                    break;
         case MessageType.SETCONT:  int[][] strength = inMessage.getStrengths();
                                    for(int i = 0; i <2; i++){
                                        for(int k = 0; k < 5; k++){
                                            if(strength[i][k] < 5){
                                                isEmpty = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (isEmpty)
                                            throw new MessageException ("Strength of Contestants is not present!", inMessage);
                                    break;
         case MessageType.WAKUCOA:  // check nothing
                                    break;   
         case MessageType.WAKUCONT:  // check nothing
                                   break;  
         case MessageType.SHUT:     // check nothing
                                    break;
         default:                   throw new MessageException ("Invalid message type!", inMessage);
       }
 
      /* processing */

      switch (inMessage.getMsgType ())
      { case MessageType.CALCONT:   ((ContestantBenchClientProxy) Thread.currentThread ()).setCoachTeamId (inMessage.getCoachId ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setCoachState (inMessage.getCoachState ());
                                    bench.callContestants (); 
                                    

                                    outMessage = new Message (MessageType.CALCONTDONE,
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getCoachTeamId (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getCoachState ());
                                    break;  
        case MessageType.FOLWCOADV: ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantTeamId (inMessage.getContestantTeamId ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantID (inMessage.getContestantId ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantSG (inMessage.getContestantStrength ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());
                                   
                                    bench.followCoachAdvice (); 

                                    outMessage = new Message (MessageType.FOLWCOADVDONE,
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantSG (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantState ());
                                    break;
        case MessageType.REVNOT:    ((ContestantBenchClientProxy) Thread.currentThread ()).setCoachTeamId (inMessage.getCoachId ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setCoachState (inMessage.getCoachState ());
                                    bench.reviewNotes (); 

                                    outMessage = new Message (MessageType.REVNOTDONE,
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getCoachTeamId (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getCoachState ());
                                    break;  
        case MessageType.SETDOW:    ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantTeamId (inMessage.getContestantTeamId ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantID (inMessage.getContestantId ());
                                    ((ContestantBenchClientProxy) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());
                                    bench.seatDown (); 

                                    outMessage = new Message (MessageType.SETDOWDONE,
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantID (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantTeamId (),
                                                            ((ContestantBenchClientProxy) Thread.currentThread ()).getContestantState ());
                                    break;
        case MessageType.SETCONT:   
        
                                    bench.setContestant (inMessage.getStrengths ()); //força e id;
                                    outMessage = new Message (MessageType.SETCONTDONE,inMessage.getStrengths ());
                                    break; 
        case MessageType.WAKUCOA:   bench.wakeUpCoach ();
                                    outMessage = new Message (MessageType.SACK);
                                    break; 
        case MessageType.WAKUCONT:  bench.wakeUpContestant ();
                                    outMessage = new Message (MessageType.SACK);
                                    break;  
        case MessageType.SHUT:      bench.shutdown ();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
        default:                   throw new MessageException ("Invalid message type!", inMessage);
      }
 
      return (outMessage);
    }
}


