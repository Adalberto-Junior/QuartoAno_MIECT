package ClientSide.stubs;

import serverSide.main.*;
import ClientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Play Ground.
 *
 *    It instantiates a remote reference to the play ground.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class PlayGroundStub {

    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     * Instantiation of a stub to the Play Ground.
     *
     * @param serverHostName name of the platform where is located the play ground
     *                       server
     * @param serverPortNumb port number for listening to service requests
     */

    public PlayGroundStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }
    
    /**
     * Operation Start Trial.
     *
     * It is called by the refree when he want to start the trial.
     * 
     */

    public void startTrial() {

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

        outMessage = new Message(MessageType.STRTTR, (((Refree) Thread.currentThread()).getRefreeState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.STRTTRDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Refree) Thread.currentThread ()).setRefreeState (inMessage.getRefereeState ());
    }

     /**
     * Operation GetRead.
     *
     * It is called by the contestants When they ready to start pulling the rope.
     * 
     */

    public void getRead() {

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

        outMessage = new Message(MessageType.GETREADY,
                (((Contestant) Thread.currentThread()).getContestantID()),
                (((Contestant) Thread.currentThread()).getContestantTeamId()),
                (((Contestant) Thread.currentThread()).getContestantState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GETREADYDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Contestant) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());
    }

    /**
     * Operation Pull The Rope.
     *
     * It is called by the contestants When they ready are pulling the rope.
     * 
     *
     */

    public void pullTheRope() {

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

        outMessage = new Message(MessageType.PUTHROP, 
                (((Contestant) Thread.currentThread()).getContestantID()),
                (((Contestant) Thread.currentThread()).getContestantTeamId()),
                (((Contestant) Thread.currentThread()).getContestantSG()),
                (((Contestant) Thread.currentThread()).getContestantState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.PUTHROPDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Contestant) Thread.currentThread ()).setContestantState (inMessage.getContestantState ());
        ((Contestant) Thread.currentThread ()).setContestantSG (inMessage.getContestantStrength ());
    }

    /**
     * Operation Am Done.
     *
     * It is called by the contestants When they finish the trial.
     * The last contestant will wake up the refree;
     * 
     */
    public void amDone() {

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

        outMessage = new Message(MessageType.AMDONE, 
                (((Contestant) Thread.currentThread()).getContestantID()),
                (((Contestant) Thread.currentThread()).getContestantTeamId()),
                (((Contestant) Thread.currentThread()).getContestantState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.AMDONEREPLY) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }


     /**
     * Operation assertTriallDecision.
     *
     * It is called by the refree When he finish the trial.
     * 
     * @return winTean The trail's winning team id;
     */

    public int assertTriallDecision() {

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
        return (inMessage.getWinningTeam ());
    }
     /**
     * Operation endOfMatch.
     *
     * is an auxiliary method that we created to update the endOfMatch
     * 
     * @param NG Number of the match(Game)
     * 
     */
    public void endOfMatch(int NG) {
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
    
            outMessage = new Message(MessageType.ENDOFMA,NG);
    
            com.writeObject(outMessage);
            inMessage = (Message) com.readObject();
            if (inMessage.getMsgType() != MessageType.ENDOFMAREPLY) {
                GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
                GenericIO.writelnString(inMessage.toString());
                System.exit(1);
            }
            if ((inMessage.getGame () < 0) || (inMessage.getGame () > 3)){
                GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Number of Game!");
                GenericIO.writelnString(inMessage.toString());
                System.exit(1);
            }
            com.close ();
    }

     /**
     * Operation endOfMatch.
     *
     * is an auxiliary method that we created to take the endOfMatch
     * 
     * @return true if the the match end or not if it not end
     * 
     */
    public boolean getEndOfMatch() {

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

        outMessage = new Message(MessageType.GETENDOFMA);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GETENDOFMADONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        com.close ();
        return inMessage.getEndOfMatch ();
    }

    /**
     * Operation callTrial.
     *
     * is an method call by refree to wake up the coach when he make callTrial
     * 
     * 
     */
    public void callTrial() {
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

        outMessage = new Message(MessageType.CALTR);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.CALTRDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }

/**
     * Operation declareMatchWinner.
     *
     * is an method call by refree when he make declareMatchWinner
     * 
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

        outMessage = new Message(MessageType.DEMACHWI);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.DEMACHWIDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }

    /**
     * Operation setStrength.
     *
     * is an auxiliary method that we created to update the Strength array.
     * @param strength contestants's array strength
     * 
     */
    public void setStrength(int[][] strength) {
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

        outMessage = new Message(MessageType.SETSTGT,strength);


        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SETSTGTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }

    /**
     * getPositionCenterRope.
     *
     * is an auxiliary method that return the center Rope
     * @return position of the center of the rope
     * 
     */
    public int getPositionCenterRope() {
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

        outMessage = new Message(MessageType.GETPSCTR);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GETPSCTRDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getPositionCenterRop () < -7) && (inMessage.getPositionCenterRop () > 7)  ) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid position of the center of the rope!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        return inMessage.getPositionCenterRop ();
    }

    /**
     * Operation getTrialDecision.
     *
     * is an auxiliary method we created to indicate if there is already
     * a winner and end the game if positive
     * 
     * @return true if there is already a winner and false if there is not
     */
    public boolean getTrialDecision() {

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

        outMessage = new Message(MessageType.GETTRIDEC);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GETTRIDECDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        com.close ();
        return (inMessage.getAssertDecision ());
    }

    /**
     * Operation timeToSleap.
     *
     * is an auxiliary method to bolck the refree after startTrial
     *
     */
    public  void timeToSleap() {

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

        outMessage = new Message(MessageType.REGOSLEP);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.REGOSLEPDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }

   /**
     * Operation resetScore.
     *
     * is an auxiliary method to reset the score and Position of the Center of the Rope when the referee announce new game 
     *
     */
    public void resetScore() {
    	
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

        outMessage = new Message(MessageType.RESTSCOR);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SACK) {
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
