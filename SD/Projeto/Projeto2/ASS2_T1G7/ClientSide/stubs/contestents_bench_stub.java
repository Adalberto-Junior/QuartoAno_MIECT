package ClientSide.stubs;

import serverSide.main.*;
import ClientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Contestant bench.
 *
 *    It instantiates a remote reference to the contestant bench.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class contestents_bench_stub {

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

    public contestents_bench_stub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

     /**
     * Operation call Contestants.
     *
     * It is called by the coaches when they want to select the Contestant for the
     * next trial.
     * 
     */
    public void callContestants() {

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

        outMessage = new Message(MessageType.CALCONT, (((Coach) Thread.currentThread()).getCoachTeamId()),
                (((Coach) Thread.currentThread()).getCoachState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.CALCONTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Coach) Thread.currentThread ()).setCoachState (inMessage.getCoachState ());
    }

     /**
     * Operation Review Notes.
     *
     * It is called by the coaches when they review the Notes of the trial.
     *
     * 
     */
    public synchronized void reviewNotes() {
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

        outMessage = new Message(MessageType.REVNOT, (((Coach) Thread.currentThread()).getCoachTeamId()),
                (((Coach) Thread.currentThread()).getCoachState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.REVNOTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Coach) Thread.currentThread ()).setCoachState (inMessage.getCoachState ());
    }

    /**
     * Operation Follow Call Advice.
     *
     * It is called by the contestant if he was selected to play.
     *
     * 
     */
    public void followCoachAdvice() {

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

        outMessage = new Message(MessageType.FOLWCOADV,
                (((Contestant) Thread.currentThread()).getContestantID()),
                (((Contestant) Thread.currentThread()).getContestantTeamId()),
                (((Contestant) Thread.currentThread()).getContestantSG()),
                (((Contestant) Thread.currentThread()).getContestantState()));
                
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.FOLWCOADVDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getContestantStrength () < 6) || (inMessage.getContestantStrength () > 10)){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Contestant strength!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
        ((Contestant) Thread.currentThread ()).setContestantSG (inMessage.getContestantStrength ());
    }

    /**
     * Operation Seat Down.
     *
     * It is called by the contestants When they finish the trial
     * and go to bench
     */

    public void seatDown() {

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

        outMessage = new Message(MessageType.SETDOW, 
                (((Contestant) Thread.currentThread()).getContestantID()),
                (((Contestant) Thread.currentThread()).getContestantTeamId()),
                (((Contestant) Thread.currentThread()).getContestantState()));

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SETDOWDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
    }

    /**
     * Operation setContestant.
     *
     * is an auxiliary method that we created to update the allCont strength
     * @param strength contestants's strength
     * 
     */
    
    public  void setContestant(int[][] strength) {
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

        outMessage = new Message(MessageType.SETCONT,strength);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SETCONTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close ();
    }


    /**
     * Operation wakeUpContestant.
     *
     * It is called by the refree when he start the new trial.
     *
     * 
     */
    public  void wakeUpContestant() {
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

        outMessage = new Message(MessageType.WAKUCONT);

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
     * Operation wakeUpCoach.
     *
     * It is called by the refree when he make callTrial.
     *
     */
    public void wakeUpCoach() {
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

        outMessage = new Message(MessageType.WAKUCOA);

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
