echo "Transfering data to the Conetentant Bench node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirContestantBench.zip sd107@l040101-ws06.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the Conetentant Bench node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirContestantBench.zip'

echo "Executing program at the Conetentant Bench node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/GameOfTheRope/dirContestantBench ; java serverSide.main.ServerGameOfTheRopeContestantBench 22162 l040101-ws01.ua.pt 22160 l040101-ws05.ua.pt 22161'

