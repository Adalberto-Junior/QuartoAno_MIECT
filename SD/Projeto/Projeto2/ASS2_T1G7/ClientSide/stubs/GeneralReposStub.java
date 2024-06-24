package ClientSide.stubs;

import ClientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the general repository.
 *
 *    It instantiates a remote reference to the general repository.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralReposStub
{
  /**
   *  Name of the platform where is located the general repository server.
   */

   private String serverHostName;

  /**
   *  Port number for listening to service requests.
   */

   private int serverPortNumb;

  /**
   *   Instantiation of a stub to the general repository.
   *
   *     @param serverHostName name of the platform where is located the barber shop server
   *     @param serverPortNumb port number for listening to service requests
   */

   public GeneralReposStub (String serverHostName, int serverPortNumb)
   {
      this.serverHostName = serverHostName;
      this.serverPortNumb = serverPortNumb;
   }

  /**
   *   Operation initialization of the simulation.
   *
   *     @param fileName logging file name
   *     @param strength array with the contestant'strength
   */

   public void initSimul (String fileName, int[][] strength)
   {
      ClientCom com;                                                 // communication channel
      Message outMessage,                                            // outgoing message
              inMessage;                                             // incoming message

      com = new ClientCom (serverHostName, serverPortNumb);
      while (!com.open ())
      { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (MessageType.SETNFIC, fileName, strength);
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();
      if (inMessage.getMsgType() != MessageType.NFICDONE)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      com.close ();
   }

   /**
    * Set referee state.
    *
    * @param state referee state
    */

    public void setRefreeState(int state) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETREFST, state);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set coach state.
      *
      * @param teamId coach team id
      * @param state  coach state
      */
  
     public void setCoachState(int teamId, int state) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETCOAST,teamId, state);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set contestant state.
      *
      * @param tId   team id
      * @param id    contestant id
      * @param state contestant state
      */
  
     public void setContestantState(int tId, int id, int state) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETCONTST,id,tId, state);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set contestant Strength.
      *
      * @param tId      team id
      * @param id       contestant id
      * @param strength contestant Strength
      */
  
     public void setContestantStrength(int tId, int id, int strength) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETCONSTGT,id,tId, strength); 

        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETCONSTGTDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set contestant identification at the position ? at the end of the rope for
      * present trial.
      *
      * @param tId      team id
      * @param id       contestant id
      * @param position contestant position
      */
  
     public void setContestantTrialId(int tId, int id, int position) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETCONTRID,id,tId, position);

        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETCONTRIDDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
              GenericIO.writelnString (inMessage.toString ());
              System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set trial number.
      *
      * @param number trial number
      */
  
     public void setTrialNumber(int number) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETNB,number);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETNBDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set Position of the centre of the rope at the beginning of the trial.
      *
      * @param position Position of the centre of the rope
      */
  
     public void setPositionCentreRope(int position) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETPSCTRO, position);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETPSCTRODONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set Number of the current Game.
      *
      * @param game Number of the current Game
      */
  
     public void setGame(int game) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETNBGM, game);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETNBGMDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set the winning team.
      *
      * @param winTeam Number of winning team
      */
  
     public void setWinTeam(int winTeam) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETWINTEAM, winTeam);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETWINTEAMDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set the winning team.
      *
      * @param score The Score of the match;
      */
  
     public void setScore(int[] score) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETSCORE, score);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETSCOREDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
     /**
      * Set the champion team.
      *
      * @param champion The champion team;
      */
  
     public void setChampion(int champion) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETCHAP, champion);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETCHAPDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
     }
  
   /**
   *   Operation server shutdown.
   *
   *   New operation.
   */

   public void shutdown ()
   {
      ClientCom com;                                                 // communication channel
      Message outMessage,                                            // outgoing message
              inMessage;                                             // incoming message

      com = new ClientCom (serverHostName, serverPortNumb);
      while (!com.open ())
      { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (MessageType.SHUT);
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();
      if (inMessage.getMsgType() != MessageType.SHUTDONE)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      com.close ();
   }
}

