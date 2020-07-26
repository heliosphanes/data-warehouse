package com.bak.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DateUtils {

  private static String pattern = "yyyy-MM-dd";
  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
  
  public static Optional<Date> parseDate(String datePatternValue) {
    Date date = null;
    try {
      date = simpleDateFormat.parse(datePatternValue);
    } catch (java.text.ParseException e) {
      // Replace this with log4j
      System.out.println(e.getMessage());
    }
    return Optional.ofNullable(date);
    
  }
}
