. ./set-broker.sh

rm -rf broker1-federated
rm -rf broker2-federated


$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name broker1-federated --queues test.queue  broker1-federated
$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 1 --allow-anonymous --no-autotune --verbose --name broker2-federated  --queues test.queue broker2-federated

cp ./federation/broker1.xml ./broker1-federated/etc
cp ./federation/broker2.xml ./broker2-federated/etc
