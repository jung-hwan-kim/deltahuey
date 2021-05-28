(ns deltahuey.main
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [deltahuey.service :as service])
  (:import (org.apache.spark.sql SparkSession)
           (com.gnosyz.deltahuey SimpleApp SparkApp)))

(defonce runnable-service (http/create-server service/service))

(defn run-spark[]
  (let [spark (.getOrCreate (.appName (SparkSession/builder) "ClojureApp"))
        app (new SimpleApp spark)
        ]
    (.log app "trig/from/clojure")
    (.stop spark)))

(defn -main[& args]
  (reset! service/spark (new SparkApp "ingest-3"))
  (.start @service/spark)
  (.log @service/spark "started")
  (println "*** HTTP START ***")
  (http/start runnable-service)
  )
