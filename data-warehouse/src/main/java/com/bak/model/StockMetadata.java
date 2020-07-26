package com.bak.model;

public class StockMetadata {

  private String symbol;
  private String information;
  private String outputSize;
  private String timeZone;
  private String lastRefreshed;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getInformation() {
    return information;
  }

  public void setInformation(String information) {
    this.information = information;
  }

  public String getOutputSize() {
    return outputSize;
  }

  public void setOutputSize(String outputSize) {
    this.outputSize = outputSize;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  public String getLastRefreshed() {
    return lastRefreshed;
  }

  public void setLastRefreshed(String lastRefreshed) {
    this.lastRefreshed = lastRefreshed;
  }

  @Override
  public String toString() {
    return "Stock [symbol=" + symbol + ", information=" + information + ", outputSize=" + outputSize + ", timeZone=" + timeZone
        + ", lastRefreshed=" + lastRefreshed +  "]";
  }
}
