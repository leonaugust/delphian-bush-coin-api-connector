## Prerequisites

* Maven.
* Java 8.
* Docker and docker-compose installed.
* `https://www.coinapi.io/` get free api key.

---

Add your properties in the following directory /config/custom-connector.properties
Change `coin.api.key`.

    name=CoinApiSourceConnectorDemo
    tasks.max=1
    connector.class=com.delphian.com.delphian.bush.CoinApiSourceConnector
    topic=exchange-rates
    application=delphian-bush-coin-api-source-connector
    coin.api.key=YOUR_API_KEY
    profile.active=test
    poll.timeout=60

Configurable parameters:

* `profile.active` - Default: **test**. Available values: [test/prod].  
  **test** - will poll rates from test file, not calling API.   
  **prod** - will call real coin-api, requires `coin.api.key`

* `application` - Name of the application  
  Will be included in key schema

* `topic` - Name of the topic to which kafka pushes the data

* `poll.timeout` - Default: 60. Should be bigger than 20. Time in seconds between the poll.

* `name` - The last offset will be associated with the name given. **Side note**: for testing
  purposes, change name after each start.
  Otherwise, the connector will keep the latest offset to track the place where it stopped reading.

Additional properties:

* `debug.additional.info` - Optional(default - false). Available values: [true/false].
  Enables logging of the additional information.

-----
**Testing in standalone mode**

Launch Kafka with docker-compose(starts on port *29092*)

    cd kafka
    docker-compose up -d
    cd ..

-----
Start in standalone mode

    mvn clean package -DskipTests
    ./run.sh

-----
Read data

    docker exec --interactive --tty kafka \
    kafka-console-consumer --bootstrap-server kafka:29092 \
    --topic exchange-rates \
    --from-beginning

-----

Stop connector and clean up data written to Kafka

    cd kafka
    docker container stop rates-connector
    docker-compose down --volumes

-----
**Setup in cluster**

    Produces fat jar coin-api-connector-VERSION-jar-with-dependencies.jar

    mvn clean package -DskipTests

<a href="https://github.com/leonaugust/delphian-bush-hoover/blob/master/README.md" target="_blank">
Usage example</a>