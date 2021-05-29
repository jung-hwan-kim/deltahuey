package com.gnosyz.deltahuey;

import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

public class SimpleApp {


    public static void main(String[] args) {

        // Listening to delta
        SparkSession spark = SparkSession.builder().appName("SimplApp").getOrCreate();
        spark.log().info("###-###-<START>-###-###");

        Dataset<Row> rows = spark.readStream().format("delta").load("/var/data/delta/ingest-log-3");
        try {
            StreamingQuery query = rows.writeStream().format("console").start();
            query.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
            spark.stop();
        }
        spark.log().info("###-###-<END>-###-###")