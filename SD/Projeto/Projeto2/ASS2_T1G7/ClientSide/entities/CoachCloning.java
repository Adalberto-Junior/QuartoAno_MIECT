package ClientSide.entities;
import ClientSide.main.SimulPar;
import ClientSide.stubs.*;
/**
 *    Coach cloning.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */

public interface CoachCloning {

     /**
     * Set Coach state.
     *
     * @param state new Coach state
     */

     public void setCoachState(int state); 

    /**
     * Set Coach Team Id.
     *
     * @param teamId new Refree state
     */

    public void setCoachTeamId(int teamId); 

    /*
     * Set new Reference to the contestant bench.
     *
     * @param bench Contestant Strength
     */

   // public void setContestantBench(contestents_bench_stub bench);

    /**
     * Get Coach state.
     *
     * @return Coach state
     */

    public int getCoachState();

    /**
     * Get Coach team Id.
     *
     * @return TeamId
     */

    public int getCoachTeamId();
} 
    

