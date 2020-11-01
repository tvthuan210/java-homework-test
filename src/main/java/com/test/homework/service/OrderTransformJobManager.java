package com.test.homework.service;

import com.test.homework.model.Order;
import com.test.homework.model.OrderDetailDTO;
import com.test.homework.processor.OrderDetailsResourcePartitioner;
import com.test.homework.processor.OrderDetailProcessor;
import com.test.homework.repository.OrderRepository;
import com.test.homework.utils.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class OrderTransformJobManager extends JobManager {

  private OrderRepository orderRepository;

  @Autowired
  public void setOrderRepository(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Bean
  public Job orderTransformJob(@Qualifier("testExecutor") TaskExecutor taskExecutor) throws Exception {
    return jobBuilderFactory
        .get(Constants.OrderTransformJob.JOB_NAME)
        .incrementer(new RunIdIncrementer())
        .start(orderTransformMasterStep(taskExecutor))
        .build();
  }

  @Bean
  public Step orderTransformMasterStep(TaskExecutor taskExecutor) throws Exception {
    return stepBuilderFactory.get(Constants.OrderTransformJob.MASTER_STEP_NAME)
        .partitioner(Constants.OrderTransformJob.MASTER_STEP_NAME, orderPartitioner())
        .step(orderTransformSlaveStep())
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  public Step orderTransformSlaveStep() {
    return stepBuilderFactory.get(Constants.OrderTransformJob.SLAVE_STEP_NAME)
        .<Order, OrderDetailDTO>chunk(10)
        .reader(ordersReaderTransform(null))
        .processor(new OrderDetailProcessor())
        .writer(orderWriter(null))
        .build();
  }

  @Bean
  @StepScope
  public OrderDetailsResourcePartitioner orderPartitioner() {
    OrderDetailsResourcePartitioner partitioner = new OrderDetailsResourcePartitioner();
    partitioner.setValues(orderRepository.findVendors());
    return partitioner;
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Order> ordersReaderTransform(@Value("#{stepExecutionContext['vendor']}") String vendor) {
    JpaPagingItemReader<Order> reader = new JpaPagingItemReader<>();
    reader.setQueryString("SELECT o from Order o where o.vendor like :vendor");
    reader.setParameterValues(Collections.singletonMap("vendor", "%" + vendor + "%"));
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(100);
    reader.setSaveState(true);
    return reader;
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<OrderDetailDTO> orderWriter(
      @Value("#{stepExecutionContext['outputFile']}") String outputFile) {
    FlatFileItemWriter<OrderDetailDTO> writer = new FlatFileItemWriter<>();
    writer.setResource(new FileSystemResource(outputFile));
    writer.setAppendAllowed(true);
    writer.setHeaderCallback(writer1 -> {
      List<String> headers = Arrays.asList("order_id", "item", "quantity");
      writer1.write(String.join(",", headers));
    });
    writer.setLineAggregator(new DelimitedLineAggregator<OrderDetailDTO>() {
      {
        setDelimiter(",");
        setFieldExtractor(new BeanWrapperFieldExtractor<OrderDetailDTO>() {
          {
            setNames(new String[] {"orderId", "item", "quantity"});
          }
        });
      }
    });
    return writer;
  }
}
