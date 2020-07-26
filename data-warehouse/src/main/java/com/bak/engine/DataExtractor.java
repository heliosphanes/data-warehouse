package com.bak.engine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.bak.model.DailyInfo;
import com.bak.model.DailySerie;
import com.bak.model.Stock;
import com.bak.model.StockMetadata;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;

public class DataExtractor {

  static int counter = 0;
  public static void main(String...args) {
    List<Stock>stocks = getStocks();
    List<JsonObject> collect = stocks.stream()
        .flatMap(d -> d.getDailySeries().stream())
        .map(d -> JsonObject.mapFrom(d))
        .collect(Collectors.toList());
    collect.forEach(System.out::println);
  }
  static  String pattern = "yyyy-mm-dd";
  static  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
  public static List<Stock> getStocks() {

    Vertx vertx = Vertx.vertx();
//    Vertx vertx = MasterVertx.INSTANCE.getInstance();
    String concat = DataExtractor.class.getClassLoader().getResource("symbols-history").getPath().toString();

    List<String> files =
        vertx.fileSystem().readDirBlocking(concat);

    List<JsonObject> allSymbolsInJsonObjects = files.stream()
        .map(vertx.fileSystem()::readFileBlocking)
        .map(d -> d.toJsonObject())
        .collect(Collectors.toList());

    List<Stock> stocksList = allSymbolsInJsonObjects.stream().map(d -> {
      List<DailySerie> series = d.getJsonObject("dailySeries").stream().map(a -> {
        DailyInfo dailyInfo = ((JsonObject) a.getValue()).mapTo(DailyInfo.class);
        DailySerie dailySerie = new DailySerie();
        dailySerie.setInfo(dailyInfo);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date;
        try {
          date = simpleDateFormat.parse(a.getKey());
          dailySerie.setDate(date);
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return dailySerie;
      }).collect(Collectors.toList()); 
      StockMetadata metadata = d.getJsonObject("metadata").mapTo(StockMetadata.class);
      Stock stock = new Stock();
      stock.setDailySeries(series);

      stock.setMetadata(metadata);
      return stock;
    }).collect(Collectors.toList());


    return stocksList;
  }


}
