(ns deltahuey.service
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [clojure.tools.logging :as log]
            [cheshire.core :as json]
            [io.pedestal.http.route.definition :refer [defroutes]])
  (:import (org.apache.spark.sql SparkSession)
           (com.gnosyz.deltahuey SparkApp)))

(def spark (atom nil))

(defn return-json[body-map]
  {:status 200
    :headers {"Content-Type" "application/json"}
    :body (json/generate-string body-map)
    })

(defn status
  [request]
  (let [name (get-in request [:params :name] "World")]
    (return-json {:spark spark})))

(defn start-spark [request]
  (if (nil? @spark)
    (do
      (println "### ---> starting")
      (reset! spark (new SparkApp "ingest-3"))
      (.start spark)
      (return-json {:status "Spark started."}))
    (do
      (println "### already running")
      (return-json {:status "Spark already running."}))))

(defn stop-spark [request]
  (if (nil? @spark)
    (do
      (log/info "nothing to do.. it's already stopped")
      (return-json {:result "Nothing to stop."}))
    (try
      (println "stopping..")
      (.stop @spark)
      (reset! spark nil)
      (return-json {:result "Stopped"}))))

(defn ingest [request]
  (let [body (json/parse-stream (clojure.java.io/reader (:body request)) true)
        path (:path body)
        message (:message body)
        ]
    (log/info "ingesting:" path "," message)
    (.log @spark message)
    (return-json {:path path :message message :status "Success"})))

(defroutes routes
           [[["/"
              ["/status" {:get status}]
              ["/start" {:get start-spark}]
              ["/stop" {:get stop-spark}]
              ["/ingest" {:post ingest}]
              ]]])

(def service {:env                 :prod
              ::http/routes        routes
              ::http/resource-path "/public"
              ::http/type          :jetty
              ::http/port          8888})
