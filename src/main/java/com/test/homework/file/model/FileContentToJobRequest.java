package com.test.homework.file.model;

import com.test.homework.utils.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;

import java.util.Date;

public class FileContentToJobRequest {

  private Job job;
  public void setJob(Job job) {
    this.job = job;
  }

  @Transformer
  public JobLaunchRequest toRequest() {
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    jobParametersBuilder.addDate(Constants.JobAttribute.KEY, new Date());
    return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
  }
}
