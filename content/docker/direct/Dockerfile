# **********************************************************************
# Ericsson Radio Systems AB
# **********************************************************************
#
# (c) Ericsson 2017 - All rights reserved.
#
# The copyright to the computer program(s) herein is the property of
# Ericsson Radio Systems AB, Sweden. The programs may be used and/or
# copied only with the written permission from Ericsson Radio Systems AB
# or in accordance with the terms and conditions stipulated in the
# agreement/contract under which the program(s) have been supplied.
#
# **********************************************************************
#Use the rheljava as base image
FROM armdocker.rnd.ericsson.se/aia/base/java:latest

ENV FLOW_HOME="/pbaName"
ENV FLOW_JAR="pbaName-bundled.jar"
ENV FLOW_MAIN_CLASS="org.apache.beam.sdk.extensions.dsls.xml.flow.loader.BeamFlowDSLRunner"
ENV FLOW_FILE_FOLDER="/flow"
ENV FLOW_FILE_NAME="flow.xml"

#create the flow folder for the jars, dependencies, and configuration
CMD mkdir -p ${FLOW_HOME}

#Copy uber jar
ADD ${FLOW_JAR} ${FLOW_HOME}/

#Run this script when docker images gets initialized
ENTRYPOINT ["sh", "-c", "java -cp ${FLOW_HOME}/${FLOW_JAR} ${FLOW_MAIN_CLASS} --dslXml=${FLOW_FILE_FOLDER}/${FLOW_FILE_NAME}"]
