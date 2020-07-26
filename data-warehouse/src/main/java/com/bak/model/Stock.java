package com.bak.model;

import java.util.List;

public class Stock {

  
  private StockMetadata metadata;
  private List<DailySerie> dailySeries;
  public StockMetadata getMetadata() {
    return metadata;
  }
  public List<DailySerie> getDailySeries() {
    return dailySeries;
  }
  public void setDailySeries(List<DailySerie> dailySeries) {
    this.dailySeries = dailySeries;
  }
  @Override
  public String toString() {
    return "Stock [metadata=" + metadata + ", dailySeries=" + dailySeries + "]";
  }
  public void setMetadata(StockMetadata metadata) {
    this.metadata = metadata;
  }

  
}
