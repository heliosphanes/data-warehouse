package com.bak.schema;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.attribute.Geoshape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaConstraintRemote {
  protected JanusGraph janusgraph;
  protected Cluster cluster;
  protected Client client;
  protected Configuration conf;
  protected boolean useMixedIndex;
  protected String mixedIndexConfigName;


  /**
   * Constructs a graph app using the given properties.
   * @param fileName location of the properties file
   */
  public SchemaConstraintRemote(final String fileName) {
//      super(fileName);
      // the server auto-commits per request, so the application code doesn't
      // need to explicitly commit transactions
//      this.supportsTransactions = false;
      
    this.fileName = fileName;
  }
  String fileName;
  public GraphTraversalSource openGraph() throws ConfigurationException {
    conf = new PropertiesConfiguration(fileName);


    // using the remote driver for schema
    try {
        cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
        client = cluster.connect();
    } catch (Exception e) {
        throw new ConfigurationException(e);
    }

    // using the remote graph for queries
    Graph instance = EmptyGraph.instance();
    GraphTraversalSource g = instance.traversal().withRemote(conf);
    return g;
  }
  public void createSchema() {
    // get the schema request as a string
    final String req = createSchemaRequest();
    // submit the request to the server
    final ResultSet resultSet = client.submit(req);
    // drain the results completely
}
  
  protected String createSchemaRequest() {
    final StringBuilder s = new StringBuilder();

    s.append("JanusGraphManagement management = graph.openManagement(); ");
    s.append("boolean created = false; ");

    // naive check if the schema was previously created
//    s.append(
//            "if (management.getRelationTypes(RelationType.class).iterator().hasNext()) { management.rollback(); created = false; } else { ");

    // properties
    s.append("PropertyKey symbol = management.makePropertyKey(\""+DBConstant.SYMBOL+"\").dataType(String.class).make(); ");
    s.append("PropertyKey information = management.makePropertyKey(\""+DBConstant.INFORMATION+"\").dataType(String.class).make(); ");
    s.append("PropertyKey outputSize = management.makePropertyKey(\""+DBConstant.OUTPUT_SIZE+"\").dataType(String.class).make(); ");
    s.append("PropertyKey timeZone = management.makePropertyKey(\""+DBConstant.TIME_ZONE+"\").dataType(String.class).make(); ");
    s.append("PropertyKey lastRefreshed = management.makePropertyKey(\""+DBConstant.LAST_REFRESHED+"\").dataType(String.class).make(); ");
    s.append("PropertyKey date = management.makePropertyKey(\""+DBConstant.DATE+"\").dataType(Date.class).make(); ");
    s.append("PropertyKey open = management.makePropertyKey(\""+DBConstant.OPEN+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey high = management.makePropertyKey(\""+DBConstant.HIGH+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey low = management.makePropertyKey(\""+DBConstant.LOW+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey close = management.makePropertyKey(\""+DBConstant.CLOSE+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey adjustedClose = management.makePropertyKey(\""+DBConstant.ADJUSTED_CLOSE+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey dividendAmount = management.makePropertyKey(\""+DBConstant.DIVIDEND_AMOUNT+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey splitCoefficient = management.makePropertyKey(\""+DBConstant.SPLIT_COEFFICIENT+"\").dataType(Double.class).make(); ");
    s.append("PropertyKey volume = management.makePropertyKey(\""+DBConstant.VOLUME+"\").dataType(Integer.class).make(); ");
    s.append("PropertyKey dailySeries = management.makePropertyKey(\""+DBConstant.DAILY_SERIES+"\").dataType(Date.class).cardinality(org.janusgraph.core.Cardinality.LIST).make(); ");
    s.append("PropertyKey info = management.makePropertyKey(\""+DBConstant.INFO+"\").dataType(String.class).make(); ");
//
//    // vertex labels
    s.append("Vertex stock = management.makeVertexLabel(\""+DBConstant.STOCK+"\").make(); ");
    s.append("management.addProperties(stock, symbol, information, outputSize,\n" + 
        "        timeZone, lastRefreshed, date, open,\n" + 
        "        high, low, close, adjustedClose,\n" + 
        "        dividendAmount, splitCoefficient, volume, dailySeries,info); ");


    // composite indexes
    s.append("management.buildIndex(\""+DBConstant.BY_USERNAME_COMPOSITE+"\", Vertex.class).addKey("+DBConstant.SYMBOL+").indexOnly("+DBConstant.STOCK+").buildMixedIndex(\""+DBConstant.SEARCH+"\"); ");
    s.append("management.buildIndex(\"userComp\", Vertex.class).unique().addKey("+DBConstant.SYMBOL+").indexOnly("+DBConstant.STOCK+").buildCompositeIndex(); ");
    s.append("management.buildIndex(\""+DBConstant.BY_DAILY_MIX+"\", Vertex.class).addKey("+DBConstant.DAILY_SERIES+").buildMixedIndex(\""+DBConstant.SEARCH+"\"); ");
    
//    s.append("management.setConsistency(symbol, ConsistencyModifier.LOCK);");
//    s.append("management.setConsistency(userNameIndexBuilder, ConsistencyModifier.LOCK);");

    

    s.append("management.commit();");

    return s.toString();
}





}
