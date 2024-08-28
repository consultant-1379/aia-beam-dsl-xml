# Introduction

pbaName is Apache beam DSL XML based flow generated from the AIA Portal(http://analytics.ericsson.se/#/) using beam-dsl-xml template.

# Pre- Prerequites

Apache beam is required for this flow. You can use the dependencies of version 2.3/2.4 from nexus. Please
modify the beam.version property in pom.xml if you want to use a different version.

Apache beam DSL XML is an extension contributed by AIA architect Sajeevan Achuthan, and it's not available in official beam release yet.
Snapshot version is used for early evaluation.

# Prerequites

Some of the services are required depending on the chosen Input/Output and Runner. For example,

Kafka service is required if the I/O is kafka.
Spark cluster is required if the runner is SparkRunner.

You can use the AIA sandbox or install these required services in your machine.

# Development

Please follow the http://analytics.ericsson.se/#/Documentation/Environment_Setup to set up your environment. The following tools are required,
```
   Java
   Maven
   Docker
```

# Terminology

We are using the following terminologies when building or running pbaName flow. And the table below contains the mapping of these terminologies.

* maven runner profile: This is the maven profile as defined in the pom.xml.
* Beam Runner: Apache beam Runner(https://beam.apache.org/documentation/runners/capability-matrix/).
* Uber Jar: One single JAR file containing the packages and required dependencies.
* docker image: dockerization is provided as one way of packaging, and maven plugin is used to generated docker image.

|maven runner profile|Beam Runner|Uber JAR|docker image|
|:------------- |:-------------|:-------------|:-------------|
|spark-runner |SparkRunner|target/pbaName-bundled.jar|armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-spark|
|flink-runner|FlinkRunner|target/pbaName-bundled.jar|armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-flink|
|direct-runner|DirectRuner|target/pbaName-bundled.jar|armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-direct|
|apex-runner|ApexRunner|target/pbaName-bundled.jar|armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-apex|
|gearpump-runner|GearpumpRunner|target/pbaName-bundled.jar|armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-gearpump|
|dataflow-runner|CloudFlowRunner|target/pbaName-bundled.jar|armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-dataflow|

`NOTE`

- NOT all the docker images are tested, at the moment, direct runner, spark runner and flink runne are under testing. Please contact AIA team if you need support for other runners.

# Build

Maven is the chosen package tool for this flow. Please run the following command to compile and package in uber jar together with docker image.
```
 mvn compile package -P<maven runner profile>

 <maven runner profile>: as defined in Terminology section.
```

e.g,

```
 mvn compile package -Pspark-runner
```

# Run

pbaName can be executed in local mode, cluster mode or docker mode with the generated uber jar file or docker image.

## local mode

pbaName runs in local mode, i.e, single JVM mode. Apache beam launches embedded server for the selected runner.
```
java -cp <uber JAR> org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner --dslXml=`pwd`/flow.xml --runner=<Beam Runner>

<uber JAR>: as defined in Terminology section.
<Beam Runner>: as defined in Terminology section.
```

e.g,

```
java -cp target/pbaName-bundled.jar org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner --dslXml=`pwd`/flow.xml --runner=SparkRunner
```


## cluster mode
pbaName can be submitted to cluster for execution. Flink and Spark are provided here for reference, please check each runner deployment documentation for details and how to deploy.

### Spark cluster
```
<SPARK_HOME>/bin/spark-submit --master=<SPARK_MASTER> --class org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner <UBER JAR> --runner=SparkRunner --dslXml=<FLOW_FILE>

<SPARK_HOME>: spark home directory.
<SPARK_MASTER>: spark master URL.
<UBER JAR>: as defined in the Terminology.
<FLOW_FILE>: flow file location.
```

e.g.,

```
/opt/spark-2.2.1/bin/spark-submit --master=spark://localhost:7077 --class org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner target/access-log-pre-processing-dsl-xml-bundled.jar --runner=SparkRunner --dslXml=`pwd`/flow.xml
```

### Flink cluster
```
<FLINK_HOME>/bin/flink run -m <FLINK_MASTER> -c org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner <UBER JAR> --dslXml=<FLOW_FILE> --runner=FlinkRunner

<FLINK_HOME>: flink home directory.
<FLINK_MASTER>: flink master URL.
<UBER JAR>: as defined in the Terminology.
<FLOW_FILE>: flow file location.
```

e.g.,

```
/opt/flink/bin/flink run -c org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner `pwd`/target/flink-access-log-bundled.jar --runner=FlinkRunner --dslXml=`pwd`/flow.xml
```

## Docker mode
With the built docker image, pbaName can run in docker mode, and it can be deployed into docker orchestration platform.
```
docker run -v <FLOW FILE FOLDER>:/flow <docker image>
<FLOW FILE FOLDER>: contains the Beam DSL XML file.
<docker image>: as defined in the Terminology.
```

You can change the environment variables for each runner, please check the Dockerfile for supported variables.

e.g,
```
docker run -d -v `pwd`:/flow armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-spark
```

## Troubleshooting
1. When running in docker mode, there is no output.

In some case this is caused by the service unavailable to the flow inside docker, you can try the following solutions,

* Run the docker with HOST network, e.g,
 ```
 docker run -d --network=host -v `pwd`:/flow armdocker.rnd.ericsson.se/aia/bps/beam/flow/pbaName-spark
 ```
* Change the input/output configuration to public address of the services.
* Use the service link or service discovery from container services, like docker-compose, kubernetes.
