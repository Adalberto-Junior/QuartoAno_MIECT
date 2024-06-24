package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import ClientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralReposInterface
{
  /**
   *  Reference to the general repository.
   */

   private final GeneralRepos repos;

  /**
   *  Instantiation of an interface to the general repository.
   *
   *    @param repos reference to the general repository
   */

   public GeneralReposInterface (GeneralRepos repos)
   {
      this.repos = repos;
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
      Message outMessage = null;                                     // mensagem de resposta

     /* validation of the incoming message */
      boolean isEmpty = false; // variavel de controlo;
      int j = 0;
      
      switch (inMessage.getMsgType ())
      { case MessageType.SETNFIC:  
                                    int[][] strength = inMessage.getStrengths();
                                   
                                    for(int i = 0; i <2; i++){
                                        for(int k = 0; k < 5; k++){
                                            if(strength[i][k] < 5){
                                                isEmpty = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (inMessage.getLogFName () == null)
                                      throw new MessageException ("Name of the logging file is not present!", inMessage);
                                      else if (isEmpty)
                                              throw new MessageException ("Strength of Contestants is not present!", inMessage);
                                   break;
        case MessageType.SETCHAP: if ((inMessage.getChampion () < 0) || (inMessage.getChampion () >= 3))
                                    throw new MessageException ("Invalid team number!", inMessage);
                                   break;
        case MessageType.SETCOAST: if ((inMessage.getCoachId () < 0) || (inMessage.getCoachId () >= SimulPar.N))
                                    throw new MessageException ("Invalid Coach id!", inMessage);
                                    else if ((inMessage.getCoachState () < CoachStates.WAITFORREFEREECOMMAND) || (inMessage.getCoachState () > CoachStates.WATCHTRIAL))
                                            throw new MessageException ("Invalid Coach state!", inMessage);
                                  break;
        case MessageType.SETCONTST: if ((inMessage.getContestantTeamId () < 0) || (inMessage.getContestantTeamId () >= SimulPar.N))
                                     throw new MessageException ("Invalid Contestant Team id!", inMessage);
                                   else if ((inMessage.getContestantId () < 0) || (inMessage.getContestantId () >= SimulPar.M))
                                      throw new MessageException ("Invalid Contestant id!", inMessage);
                                    else if ((inMessage.getContestantState () < ContestantStates.SEATATTHEBENCH) || (inMessage.getContestantState () > ContestantStates.DOYOURBEST))
                                          throw new MessageException ("Invalid Contestant state!", inMessage);
                                   break;

        case MessageType.SETCONSTGT: 
                                   
                                    if ((inMessage.getContestantId () < 0) || (inMessage.getContestantId () >= SimulPar.M))
                                       throw new MessageException ("Invalid Contestant id!", inMessage);
                                    else if ((inMessage.getContestantStrength () < 6) || (inMessage.getContestantStrength () > 10))
                                       throw new MessageException ("Invalid Contestant strength!", inMessage);
                                    else if ((inMessage.getContestantTeamId () < 0) || (inMessage.getContestantTeamId () > SimulPar.N))
                                      throw new MessageException ("Invalid Contestant Team id!", inMessage);
                                    
                                    break;
        case MessageType.SETCONTRID: if ((inMessage.getContestantTeamId () < 0) || (inMessage.getContestantTeamId () > SimulPar.N))
                                       throw new MessageException ("Invalid Contestant Team id!", inMessage);
                                      else if ((inMessage.getContestantId () < 0) || (inMessage.getContestantId () > SimulPar.M))
                                        throw new MessageException ("Invalid Contestant id!", inMessage);
                                      else if ((inMessage.getContPosition () < 0) || (inMessage.getContPosition () > 3))
                                        throw new MessageException ("Invalid Contestant trial position!", inMessage);
                                    break;
        case MessageType.SETNBGM: if ((inMessage.getGame () < 0) || (inMessage.getGame () > 3))
                                    throw new MessageException ("Invalid Number of Game!", inMessage);
                                   break;
        case MessageType.SETPSCTRO: if ((inMessage.getPositionCenterRop () < -7) || (inMessage.getPositionCenterRop () > 7))
                                      throw new MessageException ("Invalid Position of the center of the rope!", inMessage);
                                   break;
        case MessageType.SETREFST: if ((inMessage.getRefereeState () < RefreeStates.STARTOFTHEMATCH) || (inMessage.getRefereeState () > RefreeStates.ENDOFTHEMATCH))
                                            throw new MessageException ("Invalid Referee state!", inMessage);
                                   break;
        case MessageType.SETSCORE:  
                                   int scor[] = inMessage.getScore();
                                   isEmpty = false;
                                   if((scor[0] <= 0) && (scor[1] <= 0))
                                     isEmpty = true;

                                   if (isEmpty)
                                           throw new MessageException ("Invalid Score of the match!", inMessage);
                                    break;
        case MessageType.SETNB: if ((inMessage.getNB () <= 0) || (inMessage.getNB () > 6))
                                            throw new MessageException ("Invalid Trial Number!", inMessage);
                                   break;
        case MessageType.SETWINTEAM: if ((inMessage.getWinningTeam () < 0) || (inMessage.getWinningTeam () > SimulPar.N))
                                   throw new MessageException ("Invalid Team id!", inMessage);
                                   break;
        case MessageType.SHUT:     // check nothing
                                   break;
        default:                   throw new MessageException ("Invalid message type!", inMessage);
      }


     /* processing */

     switch (inMessage.getMsgType ())
     { case MessageType.SETNFIC:  
                                    repos.initSimul (inMessage.getLogFName (), inMessage.getStrengths ());
                                    outMessage = new Message (MessageType.NFICDONE);
                                    break;
       case MessageType.SETCHAP: 
                                    repos.setChampion (inMessage.getChampion ());
                                    outMessage = new Message (MessageType.SETCHAPDONE);
                                    break;
                                
        case MessageType.SETCOAST:  repos.setCoachState (inMessage.getCoachId (), inMessage.getCoachState ());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
        case MessageType.SETCONTST: repos.setContestantState (inMessage.getContestantTeamId (),inMessage.getContestantId(), inMessage.getContestantState ());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
       case MessageType.SETCONSTGT: repos.setContestantStrength (inMessage.getContestantTeamId (),inMessage.getContestantId(), inMessage.getContestantStrength ());
                                    outMessage = new Message (MessageType.SETCONSTGTDONE);
                                    break;
       case MessageType.SETCONTRID: repos.setContestantTrialId (inMessage.getContestantTeamId (),inMessage.getContestantId(), inMessage.getContPosition ());
                                    outMessage = new Message (MessageType.SETCONTRIDDONE);
                                    break;
       case MessageType.SETNBGM:    repos.setGame (inMessage.getGame ());
                                    outMessage = new Message (MessageType.SETNBGMDONE);
                                    break;
       case MessageType.SETPSCTRO:  repos.setPositionCentreRope (inMessage.getPositionCenterRop ());
                                    outMessage = new Message (MessageType.SETPSCTRODONE,inMessage.getPositionCenterRop ());
                                    break;
       case MessageType.SETREFST:   repos.setRefreeState (inMessage.getRefereeState ());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
       case MessageType.SETSCORE:   repos.setScore (inMessage.getScore ());
                                    outMessage = new Message (MessageType.SETSCOREDONE);
                                    break;
       case MessageType.SETNB:      repos.setTrialNumber (inMessage.getNB ());
                                    outMessage = new Message (MessageType.SETNBDONE);
                                    break;
       case MessageType.SETWINTEAM: repos.setWinTeam (inMessage.getWinningTeam ());
                                    outMessage = new Message (MessageType.SETWINTEAMDONE);
                                    break;
       case MessageType.SHUT:       repos.shutdown ();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
    }

     return (outMessage);
   }
}


