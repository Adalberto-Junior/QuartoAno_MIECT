package ClientSide.stubs;

import serverSide.main.*;
import ClientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Referee Site.
 *
 *    It instantiates a remote reference to the Referee Site.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class RefreeSiteStub {

    /**
     * Name of the platform where is located the barber shop server.
     */

    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     * Instantiation of a stub to the barber shop.
     *
     * @param serverHostName name of the platform where is located the barber shop
     *                       server
     * @param serverPortNumb port number for listening to service requests
     */

    public RefreeSiteStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation anounceNewGame.
     *
     * It is called by the refree When he start a new Game.
     * 
     * @param nOfGame Current game number;
     */
    public void anounceNewGame(int nOfGame) {

        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.ANEWG, nOfGame, (((Refree) Thread.currentThread()).getRefreeState()));
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.ANEWGDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Refree) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
    }

    /**
     * Operation callTrial.
     *
     * It is called by the refree When he start a new Trial.
     * 
     * @param NB Current Trial number;
     * @param PS position of the center of the rope;
     */
    public void callTrial(int NB,int PS) {
        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CALTR, NB,PS, (((Refree) Thread.currentThread()).getRefreeState()));
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.CALTRDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Refree) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
    }

    /**
     * Operation Sleep after callTrial.
     *
     * It is called by the refree When he finish call trial and wake up the coach.
     * 
     */
    public  void sleepAfterCallTrial() {
        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.SLEEPINCALTRAIL);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SLEEPINCALTRAILDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }

    /**
     * Operation declareGameWinner.
     *
     * It is called by the refree When he finish the current Game.
     * @param winTeam  Winner Team
     */
    public void declareGameWinner(int winTeam) {
        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.DEGAMWI, winTeam, (((Refree) Thread.currentThread()).getRefreeState()));
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.DEGAMWIDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Refree) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
    }


    /**
     * Operation declareMatchWinner.
     *
     * It is called by the refree When he finish the Match.
     * 
     */
    public void declareMatchWinner() {
        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.DEMACHWI, (((Refree) Thread.currentThread()).getRefreeState()));
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.DEMACHWIDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Refree) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());

    }

     /**
     * Operation informe Referee.
     *
     * It is called by the Coach When he finish Assemble the team.
     *
     */
    public void infomeRefree() {
        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.INFRF, (((Coach) Thread.currentThread()).getCoachTeamId()),
                (((Coach) Thread.currentThread()).getCoachState()));
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.INFRFDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Coach) Thread.currentThread ()).setCoachState (inMessage.getCoachState ());
    }

     /**
     * Operation assertTriallDecision().
     *
     * It is called by the refree When he finish the trial and wake up the Coach.
     * 
     */
    public void assertTriallDecision() {
        ClientCom com; // communication channel
        Message outMessage, inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) // waits for a connection to be established
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.ASSTRDEC);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.ASSTRDECDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
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
