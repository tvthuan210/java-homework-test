package com.test.homework.service;

import com.test.homework.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@StepScope
public class FileMovingTasklet implements Tasklet {

  private static final Logger log = LoggerFactory.getLogger(FileMovingTasklet.class);

  private String sourceDirectory;
  private String targetDirectory;

  public FileMovingTasklet(String srcDir, String targetDir) {
    this.sourceDirectory = srcDir;
    this.targetDirectory = targetDir;
  }

  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    Resource[] resources = Utils.getResources(sourceDirectory);
    for(Resource r: resources) {
      File file = r.getFile();
      try {
        Files.move(Paths.get(file.getAbsolutePath()), Paths.get(targetDirectory + file.getName()));
      } catch (Exception ex) {
        log.error("Cannot move {} to processed folder", file.getName(), ex);
      }
    }
    return RepeatStatus.FINISHED;
  }
}
