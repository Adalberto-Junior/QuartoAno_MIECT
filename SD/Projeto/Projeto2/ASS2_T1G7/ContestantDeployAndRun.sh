echo "Transfering data to the Contestant node."
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirContestant.zip sd107@l040101-ws08.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the Contestant node."
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirContestant.zip'

echo "Executing program at the Contestant node."
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'cd test/GameOfTheRope/dirContestant ; java ClientSide.main.ClientGameOfTheRopeContestant l040101-ws07.ua.pt 22163 l040101-ws06.ua.pt 22162 l040101-ws05.ua.pt 22161 l040101-ws01.ua.pt 22160 log'
