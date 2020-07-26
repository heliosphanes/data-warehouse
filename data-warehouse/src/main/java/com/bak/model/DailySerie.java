package com.bak.model;

import java.util.Date;

public class DailySerie {

  private Date date;
  private DailyInfo info;
  
  
  @Override
  public String toString() {
    return "DailySerie [date=" + date + ", info=" + info + "]";
  }
  public DailyInfo getInfo() {
    return info;
  }
  public void setInfo(DailyInfo info) {
    this.info = info;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
}
