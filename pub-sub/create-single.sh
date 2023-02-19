. ./set-broker.sh

rm -rf node1
rm -rf node2

#$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1
$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --max-hops 1 --queues amqp-demo-queue node1 --java-options "-agentpath:/home/clebert/work/tests-playground/pub-sub/test.dll  -javaagent:/home/clebert/work/check-leak/core/target/core-0.8-SNAPSHOT.jar=down=70;sleep=10000"
