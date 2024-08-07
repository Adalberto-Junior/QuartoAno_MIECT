echo "Transfering data to the general repository node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt  'mkdir -p test/GameOfTheRope'
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'rm -rf test/GameOfTheRope/*'
sshpass -f password scp dirGeneralRepos.zip sd107@l040101-ws01.ua.pt:test/GameOfTheRope

echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/GameOfTheRope ; unzip -uq dirGeneralRepos.zip'

echo "Executing program at the server general repository."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/GameOfTheRope/dirGeneralRepos ; java serverSide.main.ServerGameOfTheRopeGeneralRepos 22160'

echo "Server shutdown."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/GameOfTheRope/dirGeneralRepos ; less stat'

