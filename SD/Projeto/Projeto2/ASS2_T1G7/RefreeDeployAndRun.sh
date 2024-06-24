echo "Transfering data to the Refree node."
sshpass -f password ssh sd107@l040101-ws10.ua.pt 'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws10.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirReferee.zip sd107@l040101-ws10.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the Refree node."
sshpass -f password ssh sd107@l040101-ws10.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirReferee.zip'
echo "Executing program at the Refree node."
sshpass -f password ssh sd107@l040101-ws10.ua.pt 'cd test/GameOfTheRope/dirReferee ; java ClientSide.main.ClientGameOfTheRopeReferee l040101-ws07.ua.pt 22163 l040101-ws06.ua.pt 22162 l040101-ws05.ua.pt 22161 l040101-ws01.ua.pt 22160'

