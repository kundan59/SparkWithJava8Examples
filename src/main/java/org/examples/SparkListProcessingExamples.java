package org.examples;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class SparkListProcessingExamples {

    final static String SPARK_APPLICATION_NAME = "Spark List processing Examples";
    final static String SPARK_APPLICATION_RUNNING_MODE = "local";

    public static void main(String[] args) {

        // Turn off spark's default logger
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        // Create Spark Context
        SparkConf sparkConf = new SparkConf().setAppName(SPARK_APPLICATION_NAME)
                .setMaster(SPARK_APPLICATION_RUNNING_MODE);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(12);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(40);
        arrayList.add(5);
        arrayList.add(6);
        arrayList.add(7);
        arrayList.add(8);

        JavaRDD<Integer> arrayListRDD = sparkContext.parallelize(arrayList);

        // Sum of elements of List
        Integer sum = arrayListRDD.reduce(Integer::sum);
        System.out.println("sum of list elements = " + sum);

        //find number of even and odd elements in list
        long countOfEvenNumbers = arrayListRDD.filter(listElements -> (listElements % 2 == 0)).count();
        System.out.println("Number of even elements in the list = " + countOfEvenNumbers);
        System.out.println("Number of odd elements in the list = " + (arrayListRDD.count() - countOfEvenNumbers ));

        //find Max value from the list
        Integer maxValue = arrayListRDD.reduce(Math::max);
        System.out.println("Maximum value in the list = " + maxValue);

        //find Min value from the list
        Integer minValue = arrayListRDD.reduce(Math::min);
        System.out.println("Minimum value in the list = " + minValue);

        //Sort the list in ascending order
        JavaRDD<Integer> sortedListAscending = arrayListRDD.sortBy(listElement ->
                listElement, true, 1);
        System.out.println("List in ascending order = " + sortedListAscending.collect());

        //Sort the list in descending order
        JavaRDD<Integer> sortedListDescending = arrayListRDD.sortBy(listElement ->
                listElement, false, 1);
        System.out.println("list in descending order" +sortedListDescending.collect());

        //find max 3 elements from list
        System.out.println("Maximum 3 elements of list = " + sortedListDescending.top(3));

        //find min 3 elements from list
        System.out.println("Minimum three elements from the list = " + sortedListAscending.top(3));
        sparkContext.close();
    }
}
