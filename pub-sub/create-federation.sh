. ./set-broker.sh

rm -rf broker1-federated
rm -rf broker2-federated


$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name broker1-federated --queues test.queue  broker1-federated
export LINE=$(cat broker1-federated/etc/broker.xml  | grep -n '</acceptors>' | awk '{print $1 + 1}' | sed s/://g)


head -$LINE broker1-federated/etc/broker.xml > broker_head.xml
tail +$LINE broker1-federated/etc/broker.xml > broker_tail.xml

cat broker_head.xml send-receive-federated/src/main/resources/federation.xml.b1 broker_tail.xml > broker1-federated/etc/broker.xml


$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 1 --allow-anonymous --no-autotune --verbose --name broker2-federated  --queues test.queue broker2-federated
export LINE=$(cat broker2-federated/etc/broker.xml  | grep -n '</acceptors>' | awk '{print $1 + 1}' | sed s/://g)

head -$LINE broker2-federated/etc/broker.xml > broker_head.xml
tail +$LINE broker2-federated/etc/broker.xml > broker_tail.xml

cat broker_head.xml send-receive-federated/src/main/resources/federation.xml.b2 broker_tail.xml > broker2-federated/etc/broker.xml

rm -rf broker_head.xml
rm -rf broker_tail.xml