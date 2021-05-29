package com.gnosyz.deltahuey;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.*;

public class FileSourcer {
    private static StructType treeSchema = new StructType(new StructField[] {
            new StructField("id", DataTypes.StringType, false, Metadata.empty()),
            new StructField("girth", DataTypes.DoubleType, true, Metadata.empty()),
            new StructField("height", DataTypes.DoubleType, true, Metadata.empty()),
            new StructField("volume", DataTypes.DoubleType, true, Metadata.empty())
    });

    public static void main(String[] args) {

        // Listening to file
        SparkSession spark = SparkSession.builder().appName("File Sourcer").getOrCreate();
        spark.log().info("###-###-<START>-###-###");


        Dataset<Row> rows = spark.readStream()
                .option("cleanSource", "archive")
                .option("sourceArchiveDir", "/var/data/archive")
                .schema(treeSchema)
                .format("csv")
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
