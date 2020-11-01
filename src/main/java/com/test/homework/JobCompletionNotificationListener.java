package com.test.homework;

import com.test.homework.repository.OrderRepository;
import com.test.homework.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
  private final OrderRepository orderRepository;

  @Autowired
  public JobCompletionNotificationListener(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  private JobLauncher simpleJobLauncher;
  private Job orderTransform;

  @Autowired
  public void setSimpleJobLauncher(JobLauncher simpleJobLauncher) {
    this.simpleJobLauncher = simpleJobLauncher;
  }

  @Autowired
  @Qualifier("orderTransformJob")
  public void setOrderTransform(Job orderTransform) {
    this.orderTransform = orderTransform;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      try {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate(Constants.JobAttribute.KEY, new Date());
        simpleJobLauncher.run(orderTransform, jobParametersBuilder.toJobParameters());
      } catch (Exception ex) {

      }
    }
  }

}
