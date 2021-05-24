package com.gnosyz.deltahuey;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.launcher.SparkLauncher;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class SimpleApp {
    public static void main(String[] args) {



        String logFile = "README.md"; // Should be some file on your system

        SparkSession spark = SparkSession.builder().appName("SimplApp").getOrCreate();
        spark.log().info("###-###-<START>-###-###");

        Dataset<String> logData = spark.read().textFile(logFile);

        logData.show();

        spark.log().info("###-###-<END>-###-###");
        spark.stop();
    }
}
