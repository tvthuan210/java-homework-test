package com.test.homework.file.poller;

import com.test.homework.file.FileConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.DirectoryScanner;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.RecursiveDirectoryScanner;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;

import java.io.File;
import java.util.Arrays;

@Configuration
public class FilePollingConfiguration {

  @Bean
  public IntegrationFlow filePollingIntegration(
      @Value("${file.poller.fixedDelay}") long fixedDelay,
      @Value("${file.poller.maxMessagesPerPoll}") int maxMessagesPerPoll,
      MessageSource<File> fileReadingMessageSource) {

    return IntegrationFlows.from(fileReadingMessageSource,
        c -> c.poller(Pollers.fixedDelay(fixedDelay)
            .maxMessagesPerPoll(maxMessagesPerPoll)))
        .channel(FileConfiguration.INBOUND_CHANNEL)
        .get();
  }

  @Bean
  public FileReadingMessageSource fileReadingMessageSource(DirectoryScanner directoryScanner,
      @Value("${file.poller.source.directory}") String sourceDirectory) {
    FileReadingMessageSource source = new FileReadingMessageSource();
    source.setDirectory(new File(sourceDirectory));
    source.setScanner(directoryScanner);
    source.setAutoCreateDirectory(true);
    return source;
  }

  @Bean
  public DirectoryScanner directoryScanner(@Value("${file.poller.filename.regex}") String regex) {
    DirectoryScanner scanner = new RecursiveDirectoryScanner();
    CompositeFileListFilter<File> filter = new CompositeFileListFilter<>(
        Arrays.asList(
            new AcceptOnceFileListFilter<>(),
            new RegexPatternFileListFilter(regex),
            new OnlyOneFileListFilter<>())
    );
    scanner.setFilter(filter);
    return scanner;
  }
}