(ns deltahuey.service
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [clojure.tools.logging :as log]
            [io.pedestal.http.route.definition :refer [defroutes]])
  (:import (org.apache.spark.sql SparkSession)
           (com.gnosyz.deltahuey SparkApp)))

(def spark (atom nil))

(defn status
  [request]
  (let [name (get-in request [:params :name] "World")]
    {:status 200 :body {:status "ok" :spark @spark}}))

(defn start-spark [request]
  (when (nil? @spark)
    (println "### ---> starting")
    (reset! spark (new SparkApp "ingest-3"))
    (.start spark))
  {:status 200 :body {:status "spark-running"}})

(defn stop-spark [request]
  (if (nil? @spark)
    (log/info "nothing to do.. it's already stopped")
    (try
      (println "stopping..")
      (.stop @spark)
      (reset! spark nil)))
    {:status 200 :body {:cmd "stopped"}}
  )

(defn ingest [request]
  (let [message (get-in request [:params :message] "ingest-33")]
    (log/info "ingesting:" message)
    (.log @spark message)
    {:status 200 :body "ingested"}))


(defroutes routes
           [[["/"
              ["/status" {:get status}]
              ["/start" {:get start-spark}]
              ["/stop" {:get stop-spark}]
              ["/ingest" {:get ingest}]
              ]]])

(def service {:env                 :prod
              ::http/routes        routes
              ::http/resource-path "/public"
              ::http/type          :jetty
              ::http/port          8888})
