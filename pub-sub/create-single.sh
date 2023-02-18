. ./set-broker.sh

rm -rf node1
rm -rf node2

#$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1
$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1 --java-options "-agentpath:/Users/clebertsuconic/work/kevin/pub-sub/node1/bin/test.dll -javaagent:/Users/clebertsuconic/work/apache/check-leak/tick/target/check-leak-tick-0.8-SNAPSHOT.jar"
