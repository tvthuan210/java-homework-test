package com.test.homework.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileParsingCompletionListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(FileParsingCompletionListener.class);
  private MessageChannel messageChannel;

  @Autowired
  @Qualifier("orderTransformChannel")
  public void setMessageChannel(MessageChannel messageChannel) {
    this.messageChannel = messageChannel;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("Parse file completed. Launch data transformation job");
      messageChannel.send(MessageBuilder.withPayload("DONE").build());
    }
  }
}
