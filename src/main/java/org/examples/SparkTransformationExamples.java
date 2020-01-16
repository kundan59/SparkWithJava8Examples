package org.examples;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Map;

// This class have Some of the Transformations and Actions can perform on RDD.
public class SparkTransformationExamples {

    final static String SPARK_APPLICATION_NAME = "Transformation Examples";
    final static String SPARK_APPLICATION_RUNNING_MODE = "local";

    public static void main(String[] args) {

        // Turn off spark's default logger
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        // Create Spark Context
        SparkConf sparkConf = new SparkConf().setAppName(SPARK_APPLICATION_NAME)
                .setMaster(SPARK_APPLICATION_RUNNING_MODE);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> words = sparkContext.parallelize(Arrays.asList("one", "two", "two", "three", "one", "five"));
        JavaRDD<Integer> listRDD1 = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4, 1, 3, 5, 6));
        JavaRDD<Integer> listRDD2 = sparkContext.parallelize(Arrays.asList(7, 8, 9, 10, 11, 12, 3));
        JavaPairRDD<Character,Integer> mapToPair1 = sparkContext.parallelizePairs(Arrays.asList(new Tuple2<>('K',1), new Tuple2<>('R',2),
                new Tuple2<>('R',1), new Tuple2<>('K',2), new Tuple2<>('M',1) ));
        JavaPairRDD<Character,Integer> mapToPair2 = sparkContext.parallelizePairs(Arrays.asList(new Tuple2<>('T',1), new Tuple2<>('C',2),
                new Tuple2<>('R',1), new Tuple2<>('K',2), new Tuple2<>('A',1) ));

        //RDD Transformation
        //map(func)
        JavaRDD<Integer> addBy2 = listRDD1.map(listElement -> listElement + 2);
        System.out.println("Add 2 in each element of list" + addBy2.collect());

        //filter()
        JavaRDD<Integer> elementsGreaterThan2 = listRDD1.filter(listElement -> listElement > 2);
        System.out.println("Elements greater than 2 = " + elementsGreaterThan2.collect());

        //union()
        JavaRDD<Integer> unionOfLists = listRDD1.union(listRDD2);
        System.out.println("Union of list1 and list2= " + unionOfLists.collect());

        //intersection()
        JavaRDD<Integer> intersectionOfLists = listRDD1.intersection(listRDD2);
        System.out.println("Intersection of list1 and list2= " + intersectionOfLists.collect());

        //groupByKey()
        JavaPairRDD<Character, Iterable<Integer>> characterIterableJavaPairRDD = mapToPair1.groupByKey();
        System.out.println("Grouping the values with respect to key = " + characterIterableJavaPairRDD.collect());

        //reduceByKey()
        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = words.mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum);
        System.out.println("counts of words in the list using reduceByKey() = " + stringIntegerJavaPairRDD.collect());

        //sortByKey()
        JavaPairRDD<String, Integer> sortByKey = stringIntegerJavaPairRDD.sortByKey();
        System.out.println("counts of words in the list in sorted order according to key = " + sortByKey.collect());

        //join()
        JavaPairRDD<Character, Tuple2<Integer, Integer>> join = mapToPair1.join(mapToPair2);
        System.out.println("Join two RDDs = " + join.collect());

        // RDD Actions
        //countByValue()
        Map<Integer, Long> occurrenceOfElements = listRDD1.countByValue();
        System.out.println("Occurrence of  elements= " + occurrenceOfElements);

        //reduce()
        Integer sumUsingReduce = listRDD1.reduce(Integer::sum);
        System.out.println("Sum  of elements in the list using reduce = " + sumUsingReduce);

        //fold()
        Integer sumUsingFold = listRDD1.fold(0, Integer::sum);
        System.out.println("Sum  of elements in the list using fold() = " + sumUsingFold);
        sparkContext.close();
    }
}
