package ${package};

import java.io.Serializable;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;


public class CalculatePi implements Serializable {
  private static final int NUM_SAMPLES = 1_000_000;

  public static void main(String[] args) {

    SparkSession spark = SparkSession
        .builder()
        .appName("Spark Pi")
        .master("local[*]")
        .getOrCreate();

    long start = System.currentTimeMillis();

    List<Integer> l = IntStream.range(0, NUM_SAMPLES)
        .mapToObj(x -> x).collect(Collectors.toList());
    var total = spark
        .createDataset(l, Encoders.INT()).filter((FilterFunction<Integer>) i -> {
          double x = Math.random() * 2 - 1;
          double y = Math.random() * 2 - 1;
          return x * x + y * y <= 1;
        }).count();


    long end = System.currentTimeMillis();
    System.out.println("Pi = " + 4.0 * total / NUM_SAMPLES);
    System.out.println("Time = " + (end -start) + " ms");

    spark.stop();
  }
}
