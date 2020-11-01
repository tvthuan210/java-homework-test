package com.test.homework.file.handler;

import com.test.homework.file.FileConfiguration;
import com.test.homework.file.model.FileContentToJobRequest;
import com.test.homework.service.JobManager;
import com.test.homework.utils.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableBatchProcessing
public class FileHandlerConfiguration {

  private Job readCSVFilesJob;
  private JobManager jobManager;

  @Autowired
  public void setReadCSVFilesJob(Job readCSVFilesJob) {
    this.readCSVFilesJob = readCSVFilesJob;
  }

  @Autowired
  public void setJobManager(@Qualifier("FileHandlingJobManager") JobManager jobManager) {
    this.jobManager = jobManager;
  }

  @Bean
  public IntegrationFlow fileHandlingIntegration() {
    return IntegrationFlows.from(FileConfiguration.INBOUND_CHANNEL)
        .transform(fileMessageToJobRequest())
        .handle(jobManager.jobLaunchingGateway(null))
        .channel(Constants.NULL_CHANNEL)
        .get();
  }

  @Bean
  public FileContentToJobRequest fileMessageToJobRequest() {
    FileContentToJobRequest fileMessageToJobRequest = new FileContentToJobRequest();
    fileMessageToJobRequest.setJob(readCSVFilesJob);
    return fileMessageToJobRequest;
  }
}
