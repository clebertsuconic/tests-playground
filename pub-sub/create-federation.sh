. ./set-broker.sh

rm -rf broker1-federated
rm -rf broker2-federated


$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 0 --allow-anonymous --no-autotune --verbose --name broker1-federated --queues test.queue  broker1-federated
export LINE_IN=$(cat broker1-federated/etc/broker.xml  | grep -n '</acceptors>' | awk '{print $1 + 1}' | sed s/://g)


head -$LINE_IN broker1-federated/etc/broker.xml > broker_head.xml
tail +$LINE_IN broker1-federated/etc/broker.xml > broker_tail.xml

cat broker_head.xml send-receive-federated/src/main/resources/federation.xml.in broker_tail.xml > broker1-federated/etc/broker.xml


$BROKER/bin/artemis create --silent --force --user artemis --password artemis --role amq --port-offset 1 --allow-anonymous --no-autotune --verbose --name broker2-federated  --queues test.queue broker2-federated
export LINE_IN=$(cat broker2-federated/etc/broker.xml  | grep -n '</acceptors>' | awk '{print $1 + 1}' | sed s/://g)

head -$LINE_IN broker2-federated/etc/broker.xml > broker_head.xml
tail +$LINE_IN broker2-federated/etc/broker.xml > broker_tail.xml

cat broker_head.xml send-receive-federated/src/main/resources/federation.xml.out broker_tail.xml > broker2-federated/etc/broker.xml

rm -rf broker_head.xml
rm -rf broker_tail.xml