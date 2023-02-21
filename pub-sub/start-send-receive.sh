. ./set-broker.sh


cd send-receive
mvn clean install -Predhat-ga
cd target
java -agentpath:$CURRENT_DIR/test.dll -javaagent:$AGENT="sleep=10000;output=../../send-receive.log"  -jar reproducer-7.0.0-SNAPSHOT.jar


