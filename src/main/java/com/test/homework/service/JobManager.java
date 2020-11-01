package com.test.homework.service;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;

public class JobManager {

  protected JobRepository jobRepository;
  protected JobBuilderFactory jobBuilderFactory;
  protected StepBuilderFactory stepBuilderFactory;
  protected EntityManagerFactory entityManagerFactory;

  @Bean
  public JobLauncher simpleJobLauncher(@Qualifier("testExecutor") TaskExecutor taskExecutor) {
    SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
    simpleJobLauncher.setJobRepository(jobRepository);
    simpleJobLauncher.setTaskExecutor(taskExecutor);
    return simpleJobLauncher;
  }

  @Bean(name = "testExecutor")
  TaskExecutor taskExecutor(@Value("${file.poller.pool.size}") int poolSize) {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(poolSize);
    return taskExecutor;
  }

  @Bean
  public JobLaunchingGateway jobLaunchingGateway(JobLauncher jobLauncher) {
    JobLaunchingGateway jobLaunchingGateway = new JobLaunchingGateway(jobLauncher);
    return jobLaunchingGateway;
  }

  @Autowired
  public void setJobRepository(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Autowired
  public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Autowired
  public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
  }

  @Autowired
  public void setEntityManagerFactory(EntityManagerFactory emf) {
    this.entityManagerFactory = emf;
  }
}
