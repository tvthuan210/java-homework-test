package com.test.homework.file.poller;

import org.springframework.integration.file.filters.FileListFilter;

import java.util.Collections;
import java.util.List;

public class OnlyOneFileListFilter<File> implements FileListFilter<File> {

  @Override
  public List<File> filterFiles(File[] files) {
    if (files.length > 0) {
      return Collections.singletonList(files[0]);
    }
    return Collections.emptyList();
  }
}
