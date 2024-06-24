echo "Transfering data to the PlayGround node."
sshpass -f password ssh  sd107@l040101-ws05.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirPlayGround.zip sd107@l040101-ws05.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the PlayGround node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirPlayGround.zip'

echo "Executing program at the PlayGround node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'cd test/GameOfTheRope/dirPlayGround ; java serverSide.main.ServerGameOfTheRopePlayGround 22161 l040101-ws01.ua.pt 22160'

