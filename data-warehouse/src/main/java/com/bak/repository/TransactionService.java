package com.bak.repository;

import java.util.List;
import com.bak.JanusClient;
import com.bak.model.Stock;

public class TransactionService {
  
  public static void  persistAllStocks(List<Stock> stocks) {

    stocks.stream().peek(d -> System.out.println(d.getDailySeries().size())).forEach(StockService::createStock);
//    StockService.graph.tx().commit();
  }

}
