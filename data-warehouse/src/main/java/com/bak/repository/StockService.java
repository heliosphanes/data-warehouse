package com.bak.repository;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.has;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.attribute.Text;
import com.bak.JanusClient;
import com.bak.model.DailyInfo;
import com.bak.model.DailySerie;
import com.bak.model.Stock;
import com.bak.model.StockMetadata;
import com.bak.schema.DBConstant;
import com.bak.schema.SchemaConstraintRemote;
import io.vertx.core.json.Json;


public class StockService {

  public static GraphTraversalSource graph =null;

  static {
      try {
        graph = new SchemaConstraintRemote("conf/remote-graph.properties").openGraph();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }
  public static Vertex createStock(Stock stock) {


      Vertex v = graph.addV(DBConstant.STOCK)
          .property(DBConstant.SYMBOL, stock.getMetadata().getSymbol())
          .property(DBConstant.LAST_REFRESHED, stock.getMetadata().getLastRefreshed())
          .property(DBConstant.INFORMATION, stock.getMetadata().getInformation())
          .property(DBConstant.OUTPUT_SIZE, stock.getMetadata().getOutputSize())
          .property(DBConstant.TIME_ZONE, stock.getMetadata().getTimeZone()).next();

      GraphTraversal<Vertex, Vertex> v2 = graph.V(v);
      stock.getDailySeries().stream()
           .forEach(d -> v2  
           .property(Cardinality.list,DBConstant.DAILY_SERIES,d.getDate())) ;
      v2.iterate();
//      v2.asAdmin().getEndStep().next();
      Vertex next =null; 
//      System.out.println(graph.V(next).valueMap().next());
      return next; 
  }

  public static void deleteAllStocks(Date before, Date after) {

      graph.V()
          .hasLabel(DBConstant.STOCK)
          .and(
              has(DBConstant.DAILY_SERIES, P.between(before,after))
              ).drop().iterate();               

  }
  public static Stock getStock(String symbol) {

      Vertex stockV = graph.V().hasLabel(DBConstant.STOCK)
          .has(DBConstant.SYMBOL,Text.textContains(symbol)).next();
      
      Stock stock = convertVertexToStock(stockV);
      return stock;

  }
 
  public static Stock convertVertexToStock(Vertex stockV) {
      Vertex stockV2 = graph.V(stockV.id()).next();

      System.out.println("fsdf "+graph.V(stockV).valueMap().next());
      Stock stock = new Stock();
      StockMetadata metadata = new StockMetadata();
      if (stockV2.property(DBConstant.SYMBOL).isPresent()) {
        System.out.println("true");
        String string = stockV2.property(DBConstant.SYMBOL).value().toString();
        metadata.setSymbol(string);
        stock.setMetadata(metadata);
      }
      if (stockV2.property(DBConstant.INFORMATION).isPresent()) {
       String string = stockV2.property(DBConstant.INFORMATION).value().toString();
        metadata.setInformation(string);
        stock.setMetadata(metadata);
      }
      if (stockV2.property(DBConstant.OUTPUT_SIZE).isPresent()) {
       String string = stockV2.property(DBConstant.OUTPUT_SIZE).value().toString();
        metadata.setOutputSize(string);
        stock.setMetadata(metadata);
      }
      if (stockV2.property(DBConstant.TIME_ZONE).isPresent()) {
       String string = stockV2.property(DBConstant.TIME_ZONE).value().toString();
        metadata.setOutputSize(string);
        stock.setMetadata(metadata);
      }
      if (stockV2.property(DBConstant.TIME_ZONE).isPresent()) {
       String string = stockV2.property(DBConstant.TIME_ZONE).value().toString();
        metadata.setOutputSize(string);
        stock.setMetadata(metadata);
      }
      if (stockV2.property(DBConstant.LAST_REFRESHED).isPresent()) {
       String string = stockV2.property(DBConstant.LAST_REFRESHED).value().toString();
        metadata.setLastRefreshed(string);
        stock.setMetadata(metadata);
      }
      if (stockV2.properties(DBConstant.DAILY_SERIES).hasNext()) {
        DailySerie  serie = new DailySerie();
        List<DailySerie> series = new ArrayList<>();
        if (stockV2.properties(DBConstant.DAILY_SERIES).next().property(DBConstant.INFO).isPresent()) {
          String string = stockV2.properties(DBConstant.DAILY_SERIES).next().property(DBConstant.INFO).value().toString();
          DailyInfo info = Json.decodeValue(string, DailyInfo.class);

          stockV2.properties(DBConstant.DAILY_SERIES)
                 .forEachRemaining(d -> {
                   serie.setDate((Date)d.value());
                   serie.setInfo(info);
                   series.add(serie);
                 });
          
          stock.setDailySeries(series);
        }
      }
      return stock;
  }
  
  
  public static List<Stock> searchStockBySymbol(String searchWord) {
      List<Vertex> stock = graph.V()
          .hasLabel(DBConstant.STOCK)
          .or(
              has(DBConstant.SYMBOL,Text.textContains(searchWord)),
              has(DBConstant.SYMBOL,Text.textContainsPrefix(searchWord)))
          .range(0, 5)
          .order()
          .by(DBConstant.SYMBOL)
          .toList();
      
      List<Stock> stocks = stock.stream()
          .map(StockService::convertVertexToStock)
          .collect(Collectors.toList());
      
      return stocks;
  }

  public static List<Stock> searchStocksByDateRange(Date before, Date after) {
      List<Vertex> stock = graph.V()
          .hasLabel(DBConstant.STOCK)
          .and(
              has(DBConstant.DAILY_SERIES, P.between(before,after))
              )
          .toList();
      List<Stock> stocks = stock.stream()
          .map(StockService::convertVertexToStock)
          .collect(Collectors.toList());
      return stocks;
  }

  public static List<Stock> searchStocksByDate(Date date) {

      List<Vertex> stock = graph.V()
          .hasLabel(DBConstant.STOCK)
          .and(has(DBConstant.DAILY_SERIES, P.eq(date)))
          .toList();

      List<Stock> stocks = stock.stream()
          .map(StockService::convertVertexToStock)
          .collect(Collectors.toList());
      
      System.out.println(stocks.size());
      return stocks;
    
  }
}

