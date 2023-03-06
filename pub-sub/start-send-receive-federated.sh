. ./set-broker.sh


cd send-receive-federated
mvn clean package
cd target

java -jar send-receive-federated-7.0.0-SNAPSHOT.jar
# java -agentpath:$CURRENT_DIR/test.dll -javaagent:$AGENT="sleep=10000;output=../../send-receive.log"  -jar send-receive-federated-7.0.0-SNAPSHOT.jar


