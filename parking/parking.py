from pyspark.sql import SparkSession
from pyspark.sql.functions import col, countDistinct, avg, unix_timestamp, round, expr, from_unixtime, date_trunc

# Initialize Spark Session
spark = SparkSession.builder.appName("ParkingLotAnalysis").getOrCreate()

# Read CSV file into DataFrame
df = spark.read.csv("parking_data_sz.csv", header=True, inferSchema=True)

# Convert in_time and out_time to timestamp and calculate parking time in seconds
df = df.withColumn("in_time", unix_timestamp(col("in_time"), "yyyy-MM-dd HH:mm:ss"))
df = df.withColumn("out_time", unix_timestamp(col("out_time"), "yyyy-MM-dd HH:mm:ss"))
df = df.withColumn("parking_time", col("out_time") - col("in_time"))

# Filter out invalid data and cast berthage as integer
df = df.filter(df.out_time > df.in_time).withColumn("berthage", col("berthage").cast("integer"))

# Round down in_time and out_time to the nearest hour
df = df.withColumn("in_hour", date_trunc('hour', from_unixtime("in_time")))
df = df.withColumn("out_hour", date_trunc('hour', from_unixtime("out_time")))

# Requirement 1: Total number of berthages in each section
req1 = df.groupBy("section").agg(countDistinct("berthage").alias("count"))
req1.write.csv('r1', header=True)

# Requirement 2: All unique ids (berthages) with their sections
req2 = df.select("berthage", "section").distinct()
req2.write.csv('r2', header=True)

# Requirement 3: Average parking time for each section
req3 = df.groupBy("section").agg(round(avg("parking_time")).cast("integer").alias("avg_parking_time"))
req3.write.csv('r3', header=True)

# Requirement 4: Average parking time for each berthage, sorted in descending order
req4 = df.groupBy("berthage").agg(round(avg("parking_time")).cast("integer").alias("avg_parking_time"))
req4 = req4.orderBy(req4.avg_parking_time.desc())
req4.write.csv('r4', header=True)
# Generate intervals starting from the nearest rounded down hour
time_df = df.selectExpr("section", "berthage", "sequence(unix_timestamp(in_hour), unix_timestamp(out_hour), 3600) as hour_marks") \
    .selectExpr("section", "berthage", "explode(hour_marks) as hour_mark")

time_df = time_df.groupBy("section", "hour_mark").agg(countDistinct("berthage").alias("count_in_interval"))
time_df = time_df.withColumn("start_time", col("hour_mark")).withColumn("end_time", expr("hour_mark + 3600"))
total_berthage_df = df.groupBy("section").agg(countDistinct("berthage").alias("total_berthage"))
result_df = time_df.join(total_berthage_df, "section")

result_df = result_df.withColumn("percentage", round((col("count_in_interval") / col("total_berthage")) * 100, 1))
result_df = result_df.withColumn("start_time", from_unixtime("start_time", "yyyy-MM-dd HH:mm:ss"))
result_df = result_df.withColumn("end_time", from_unixtime("end_time", "yyyy-MM-dd HH:mm:ss"))
result_df = result_df.withColumn("count_in_interval", col("count_in_interval").cast("integer"))
result_df = result_df.orderBy("section", "start_time")
result_df.select("start_time", "end_time", "section", "count_in_interval", "percentage").coalesce(1).write.csv('r5', header=True)

# input("Press Enter to exit...")

# Stop the Spark Session
spark.stop()
