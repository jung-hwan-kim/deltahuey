# Journal into Delta Lake

Running SimpleApp 
```shell
lein uberjar

spark-submit --class "com.gnosyz.deltahuey.SimpleApp" --master local target/deltahuey-0.0.1-standalone.jar

spark-submit --class "deltahuey.main" --master local target/deltahuey-0.0.1-standalone.jar
```