echo "Transfering data to the Refree site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirRefereeSite.zip sd107@l040101-ws07.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the Refree site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirRefereeSite.zip'

echo "Executing program at the Refree site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'cd test/GameOfTheRope/dirRefereeSite ; java serverSide.main.ServerGameOfTheRopeRefereeSite 22163 l040101-ws01.ua.pt 22160 l040101-ws05.ua.pt 22161 l040101-ws06.ua.pt 22162'
