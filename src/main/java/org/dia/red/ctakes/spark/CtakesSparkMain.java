/**
 * 
 */
package org.dia.red.ctakes.spark;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * @author Selina Chu, Michael Starch, and Giuseppe Totaro
 *
 */
public class CtakesSparkMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			System.err.println("please supply port");
			System.exit(-1);
		}
		int port = Integer.parseInt(args[0]);
		SparkConf c = new SparkConf();
        c.setAppName("ctakes");
        c.setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(c);
        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(500));
        JavaDStream<String> paragraphs = ssc.receiverStream(new ParagraphReceiver(StorageLevel.MEMORY_AND_DISK_2(), port));
        JavaDStream<String> output = paragraphs.map(new CtakesFunction());
        // Below is "throw away"... more for proof of concept
        
        output.foreachRDD(new Function<JavaRDD<String>, Void>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Void call(JavaRDD<String> RDD) throws Exception {
				List<String> paragraphs = RDD.collect();
				for(String par:paragraphs) 
					System.out.println(par);
				return null;
			}});
        ssc.start();
        ssc.awaitTermination();
        ssc.close();

	}

}
