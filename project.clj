(defproject deltahuey "0.0.1"
  :description "Explore Delta Lake with Spark 3 using Java & Clojure"
  :url "http://deltahuey.gnosyz.com"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [io.delta/delta-core_2.12 "0.8.0"]
                 [io.pedestal/pedestal.service "0.5.9"]
                 [io.pedestal/pedestal.jetty "0.5.9"]
                 [org.clojure/tools.logging "1.1.0"]
                 ]
  :profiles {:provided
             {:dependencies
              [[org.apache.spark/spark-core_2.12 "3.1.1"]
               [org.apache.spark/spark-sql_2.12 "3.1.1"]
               [org.apache.spark/spark-streaming_2.12 "3.1.1"]
               ]}}
  :java-source-paths ["main/java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :resource-paths ["resources"]
  :repl-options {:init-ns core}
  :main deltahuey.main
  :aot :all)