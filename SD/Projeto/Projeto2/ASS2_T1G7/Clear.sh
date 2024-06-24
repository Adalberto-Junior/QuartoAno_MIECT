echo "Cleaning generated folders localy"
echo "  Removing .zip files"
rm -rf *.zip

echo "  Removing generated folders"
rm -rf dirGeneralRepos.zip
rm -rf dirPlayGround.zip
rm -rf dirContestantBench.zip
rm -rf dirRefereeSite.zip
rm -rf dirReferee.zip
rm -rf dirCoach.zip
rm -rf dirContestant.zip

rm -rf dirGeneralRepos
rm -rf dirPlayGround
rm -rf dirContestantBench
rm -rf dirRefereeSite
rm -rf dirReferee
rm -rf dirCoach
rm -rf dirContestant


echo "  Removing generated .class files"
rm ./*/*.class
rm ./*/*/*.class