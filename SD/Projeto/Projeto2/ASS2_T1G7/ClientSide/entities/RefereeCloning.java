package ClientSide.entities;

import ClientSide.stubs.*;
/**
 *    Referee cloning.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */

public interface RefereeCloning {
    /**
     * Set Refree state.
     *
     * @param state new Refree state
     */

     public void setRefreeState(int state);

    /*
     * Set new Reference to the contestant bench.
     *
     * @param bench Contestant Strength
     */

    //public void setContestantBench(contestents_bench_stub bench);

    /**
     * Get Refree state.
     *
     * @return Refree state
     */

    public int getRefreeState();

}
