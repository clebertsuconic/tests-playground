. ./set-broker.sh

rm -rf node1
rm -rf node2

$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node1 --clustered --staticCluster tcp://localhost:61617 --max-hops 1 --queues amqp-demo-queue node1
$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name node2 --clustered --staticCluster tcp://localhost:61616 --max-hops 1 --port-offset 1 --queues amqp-demo-queue node2
