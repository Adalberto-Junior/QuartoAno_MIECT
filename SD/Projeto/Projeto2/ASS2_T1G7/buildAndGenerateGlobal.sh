echo "Compiling source code."
javac -source 8 -target 8 -cp lib/genclass.jar */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."
echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions \
         dirGeneralRepos/ClientSide dirGeneralRepos/ClientSide/entities dirGeneralRepos/commInfra
cp  serverSide/main/SimulPar.class  serverSide/main/ServerGameOfTheRopeGeneralRepos.class dirGeneralRepos/serverSide/main
cp  serverSide/entities/GeneralReposClientProxy.class dirGeneralRepos/serverSide/entities
cp  serverSide/sharedRegions/GeneralReposInterface.class  serverSide/sharedRegions/GeneralRepos.class dirGeneralRepos/serverSide/sharedRegions
cp  ClientSide/entities/RefreeStates.class  ClientSide/entities/CoachStates.class  ClientSide/entities/ContestantStates.class dirGeneralRepos/ClientSide/entities
cp  commInfra/Message.class  commInfra/MessageType.class  commInfra/MessageException.class  commInfra/ServerCom.class dirGeneralRepos/commInfra

echo "  Play Ground" 
rm -rf dirPlayGround
mkdir -p dirPlayGround dirPlayGround/serverSide dirPlayGround/serverSide/main dirPlayGround/serverSide/entities dirPlayGround/serverSide/sharedRegions \
         dirPlayGround/ClientSide dirPlayGround/ClientSide/entities dirPlayGround/ClientSide/stubs dirPlayGround/commInfra
cp  serverSide/main/SimulPar.class  serverSide/main/ServerGameOfTheRopePlayGround.class dirPlayGround/serverSide/main
cp  serverSide/entities/PlayGroundClientProxy.class dirPlayGround/serverSide/entities
cp  serverSide/sharedRegions/GeneralReposInterface.class  serverSide/sharedRegions/PlayGroundInterface.class  serverSide/sharedRegions/PlayGround.class dirPlayGround/serverSide/sharedRegions
cp  ClientSide/entities/RefreeStates.class  ClientSide/entities/CoachStates.class  ClientSide/entities/ContestantStates.class   ClientSide/entities/RefereeCloning.class  ClientSide/entities/CoachCloning.class  ClientSide/entities/ContestantCloning.class \
   dirPlayGround/ClientSide/entities
cp  ClientSide/stubs/GeneralReposStub.class dirPlayGround/ClientSide/stubs
cp  commInfra/*.class dirPlayGround/commInfra

echo "  Contestant Bench"
rm -rf dirContestantBench
mkdir -p dirContestantBench dirContestantBench/serverSide dirContestantBench/serverSide/main dirContestantBench/serverSide/entities dirContestantBench/serverSide/sharedRegions \
         dirContestantBench/ClientSide dirContestantBench/ClientSide/entities dirContestantBench/ClientSide/stubs dirContestantBench/commInfra
cp  serverSide/main/SimulPar.class  serverSide/main/ServerGameOfTheRopeContestantBench.class dirContestantBench/serverSide/main
cp  serverSide/entities/ContestantBenchClientProxy.class dirContestantBench/serverSide/entities
cp  serverSide/sharedRegions/GeneralReposInterface.class  serverSide/sharedRegions/PlayGroundInterface.class  serverSide/sharedRegions/ContestantBenchInterface.class  serverSide/sharedRegions/Contestants_bench.class dirContestantBench/serverSide/sharedRegions
cp  ClientSide/entities/RefreeStates.class  ClientSide/entities/CoachStates.class  ClientSide/entities/ContestantStates.class   ClientSide/entities/RefereeCloning.class  ClientSide/entities/CoachCloning.class  ClientSide/entities/ContestantCloning.class \
   dirContestantBench/ClientSide/entities
cp  ClientSide/stubs/GeneralReposStub.class  ClientSide/stubs/PlayGroundStub.class dirContestantBench/ClientSide/stubs
cp  commInfra/*.class dirContestantBench/commInfra

echo "  Referee Site"
rm -rf dirRefereeSite
mkdir -p dirRefereeSite dirRefereeSite/serverSide dirRefereeSite/serverSide/main dirRefereeSite/serverSide/entities dirRefereeSite/serverSide/sharedRegions \
         dirRefereeSite/ClientSide dirRefereeSite/ClientSide/entities dirRefereeSite/ClientSide/stubs dirRefereeSite/commInfra
cp  serverSide/main/SimulPar.class  serverSide/main/ServerGameOfTheRopeRefereeSite.class dirRefereeSite/serverSide/main
cp  serverSide/entities/RefereeSiteClientProxy.class dirRefereeSite/serverSide/entities
cp  serverSide/sharedRegions/GeneralReposInterface.class  serverSide/sharedRegions/PlayGroundInterface.class  serverSide/sharedRegions/ContestantBenchInterface.class  serverSide/sharedRegions/RefereeSiteInterface.class  serverSide/sharedRegions/Refree_site.class dirRefereeSite/serverSide/sharedRegions
cp  ClientSide/entities/RefreeStates.class  ClientSide/entities/CoachStates.class  ClientSide/entities/ContestantStates.class   ClientSide/entities/RefereeCloning.class  ClientSide/entities/CoachCloning.class  ClientSide/entities/ContestantCloning.class \
   dirRefereeSite/ClientSide/entities
cp  ClientSide/stubs/GeneralReposStub.class  ClientSide/stubs/PlayGroundStub.class  ClientSide/stubs/contestents_bench_stub.class  ClientSide/stubs/RefreeSiteStub.class dirRefereeSite/ClientSide/stubs
cp  commInfra/*.class dirRefereeSite/commInfra


echo "  Referee"
rm -rf dirReferee
mkdir -p dirReferee dirReferee/serverSide dirReferee/serverSide/main dirReferee/ClientSide dirReferee/ClientSide/main dirReferee/ClientSide/entities \
         dirReferee/ClientSide/stubs dirReferee/commInfra
cp  serverSide/main/SimulPar.class dirReferee/serverSide/main
cp  ClientSide/main/ClientGameOfTheRopeReferee.class dirReferee/ClientSide/main
cp  ClientSide/entities/Refree.class  ClientSide/entities/RefreeStates.class dirReferee/ClientSide/entities
cp  ClientSide/stubs/GeneralReposStub.class  ClientSide/stubs/PlayGroundStub.class  ClientSide/stubs/contestents_bench_stub.class  ClientSide/stubs/RefreeSiteStub.class dirReferee/ClientSide/stubs
cp  commInfra/Message.class  commInfra/MessageType.class  commInfra/MessageException.class  commInfra/ClientCom.class dirReferee/commInfra

echo "  Coach"
rm -rf dirCoach
mkdir -p dirCoach dirCoach/serverSide dirCoach/serverSide/main dirCoach/ClientSide dirCoach/ClientSide/main dirCoach/ClientSide/entities \
         dirCoach/ClientSide/stubs dirCoach/commInfra
cp  serverSide/main/SimulPar.class dirCoach/serverSide/main
cp  ClientSide/main/ClientGameOfTheRopeCoach.class dirCoach/ClientSide/main
cp  ClientSide/entities/Coach.class  ClientSide/entities/CoachStates.class dirCoach/ClientSide/entities
cp  ClientSide/stubs/GeneralReposStub.class  ClientSide/stubs/PlayGroundStub.class  ClientSide/stubs/contestents_bench_stub.class  ClientSide/stubs/RefreeSiteStub.class dirCoach/ClientSide/stubs
cp  commInfra/Message.class  commInfra/MessageType.class  commInfra/MessageException.class  commInfra/ClientCom.class dirCoach/commInfra

echo "  Contestant"
rm -rf dirContestant
mkdir -p dirContestant dirContestant/serverSide dirContestant/serverSide/main dirContestant/ClientSide dirContestant/ClientSide/main dirContestant/ClientSide/entities \
         dirContestant/ClientSide/stubs dirContestant/commInfra
cp  serverSide/main/SimulPar.class dirContestant/serverSide/main
cp  ClientSide/main/ClientGameOfTheRopeContestant.class dirContestant/ClientSide/main
cp  ClientSide/entities/Contestant.class  ClientSide/entities/ContestantStates.class dirContestant/ClientSide/entities
cp  ClientSide/stubs/GeneralReposStub.class  ClientSide/stubs/PlayGroundStub.class  ClientSide/stubs/contestents_bench_stub.class  ClientSide/stubs/RefreeSiteStub.class dirContestant/ClientSide/stubs
cp  commInfra/Message.class  commInfra/MessageType.class  commInfra/MessageException.class  commInfra/ClientCom.class dirContestant/commInfra


echo "Compressing execution environments."

echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos

echo "  Play Ground"
rm -f  dirPlayGround.zip
zip -rq dirPlayGround.zip dirPlayGround

echo "  Contestant Bench"
rm -f  dirContestantBench.zip
zip -rq dirContestantBench.zip dirContestantBench

echo "  Referee Site"
rm -f  dirRefereeSite.zip
zip -rq dirRefereeSite.zip dirRefereeSite

echo "  Referee"
rm -f  dirReferee.zip
zip -rq dirReferee.zip dirReferee

echo "  Coach"
rm -f  dirCoach.zip
zip -rq dirCoach.zip dirCoach

echo "  Contestant"
rm -f  dirContestant.zip
zip -rq dirContestant.zip dirContestant