# SparkStreamingCTK

## Introduction
An example of using the
[ClinicalPipeline](https://github.com/giuseppetotaro/ctakes-clinical-pipeline) project
adding the ability to utilize [Spark Streaming](http://spark.apache.org/streaming/).

## Prerequisites
 * Download and install [Apache cTAKES](http://ctakes.apache.org) v3.2.2 as shown below. It is important to install v3.2.2 as this is expected later on.
```
$ cd /usr/local
$ wget "http://archive.apache.org/dist/ctakes/ctakes-3.2.2/apache-ctakes-3.2.2-bin.tar.gz"
$ tar -zxvf apache-ctakes-3.2.2-bin.tar.gz
```
 * Download and install [ClinicalPipeline](https://github.com/giuseppetotaro/ctakes-clinical-pipeline). WARNING - This installation takes a LONG time. Go and make yourself a cup of tea!

```
$ cd /usr/local
$ git clone https://github.com/giuseppetotaro/ctakes-clinical-pipeline.git
$ cd ctakes-clinical-pipeline
$ mvn install 
 ```

## Installation
Update CtakesFunction.java with [UMLS](http://www.nlm.nih.gov/research/umls/) username and password.
The case be obtained by [registering with and signing the UMLS Metathesaurus License](https://uts.nlm.nih.gov//license.html). Once you have it, proceeed as below:
```
$ vim src/main/java/org/dia/red/ctakes/spark/CtakesFunction.java
```
Then populate the following two properties with your username and password respectively:
```
	private void setup() throws UIMAException {
		System.setProperty("ctakes.umlsuser", "");
		System.setProperty("ctakes.umlspw", "");
```
Then build the project.
```
$ mvn install
```

## Running the Spark Streaming Service Locally
```
$ cd target
$ tar -zxzf spark-ctakes-0.1-dist.tar.gz
$ cd spark-ctakes-0.1/bin
$ ./start-ctakes-server.sh 9321
```
This will start the streaming service which you can now interact with. We provide an example below.

## Client Example

This is an example displaying how we can submit portions of clinical text to the streaming service using [netcat](http://netcat.sourceforge.net/). 
First we need to obtain some sample data, this can be located in the file sampledata.tar.gz
```
$ tar -zxvf sampledata.tar.gz
```
We can then use netcat to submit this data over TCP/IP to the local service. The service uses the UMLS credentials provided earlier to process the input data.
```
$ nc 127.0.0.1 9321 < sampledata/all.txt
```

## Executing on an Existing Spark Cluster
When we installed the project as above, you may have also noticied that besides the generated spark-ctakes-0.1-dist.tar.gz and spark-ctakes-0.1-dist.zip artifacts, the build system also generated an spark-ctakes-0.1-job.jar artifact.

This artifact is a self-contained job JAR (uber jar) that contains all the dependencies required to run our application on an existing Cluster.

The [Spark Documentation](https://spark.apache.org/docs/1.1.0/submitting-applications.html) provides excellent context on how to submit your jobs. A bare bones example is provided below:
 * Typical syntax using spark-submit
```
./bin/spark-submit \
  --class <main-class>
  --master <master-url> \
  --deploy-mode <deploy-mode> \
  --conf <key>=<value> \
  ... # other options
  <application-jar> \
  [application-arguments]
```
Now an example for our application
```
$ ./bin/spark-submit \
  --class org.dia.red.ctakes.spark.CtakesSparkMain \
  --master spark://207.184.161.138:7077 \
  --executor-memory 20G \
  --total-executor-cores 100 \
  target/spark-ctakes-0.1-job.jar \
  sampledata/all.txt
```

# License
SparkStreamingCTK is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
A copy of that license is shipped with this source code.
