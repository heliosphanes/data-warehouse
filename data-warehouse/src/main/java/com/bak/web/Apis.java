package com.bak.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import com.bak.engine.MasterVertx;
import com.bak.model.Stock;
import com.bak.repository.StockService2;
import com.bak.util.DateUtils;
import com.google.common.base.Predicate;
import io.vavr.Tuple;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.CorsHandler;

public class Apis {

	private static int serverPort = 8280;

    private static final String X_PINGARUNER = "X-PINGARUNER";
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final String X_TOTAL_COUNT = "X-Total-Count";
  
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String CONTENT_RANGE = "Content-Range";
  
    public static CorsHandler corsHandler() {
      return CorsHandler.create("*").allowedHeader("*").allowedMethod(HttpMethod.GET)
          .allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.OPTIONS)
          .allowedHeader(X_PINGARUNER).allowedHeader(CONTENT_TYPE).allowedHeader(X_REQUESTED_WITH)
          .allowedHeader(ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeader(X_TOTAL_COUNT)
          .exposedHeader(CONTENT_RANGE);
    }
	public static void startServer() {
		Vertx vertx = MasterVertx.INSTANCE.getInstance();
		Router router = Router.router(vertx);
		router.route().handler(Apis.corsHandler());
		router.route(HttpMethod.GET, "/api/stock/:symbol").blockingHandler(Apis::getStocksBySymbolHandler);
		router.route(HttpMethod.GET, "/api/stocks/search").blockingHandler(Apis::getStocksHandler);
		vertx.createHttpServer().requestHandler(router::accept).listen(serverPort);
	}
    public static void getStocksBySymbolHandler(
        final RoutingContext routingContext) {
      HttpServerResponse response = routingContext.response();

      Optional<String> symbol = Optional.ofNullable(routingContext.request().getParam("symbol"));

      symbol.map(StockService2::getStock)
            .map(JsonObject::mapFrom)
            .map(d -> {
              return response
                  .setChunked(true).write(d.toString());
            })
            .orElse(response.setChunked(true).setStatusCode(404))
            .end();;

//            .ifPresent(;

//      date.map(DateUtils::parseDate)
//          .flatMap(d -> d)
//          .map(StockService::searchStocksByDate)
//          .map(JsonObject::mapFrom)
//          .ifPresent((d)-> {
//            response
//                .setChunked(true).write(d.toString()).end();
//          });
    }

    public static void getStocksHandler(
        final RoutingContext routingContext) {
      System.out.println("call");
      HttpServerResponse response = routingContext.response();

      Optional<String> from = Optional.ofNullable(routingContext.request().getParam("from"));
      Optional<String> to = Optional.ofNullable(routingContext.request().getParam("to"));
      Optional<String> date = Optional.ofNullable(routingContext.request().getParam("date"));
      Optional<String> symbol = Optional.ofNullable(routingContext.request().getParam("symbol"));
      
//      Date fromDate = from.map(DateUtils::parseDate).flatMap(d ->d).get();
//      Date toDate = to.map(DateUtils::parseDate).flatMap(d ->d).get();
      Optional<Date> fromOpt = from.map(DateUtils::parseDate).flatMap(d ->d);
      Optional<Date> toOpt = to.map(DateUtils::parseDate).flatMap(d ->d);

      symbol.map(StockService2::searchStockBySymbol)
          .map(d ->{
            List<JsonObject> collect = d.stream()
              .map(stock -> new JsonObject().put("symbol", stock.getMetadata().getSymbol()))
              .collect(Collectors.toList());
            return collect;
          })
          .map(JsonArray::new)
          .ifPresent((d)-> {
            System.out.println();
            response
                .setChunked(true).write(d.toString()).end();
          });
      
      if(fromOpt.isPresent() && toOpt.isPresent()) {
        List<Stock> searchStocksByDateRange = StockService2.searchStocksByDateRange(fromOpt.get(),toOpt.get());
        List<JsonObject> collect = searchStocksByDateRange.stream()
              .map(stock -> new JsonObject().put("symbol", stock.getMetadata().getSymbol()))
              .collect(Collectors.toList());
        JsonArray jsonArray = new JsonArray(collect);
            response
                .setChunked(true).write(jsonArray.toString()).end();
      }


      date.map(DateUtils::parseDate)
          .flatMap(d -> d)
          .map(StockService2::searchStocksByDate)
          .map(d ->{
            List<JsonObject> collect = d.stream()
              .map(stock -> new JsonObject().put("symbol", stock.getMetadata().getSymbol()))
              .collect(Collectors.toList());
            return collect;
          })
          .map(JsonArray::new)
          .ifPresent((d)-> {
            System.out.println();
            response
                .setChunked(true).write(d.toString()).end();
          });
//      String param = routingContext.queryParam("q").get(0).toString();
//
//      symbol.map(StockService::getStock)
//            .map(JsonObject::mapFrom)
//            .ifPresent((d)-> {
//              response
//                  .setChunked(true).write(d.toString()).end();
//            });
//
//      System.out.println(param);
//      List<Stock> search = StockService.searchStockBySymbol(param);
//      search.stream().forEach(System.out::println);
//      JsonArray jsonArray = new JsonArray(search);
//
//      System.out.println(jsonArray);
      response
          .setChunked(true).write("hello").end();
    }
}
