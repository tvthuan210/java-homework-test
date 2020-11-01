package com.test.homework.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

public class Utils {

  public static Resource[] getResources(String sourceDirectory) {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = null;
    try {
      resources = resolver.getResources("file:" + sourceDirectory + "*");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return resources;
  }
}
