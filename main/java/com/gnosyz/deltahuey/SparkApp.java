package com.gnosyz.deltahuey;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

public class SparkApp {
    private SparkSession session;
    private String name;
    public SparkApp(String name) {
        this.name = name;
    }
    public void start() {
        session = SparkSession.builder().appName(name).getOrCreate();
    }
    public void log(String msg) {
        Log l = new Log(UUID.randomUUID().toString(),
                name,
                msg,
                Timestamp.from(Instant.now()));
        Dataset<Log> ds = session.createDataset(Collections.singletonList(l),
                Encoders.bean(Log.class));
        ds.write().format("delta").mode("append").save("/var/data/delta/ingest-log-3");
    }
    public void stop() {
        session.stop();
    }
    public String toString() {
        return session.toString();
    }
    public SparkSession getSession() {
        return session;
    }
}
