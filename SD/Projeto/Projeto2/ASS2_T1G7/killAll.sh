echo "Killing General Repository Java process"
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'killall -9 -u sd107 java'

echo "Killing play Ground java process"
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'killall -9 -u sd107 java'

echo "Killing Contestant Bench java process"
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'killall -9 -u sd107 java'

echo "Killing Referee Site Java process."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'killall -9 -u sd107 java'

echo "Killing Contestant Java process"
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'killall -9 -u sd107 java'

echo "Killing Coach Java process"
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'killall -9 -u sd107 java'

echo "Killing Referee Java process"
sshpass -f password ssh sd107@l040101-ws10.ua.pt 'killall -9 -u sd107 java'

