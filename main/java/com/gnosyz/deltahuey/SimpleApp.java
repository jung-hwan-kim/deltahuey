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

    private SparkSession session;
    public SimpleApp(SparkSession session) {
        this.session = session;
    }
    public void log(String msg) {
        Log l = new Log(UUID.randomUUID().toString(),
                "ingest",
                msg,
                Timestamp.from(Instant.now()));
        Dataset<Log> ds = session.createDataset(Collections.singletonList(l),
                Encoders.bean(Log.class));
        ds.write().format("delta").mode("append").save("/var/data/delta/ingest-log-2");
    }

    public static void main(String[] args) {

        String logFile = "README.md"; // Should be some file on your system

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
        spark.log().info("###-###-<END>-###-###");
    }
}
