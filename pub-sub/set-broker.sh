# set BROKER accordingly to your environment. Notice you don't include the /bin as part of the BROKER
export BROKER=/Users/clebertsuconic/work/apache/activemq-artemis/artemis-distribution/target/apache-artemis-2.29.0-SNAPSHOT-bin/apache-artemis-2.29.0-SNAPSHOT
export AGENT=/Users/clebertsuconic/work/apache/check-leak/check-leak/target/check-leak-0.8-SNAPSHOT.jar
export CURRENT_DIR=`pwd`

export AGENT_OPTIONS="-agentpath:$CURRENT_DIR/test.dll -javaagent:$AGENT=sleep=60000"
