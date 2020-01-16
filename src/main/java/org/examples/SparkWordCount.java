package org.examples;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

// Illustrates a word count in Java.
public class SparkWordCount {
    final static String FILE_INPUT = "sparkdata/sparkwordcountinputdata";
    final static String FILE_OUTPUT = "sparkdata/sparkwordcountoutputdata";
    final static String SPARK_APPLICATION_NAME = "Word Count";
    final static String SPARK_APPLICATION_RUNNING_MODE = "local";

    public static void main(String[] args) {

        // Turn off spark's default logger
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        // Create Spark Context
        SparkConf sparkConf = new SparkConf().setAppName(SPARK_APPLICATION_NAME)
                .setMaster(SPARK_APPLICATION_RUNNING_MODE);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = sparkContext.textFile(FILE_INPUT);
        JavaPairRDD<String, Integer> wordCounts = lines.flatMap(line ->
                Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum);
        System.out.println(wordCounts.collect());
        sparkContext.close();
    }
}
