echo "Transfering data to the Coach node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirCoach.zip sd107@l040101-ws09.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the Coach node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirCoach.zip'

echo "Executing program at the Coach node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'cd test/GameOfTheRope/dirCoach ; java ClientSide.main.ClientGameOfTheRopeCoach l040101-ws07.ua.pt 22163 l040101-ws06.ua.pt 22162 l040101-ws05.ua.pt 22161 l040101-ws01.ua.pt 22160'
