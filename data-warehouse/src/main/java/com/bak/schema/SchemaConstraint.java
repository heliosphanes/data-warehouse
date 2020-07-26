package com.bak.schema;

import java.util.Date;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import com.bak.JanusClient;

public class SchemaConstraint {

  static JanusGraphManagement mgmt;

  static {
    mgmt = JanusClient.getJanusClient().getGraph().openManagement();
  }

  private VertexLabel stock = mgmt
      .makeVertexLabel(DBConstant.STOCK)
      .make();


  // Properties
  private PropertyKey symbol = mgmt.makePropertyKey(DBConstant.SYMBOL)
      .dataType(String.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey information = mgmt.makePropertyKey(DBConstant.INFORMATION)
      .dataType(String.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey outputSize = mgmt.makePropertyKey(DBConstant.OUTPUT_SIZE)
      .dataType(String.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey timeZone = mgmt.makePropertyKey(DBConstant.TIME_ZONE)
      .dataType(String.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey lastRefreshed = mgmt.makePropertyKey(DBConstant.LAST_REFRESHED)
      .dataType(String.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey date = mgmt.makePropertyKey(DBConstant.DATE)
      .dataType(Date.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey open = mgmt.makePropertyKey(DBConstant.OPEN)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey high = mgmt.makePropertyKey(DBConstant.HIGH)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey low = mgmt.makePropertyKey(DBConstant.LOW)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey close = mgmt.makePropertyKey(DBConstant.CLOSE)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey adjustedClose = mgmt.makePropertyKey(DBConstant.ADJUSTED_CLOSE)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey dividendAmount = mgmt.makePropertyKey(DBConstant.DIVIDEND_AMOUNT)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey splitCoefficient = mgmt.makePropertyKey(DBConstant.SPLIT_COEFFICIENT)
      .dataType(Double.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey volume = mgmt.makePropertyKey(DBConstant.VOLUME)
      .dataType(Integer.class)
      .cardinality(Cardinality.SINGLE)
      .make();
  private PropertyKey dailySeries = mgmt.makePropertyKey(DBConstant.DAILY_SERIES)
      .dataType(Date.class)
      .cardinality(Cardinality.LIST)
      .make();
  private PropertyKey info = mgmt.makePropertyKey(DBConstant.INFO)
      .dataType(String.class)
      .cardinality(Cardinality.SINGLE)
      .make();

  private void addStockVertexProperties() {
    mgmt.addProperties(
        stock, symbol, information, outputSize,
        timeZone, lastRefreshed, date, open,
        high, low, close, adjustedClose,
        dividendAmount, splitCoefficient, volume, dailySeries,info
    );
  }

  private  void buildIndexesAndConsistency() {
//    JanusGraphIndex userNameIndexBuilder =
//    mgmt
//        .buildIndex(DBConstant.BY_SYMBOL, Vertex.class)
//        .addKey(symbol)
//        .indexOnly(stock)
//        
//        .buildCompositeIndex();
//        .buildMixedIndex(DBConstant.SEARCH);
    mgmt
        .buildIndex(DBConstant.BY_USERNAME_COMPOSITE, Vertex.class)
        .addKey(symbol).unique().indexOnly(stock)
        .buildMixedIndex(DBConstant.SEARCH);

    mgmt.buildIndex(DBConstant.BY_DAILY_MIX, Vertex.class)
        .addKey(dailySeries)
        .indexOnly(stock)

        .buildMixedIndex(DBConstant.SEARCH);

  
//    ManagementSystem.awaitGraphIndexStatus(g, graphIndexName)
    ;
//    mgmt.setConsistency(symbol, ConsistencyModifier.LOCK);
//    mgmt.setConsistency(userNameIndexBuilder,
//        ConsistencyModifier.LOCK);
  }

  public static SchemaConstraint schemaConstraint() {
    return new SchemaConstraint();
  }

  public SchemaConstraint prepareSchema() {
    addStockVertexProperties();
    buildIndexesAndConsistency();
    return this;
  }
  
  public void build() {
    mgmt.commit();
  }

}

