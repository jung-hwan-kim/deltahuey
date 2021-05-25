(ns deltahuey.main
  (:gen-class)
  (:import (org.apache.spark.sql SparkSession)))

(defn this-a[]
  (println "this-a"))


(defn -main[& args]
  (let [spark (.getOrCreate (.appName (SparkSession/builder) "ClojureApp"))
        log (.log spark)
        logFile "README.md"
        logData (.textFile (.read spark) logFile)
        ]

    (.info log "### START ###")
    (.show logData)
    )
  (println "*** STOP ***"))
