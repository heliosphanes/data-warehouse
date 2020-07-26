package com.bak.model;

import io.vertx.core.json.JsonObject;

public class DailyInfo {

  private Double open; 
  private Double high; 
  private Double low; 
  private Double close; 
  private Double adjustedClose; 
  private int volume;
  private Double dividendAmount;
  private Double splitCoefficient;

  public Double getOpen() {
    return open;
  }
  public void setOpen(Double open) {
    this.open = open;
  }
  public Double getHigh() {
    return high;
  }
  public void setHigh(Double high) {
    this.high = high;
  }
  public Double getLow() {
    return low;
  }
  public void setLow(Double low) {
    this.low = low;
  }
  public Double getClose() {
    return close;
  }
  public void setClose(Double close) {
    this.close = close;
  }
  public Double getAdjustedClose() {
    return adjustedClose;
  }
  public void setAdjustedClose(Double adjustedClose) {
    this.adjustedClose = adjustedClose;
  }
  public int getVolume() {
    return volume;
  }
  public void setVolume(int volume) {
    this.volume = volume;
  }
  public Double getDividendAmount() {
    return dividendAmount;
  }
  public void setDividendAmount(Double dividendAmount) {
    this.dividendAmount = dividendAmount;
  }
  public Double getSplitCoefficient() {
    return splitCoefficient;
  }
  public void setSplitCoefficient(Double splitCoefficient) {
    this.splitCoefficient = splitCoefficient;
  }
  @Override
  public String toString() {
    return JsonObject.mapFrom(this).toString();
  }
}
