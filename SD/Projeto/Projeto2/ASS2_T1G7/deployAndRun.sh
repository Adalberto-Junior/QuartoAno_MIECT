# loop 100 times
for i in $(seq 1 1); do
    echo "Start of iteration $i"

    xterm -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
    sleep 1
    xterm -T "Play Ground" -hold -e "./PlayGroundDeployAndRun.sh" &
    sleep 1
    xterm -T "Contestant Bench" -hold -e "./ContestantBenchDeployAndRun.sh" &
    sleep 1
    xterm -T "Referee Site" -hold -e "./RefreeSiteDeployAndRun.sh" &
    sleep 1
    xterm -T "Contestant" -hold -e "./ContestantDeployAndRun.sh" &
    sleep 1
    xterm -T "Coach" -hold -e "./CoachDeployAndRun.sh" &
    sleep 1
    xterm -T "Referee" -hold -e "./RefreeDeployAndRun.sh"

    echo "Downloading logger from the General Repository node."
    rm -rf /home/rosario/Documents/SD/SD_WORK_2/remote_logs
    mkdir /home/rosario/Documents/SD/SD_WORK_2/remote_logs
    sshpass -f password scp sd107@l040101-ws01.ua.pt:test/GameOfTheRope/dirGeneralRepos/log /home/rosario/Documents/SD/SD_WORK_2/remote_logs
    
    echo "End of iteration $i"
done