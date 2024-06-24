package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import ClientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 *    Server side of the Referee Site of Information.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ServerGameOfTheRopeRefereeSite 
{
  /**
   *  Flag signaling the service is active.
   */

   public static boolean waitConnection;

  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - port nunber for listening to service requests
   *        args[1] - name of the platform where is located the server for the general repository
   *        args[2] - port nunber where the server for the general repository is listening to service requests
   *        args[3] - name of the platform where is located the server for the play ground
   *        args[4] - port nunber where the server for the play ground is listening to service requests
   *        args[5] - name of the platform where is located the server for the contestant bench
   *        args[6] - port nunber where the server for the contestant bench is listening to service requests
   */

   public static void main (String [] args)
   {
      Refree_site site;                                              // referee site (service to be rendered)
      RefereeSiteInterface siteInter;                                // interface to the referee site
      GeneralReposStub reposStub;                                    // stub to the general repository
      PlayGroundStub plGrndStub;                                     // stub to the play ground
      contestents_bench_stub benchStub;                              // stub to the play ground
      ServerCom scon, sconi;                                         // communication channels
      int portNumb = -1;                                             // port number for listening to service requests
      String reposServerName;                                        // name of the platform where is located the server for the general repository
      int reposPortNumb = -1;                                        // port nunber where the server for the general repository is listening to service requests
      String plGrndServerName;                                       // name of the platform where is located the server for the play ground
      int plGrndPortNumb = -1;                                       // port nunber where the server for the play ground is listening to service requests
      String benchServerName;                                        // name of the platform where is located the server for the contestant bench
      int benchPortNumb = -1;                                        // port nunber where the server for the contestant bench is listening to service requests      

      if (args.length != 7)
         { GenericIO.writelnString ("Wrong number of parameters!");
           System.exit (1);
         }
      try
      { portNumb = Integer.parseInt (args[0]);
      }
      catch (NumberFormatException e)
      { GenericIO.writelnString ("args[0] is not a number!");
        System.exit (1);
      }
      if ((portNumb < 22160) || (portNumb >= 22170))
         { GenericIO.writelnString ("args[0] is not a valid port number!");
           System.exit (1);
         }
      reposServerName = args[1];
      try
      { reposPortNumb = Integer.parseInt (args[2]);
      }
      catch (NumberFormatException e)
      { GenericIO.writelnString ("args[2] is not a number!");
        System.exit (1);
      }
      if ((reposPortNumb < 22160) || (reposPortNumb >= 22170))
         { GenericIO.writelnString ("args[2] is not a valid port number!");
           System.exit (1);
         }
      plGrndServerName = args[3];
      try
      { plGrndPortNumb = Integer.parseInt (args[4]);
      }
      catch (NumberFormatException e)
      { GenericIO.writelnString ("args[4] is not a number!");
         System.exit (1);
      }
      if ((plGrndPortNumb < 22160) || (plGrndPortNumb >= 22170))
      { GenericIO.writelnString ("args[4] is not a valid port number!");
         System.exit (1);
      }
      benchServerName = args[5];
      try
      { benchPortNumb = Integer.parseInt (args[6]);
      }
      catch (NumberFormatException e)
      { GenericIO.writelnString ("args[6] is not a number!");
         System.exit (1);
      }
      if ((benchPortNumb < 22160) || (benchPortNumb >= 22170))
      { GenericIO.writelnString ("args[6] is not a valid port number!");
         System.exit (1);
      }

     /* service is established */

      reposStub = new GeneralReposStub (reposServerName, reposPortNumb);               // communication to the general repository is instantiated
      plGrndStub = new PlayGroundStub (plGrndServerName, plGrndPortNumb);             // communication to the play ground is instantiated
      benchStub = new contestents_bench_stub (benchServerName, benchPortNumb);       // communication to the contestant bench is instantiated
      site = new Refree_site (reposStub, benchStub, plGrndStub);                    // service is instantiated
      siteInter = new RefereeSiteInterface (site);                                 // interface to the service is instantiated
      scon = new ServerCom (portNumb);                                            // listening channel at the public port is established
      scon.start ();
      GenericIO.writelnString ("Service is established!");
      GenericIO.writelnString ("Server is listening for service requests.");

     /* service request processing */

     RefereeSiteClientProxy cliProxy;                                // service provider agent

      waitConnection = true;
      while (waitConnection)
      { try
        { sconi = scon.accept ();                                      // enter listening procedure
          cliProxy = new RefereeSiteClientProxy (sconi, siteInter);   // start a service provider agent to address
          cliProxy.start ();                                         //  the request of service
        }
        catch (SocketTimeoutException e) {}
      }
      scon.end ();                                                   // operations termination
      GenericIO.writelnString ("Server was shutdown.");
   }
}

