package org.examples;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// Illustrate how to find count of distinct names in the file per first letter.
public class DistinctNamesCount {
    final static String FILE_INPUT = "sparkdata/Sparkdistinctnameinputdata";
    final static String SPARK_APPLICATION_NAME = "Distinct Names Count";
    final static String SPARK_APPLICATION_RUNNING_MODE = "local";

    public static void main(String[] args) {

        // Turn off spark's default logger
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        // Create Spark Context
        SparkConf sparkConf = new SparkConf().setAppName(SPARK_APPLICATION_NAME)
                .setMaster(SPARK_APPLICATION_RUNNING_MODE);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> names = sparkContext.textFile(FILE_INPUT);
        JavaPairRDD<Character, Object> distinctNamesCount = names.mapToPair(name ->
                new Tuple2<>(name.charAt(NumberUtils.INTEGER_ZERO), name))
                .groupByKey().mapValues(distinctNames -> {
                            List<String> name = new ArrayList<>();
                            distinctNames.iterator().forEachRemaining(name::add);
                            HashSet<String> distinctName = new HashSet<>(name);
                            return distinctName.size();
                        });

        System.out.println(distinctNamesCount.collect());
        sparkContext.close();
    }
}
