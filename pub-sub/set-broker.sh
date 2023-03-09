# set BROKER accordingly to your environment. Notice you don't include the /bin as part of the BROKER
export BROKER=/Users/asouza/redhat/middleware/amq/amq-broker-7.10.2-PATCH-3248
export AGENT=/Users/clebertsuconic/work/apache/check-leak/check-leak/target/check-leak-0.8-SNAPSHOT.jar
export CURRENT_DIR=`pwd`

java -jar $AGENT test.dll

export AGENT_OPTIONS="-agentpath:$CURRENT_DIR/test.dll -javaagent:$AGENT=sleep=60000"
