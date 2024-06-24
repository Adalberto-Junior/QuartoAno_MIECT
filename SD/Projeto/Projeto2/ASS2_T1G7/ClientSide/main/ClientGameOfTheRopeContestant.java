package ClientSide.main;

import ClientSide.entities.*;
import ClientSide.stubs.*;
import serverSide.main.*;
import commInfra.*;
import genclass.GenericIO;
import java.util.Random;
/**
 * Client side of the Game of the Rope (Contestant).
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class ClientGameOfTheRopeContestant {
    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - name of the platform where is located the Referee Site
     *             server
     *             args[1] - port nunber for listening to service requests
     *             args[2] - name of the platform where is located the Contestant
     *             Bench server
     *             args[3] - port nunber for listening to service requests
     *             args[4] - name of the platform where is located the Play Ground
     *             server
     *             args[5] - port nunber for listening to service requests
     *             args[6] - name of the platform where is located the general
     *             repository server
     *             args[7] - port nunber for listening to service requests
     *             args[8] - name of the logging file
     */

    public static void main(String[] args) {
        String refereeSiteServerHostName; // name of the platform where is located the referee site server
        int refereeSiteServerPortNumb = -1; // port number for listening to service requests
        String contestantBenchServerHostName; // name of the platform where is located the contestant bench server
        int contestantBenchServerPortNumb = -1; // port number for listening to service requests
        String playGroundServerHostName; // name of the platform where is located the play ground server
        int playGroundServerPortNumb = -1; // port number for listening to service requests
        String genReposServerHostName; // name of the platform where is located the general repository server
        int genReposServerPortNumb = -1; // port number for listening to service requests
        String fileName; // name of the logging file
        int[][] strength; // contestant'strength
        Contestant[][] contestants; // Contestant

        RefreeSiteStub siteStub; // remote reference to the referee site
        contestents_bench_stub benchStub; // remote reference to the contestant bench
        PlayGroundStub plGrndStub; // remote reference to the play ground
        GeneralReposStub genReposStub; // remote reference to the general repository

        /* getting problem runtime parameters */

        if (args.length != 9) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        refereeSiteServerHostName = args[0];
        try {
            refereeSiteServerPortNumb = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if ((refereeSiteServerPortNumb < 22160) || (refereeSiteServerPortNumb >= 22170)) {
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }
        contestantBenchServerHostName = args[2];
        try {
            contestantBenchServerPortNumb = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((contestantBenchServerPortNumb < 22160) || (contestantBenchServerPortNumb >= 22170)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }
        playGroundServerHostName = args[4];
        try {
            playGroundServerPortNumb = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((playGroundServerPortNumb < 22160) || (playGroundServerPortNumb >= 22170)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }
        genReposServerHostName = args[6];
        try {
            genReposServerPortNumb = Integer.parseInt(args[7]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((genReposServerPortNumb < 22160) || (genReposServerPortNumb >= 22170)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }
        fileName = args[8];

        /* problem initialization */

        siteStub = new RefreeSiteStub(refereeSiteServerHostName, refereeSiteServerPortNumb);
        benchStub = new contestents_bench_stub(contestantBenchServerHostName, contestantBenchServerPortNumb);
        plGrndStub = new PlayGroundStub(playGroundServerHostName, playGroundServerPortNumb);
        genReposStub = new GeneralReposStub(genReposServerHostName, genReposServerPortNumb);

        Random random = new Random();

        strength = new int[SimulPar.T][SimulPar.M];
        for (int i = 0; i < SimulPar.N; i++) {
            for (int k = 0; k < SimulPar.M; k++) {
                int randomNumber = 0;
                do {
                    randomNumber = random.nextInt(11);
                } while (randomNumber < 6);

                strength[i][k] = randomNumber;
            }
        }
        genReposStub.initSimul(fileName, strength);
        benchStub.setContestant(strength);
        contestants = new Contestant[SimulPar.N][SimulPar.M];

        for (int i = 0; i < SimulPar.N; i++) {
            for (int k = 0; k < SimulPar.M; k++) {
                contestants[i][k] = new Contestant("Cont_" + (k + 1), (k), (i), strength[i][k], benchStub, plGrndStub);
            }
        }
        
        /* start of the simulation */
        for (int i = 0; i < SimulPar.N; i++) {
            for (int k = 0; k < SimulPar.M; k++) {
                contestants[i][k].start();
            }
        }

        /* waiting for the end of the simulation */

        GenericIO.writelnString();
        for (int i = 0; i < SimulPar.N; i++) {
            for (int k = 0; k < SimulPar.M; k++) {
                try {
                    contestants[i][k].join();
                } catch (InterruptedException e) {
                }
                GenericIO.writelnString(
                        "The contestant of teamId " + (i + 1) + " with id " + (k + 1) + " has terminated.");

            }
        }

        GenericIO.writelnString();
        siteStub.shutdown();
        benchStub.shutdown();
        plGrndStub.shutdown();
        genReposStub.shutdown();
    }
}