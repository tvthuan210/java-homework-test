package com.test.homework.processor;

import com.test.homework.utils.Constants;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsResourcePartitioner implements Partitioner {

  private List<String> values;

  @Value("${file.poller.output.directory}")
  private String outputDirectory;

  @Value("${file.poller.output.file.extension}")
  private String fileExtension;

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    Map<String, ExecutionContext> map = new HashMap<>(gridSize);
    int i = 0;
    for (String value: values) {
      ExecutionContext context = new ExecutionContext();
      context.putString(Constants.OrderTransformJob.VENDOR, value);
      context.putString(Constants.OrderTransformJob.OUTPUT_FILE, outputDirectory + value + fileExtension);
      map.put("partition" + i, context);
      i++;
    }
    return map;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }
}