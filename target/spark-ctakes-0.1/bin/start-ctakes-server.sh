#!/bin/sh
if [[ $# != 1 ]]
then
    echo "Usage:\n\t${0} <port>\n\tStarts up the ctakes streaming server." 1>&2
    exit 1
fi
CTAKES_HOME=/usr/local/apache-ctakes-3.2.2/
CP="${CTAKES_HOME}/desc/:${CTAKES_HOME}/resources/:$( find $(dirname $0)/../lib ${CTAKES_HOME}/lib  -name "*.jar" | tr '\n' ':')"
${JAVA_HOME}/bin/java -cp ${CP} com.celgene.red.ctakes.spark.CtakesSparkMain ${1}
