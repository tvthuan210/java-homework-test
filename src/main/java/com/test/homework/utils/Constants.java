package com.test.homework.utils;

public class Constants {

  public static class JobAttribute {
    public static String KEY = "key";
  }

  public static String NULL_CHANNEL = "nullChannel";

  public static class CsvJob {
    public static String JOB_NAME = "readCSVFilesJob";
    public static String MASTER_STEP_NAME = "readCSVFileMasterStep";
    public static String SLAVE_STEP_NAME = "readCSVFileSlaveStep";
    public static String MOVE_FILE_STEP = "moveFileStep";
  }

  public static class OrderTransformJob {
    public static String JOB_NAME = "orderTransformJob";
    public static String MASTER_STEP_NAME = "orderTransformMasterStep";
    public static String SLAVE_STEP_NAME = "orderTransformSlaveStep";

    public static String VENDOR = "vendor";
    public static String OUTPUT_FILE = "outputFile";
  }

}
