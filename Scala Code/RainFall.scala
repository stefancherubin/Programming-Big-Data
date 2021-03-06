import org.apache.spark.sql.{SQLContext, SparkSession}
object RainFall {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .master("local[*]")
      .appName("TotalRainFallUSA")
      .getOrCreate()

    val year = 2014


    for (year <- 2014 to 2018) {
      val df = spark.read.option("header", "true").csv("D:/data/us_weather_" + year + "_clean/us_weather_" + year +
        "_clean.csv")
      df.show()

      df.createOrReplaceTempView("CC")
      val sqlDF = spark.sql("select YEARMODA, sum(PRCP)" +
        " from CC where PRCP > 0.0 group by YEARMODA order by YEARMODA")
      sqlDF.show()

      sqlDF.coalesce(1).write.format("com.databricks.spark.csv").option(
        "header", "true").save("D:/data/us_weather_rain_" + year)
    }
  }
}
