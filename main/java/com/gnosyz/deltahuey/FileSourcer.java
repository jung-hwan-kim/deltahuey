package com.gnosyz.deltahuey;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;

public class FileSourcer {
    public static void main(String[] args) {

        // Listening to file
        SparkSession spark = SparkSession.builder().appName("File Sourcer").getOrCreate();
        spark.log().info("###-###-<START>-###-###");

        Dataset<Row> rows = spark.readStream().format("file")
                .option("cleanSource", "archive")
                .option("sourceArchiveDir", "/var/data/file-archive")
                .load("/var/data/file-source");
        try {
            StreamingQuery query = rows.writeStream().format("console").start();
            query.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
            spark.stop();
        }
        spark.log().info("###-###-<END>-###-###");
    }
}
