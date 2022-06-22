FROM confluentinc/cp-kafka-connect:3.2.0
#3.2.0
#6.2.4

WORKDIR /coin-api-connector
COPY config config
COPY target target

VOLUME /coin-api-connector/config
VOLUME /coin-api-connector/offsets

CMD export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=config/log4j.properties" && CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')" connect-standalone config/worker.properties config/custom-connector.properties
