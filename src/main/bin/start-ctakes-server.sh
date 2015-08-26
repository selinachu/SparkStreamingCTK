#!/bin/sh
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

if [[ $# != 1 ]]
then
    echo "Usage:\n\t${0} <port>\n\tStarts up the ctakes streaming server." 1>&2
    exit 1
fi
CTAKES_HOME=/usr/local/apache-ctakes-3.2.2/
CP="${CTAKES_HOME}/desc/:${CTAKES_HOME}/resources/:$( find $(dirname $0)/../lib ${CTAKES_HOME}/lib  -name "*.jar" | tr '\n' ':')"
${JAVA_HOME}/bin/java -cp ${CP} org.dia.red.ctakes.spark.CtakesSparkMain ${1}
