# SparkStreamingCTK
A very simple example of using ClinicalPipeline with Spark Streaming


To run this example:


Update CtakesFunction.java with UMLS username and password


Server side:

% mvn install

% cd target

% tar -xzf spark-ctakes-0.1-dist.tar.gz

% cd spark-ctakes-0.1/bin

%./start-ctakes-server.sh 9321


Client side:
(Example of a simple way of using netcat)
% nc 127.0.0.1 9321 < sample.txt
