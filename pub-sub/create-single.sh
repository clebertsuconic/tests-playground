. ./set-broker.sh

rm -rf node1
rm -rf node2

java -jar $AGENT test.dll


#$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1
#$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1 --java-options "-agentpath:/Users/clebertsuconic/work/kevin/pub-sub/test.dll -javaagent:/Users/clebertsuconic/work/apache/check-leak/core/target/core-0.8-SNAPSHOT.jar=output=sleep=1000"

$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1 --java-options "$AGENT_OPTIONS"
