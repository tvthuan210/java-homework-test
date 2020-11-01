package com.test.homework.service;

import com.test.homework.file.FileParsingCompletionListener;
import com.test.homework.model.Order;
import com.test.homework.model.OrderDTO;
import com.test.homework.processor.OrderProcessor;
import com.test.homework.utils.Constants;
import com.test.homework.utils.Utils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.task.TaskExecutor;

import java.io.IOException;
import java.net.MalformedURLException;

@Configuration
@Qualifier("FileHandlingJobManager")
public class FileHandlingJobManager extends JobManager {

  @Value("${file.poller.source.directory}")
  private String sourceDirectory;
  @Value("${file.poller.target.directory}")
  private String processedDirectory;

  @Bean
  public Job readCSVFilesJob(@Qualifier("testExecutor") TaskExecutor taskExecutor,
      FileParsingCompletionListener listener) throws IOException {
    return jobBuilderFactory
        .get(Constants.CsvJob.JOB_NAME)
        .incrementer(new RunIdIncrementer())
        .start(readCSVFileMasterStep(taskExecutor))
        .next(moveFileStep())
        .listener(listener)
        .build();
  }

  @Bean
  public Step readCSVFileMasterStep(TaskExecutor taskExecutor) throws IOException {
    return stepBuilderFactory.get(Constants.CsvJob.MASTER_STEP_NAME)
        .partitioner(Constants.CsvJob.MASTER_STEP_NAME, partitioner())
        .step(readCSVFileSlaveStep())
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  public Step readCSVFileSlaveStep() throws MalformedURLException {
    return stepBuilderFactory.get(Constants.CsvJob.SLAVE_STEP_NAME)
        .<OrderDTO, Order>chunk(10)
        .processor(new OrderProcessor())
        .reader(ordersReader(null))
        .writer(jpaWriter())
        .build();
  }

  @Bean
  @StepScope
  public Partitioner partitioner() {
    MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
    Resource[] resources = Utils.getResources(sourceDirectory);
    partitioner.setResources(resources);
    partitioner.partition(10);
    return partitioner;
  }

  @Bean
  public Step moveFileStep() {
    return stepBuilderFactory.get(Constants.CsvJob.MOVE_FILE_STEP)
        .tasklet(new FileMovingTasklet(sourceDirectory, processedDirectory))
        .build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<OrderDTO> ordersReader(@Value("#{stepExecutionContext['fileName']}") String filename)
      throws MalformedURLException {
    FlatFileItemReader fileItemReader = new FlatFileItemReaderBuilder<OrderDTO>()
        .name("ordersReader")
        .delimited()
        .names(new String[] {"orderId", "item", "quantity", "vendor"})
        .fieldSetMapper(new BeanWrapperFieldSetMapper<OrderDTO>() {{
          setTargetType(OrderDTO.class);
        }})
        .build();
    fileItemReader.setLinesToSkip(1);
    fileItemReader.setResource(new UrlResource(filename));
    return fileItemReader;
  }

  @Bean
  public JpaItemWriter<Order> jpaWriter() {
    JpaItemWriter<Order> itemWriter = new JpaItemWriter<>();
    itemWriter.setEntityManagerFactory(entityManagerFactory);
    return itemWriter;
  }
}