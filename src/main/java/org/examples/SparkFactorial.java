package org.examples;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.math.BigInt;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Illustrate the factorial of a number.
public class SparkFactorial {

    final static String SPARK_APPLICATION_NAME = "Factorial";
    final static String SPARK_APPLICATION_RUNNING_MODE = "local";

    public static void main(String[] args) {

        // Turn off spark's default logger
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        // Create Spark Context
        SparkConf sparkConf = new SparkConf().setAppName(SPARK_APPLICATION_NAME)
                .setMaster(SPARK_APPLICATION_RUNNING_MODE);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        System.out.println("Enter number to find its factorial");
        Scanner sc = new Scanner(System.in);
        Integer number = sc.nextInt();

        BigInteger factorialOfNumber = factorial(number, sparkContext);
        System.out.println("factorial of a given Number = " +factorialOfNumber);
        sparkContext.close();
    }

    private static BigInteger factorial(Integer num, JavaSparkContext sparkContext) {

        List<BigInteger> listOfElementForNum = Stream.iterate(BigInteger.ONE, i ->
                i.add(BigInteger.ONE)).limit(num).collect(Collectors.toList());
        JavaRDD<BigInteger> listOfElementForNumRDD = sparkContext.parallelize(listOfElementForNum);
       return listOfElementForNumRDD.reduce(BigInteger::multiply);
    }
}
