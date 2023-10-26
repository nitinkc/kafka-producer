

```shell
cd /Applications/Development/kafka_2.13-3.6.0
bin/zookeeper-server-start.sh config/zookeeper.properties
```

In another tab

```shell
cd /Applications/Development/kafka_2.13-3.6.0
bin/kafka-server-start.sh config/server.properties
```

## Conduktor

Download and install and run once the kafka server is running on local and 